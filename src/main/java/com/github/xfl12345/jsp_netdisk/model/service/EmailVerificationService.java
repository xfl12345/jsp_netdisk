package com.github.xfl12345.jsp_netdisk.model.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.xfl12345.jsp_netdisk.appconst.api.result.EmailVerificationApiResult;
import com.github.xfl12345.jsp_netdisk.appconst.api.result.SentEmailApiResult;
import com.github.xfl12345.jsp_netdisk.appconst.api.request.EmailVerificationRequestField;
import com.github.xfl12345.jsp_netdisk.appconst.field.MySessionAttributes;
import com.github.xfl12345.jsp_netdisk.appconst.field.TbAccountField;
import com.github.xfl12345.jsp_netdisk.model.dao.EmailVerificationDao;
import com.github.xfl12345.jsp_netdisk.model.pojo.EmailVerificationLogWithLock;
import com.github.xfl12345.jsp_netdisk.model.pojo.database.EmailVerification;
import com.github.xfl12345.jsp_netdisk.model.pojo.database.EmailVerificationLog;
import com.github.xfl12345.jsp_netdisk.model.pojo.database.EmailVerificationLogItem;
import com.github.xfl12345.jsp_netdisk.model.pojo.database.TbAccount;
import com.github.xfl12345.jsp_netdisk.model.pojo.result.EmailVerificationLogResult;
import com.github.xfl12345.jsp_netdisk.model.utility.JsonRequestUtils;
import com.github.xfl12345.jsp_netdisk.model.utility.MyStrIsOK;
import com.github.xfl12345.jsp_netdisk.model.utility.SimpleMimeMessageBuilder;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.xfl12345.jsp_netdisk.StaticSpringApp;
import com.github.xfl12345.jsp_netdisk.appconst.field.VerificationEmailHtmlIdField;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import static com.github.xfl12345.jsp_netdisk.StaticSpringApp.appInfo;
import static com.github.xfl12345.jsp_netdisk.StaticSpringApp.myConst;

/**
 * 发电子邮件服务实现类
 *
 * @author makejava
 * @since 2021-04-19 16:00:51
 */
@Service("emailVerificationService")
public class EmailVerificationService implements IEmailSentCallback {

    private final Logger logger = LoggerFactory.getLogger(EmailVerificationService.class);

    /**
     * service 套 service，老套娃了
     */
    @Autowired
    private EmailService emailService;

    @Autowired
    private EmailVerificationDao emailVerificationDao;

    @Autowired
    private TbAccountService tbAccountService;

    public final ConcurrentHashMap<String, EmailVerificationLogWithLock> emailVerificationMap = new ConcurrentHashMap<>();

    /**
     * TODO 补完 emailVerificationMap 的删除操作、补完邮箱验证码日志回写到数据库的操作
     */


    public void logAfterSent(
            String email,
            EmailVerificationLogItem logItem){
        EmailVerificationLogWithLock emailVerificationLogWithLock = emailVerificationMap.get(email);
        //解决高并发读写时，发生脏写。为防止数据被覆盖的情况出现，
        // 使用 ConcurrentHashMap 的 putIfAbsent 方法进行实例化
        if( emailVerificationLogWithLock == null  ){
            emailVerificationLogWithLock = emailVerificationMap.putIfAbsent(email, new EmailVerificationLogWithLock() );
            if(emailVerificationLogWithLock == null){
                emailVerificationLogWithLock = emailVerificationMap.get(email);
            }
        }
        //插入logItem
        emailVerificationLogWithLock.writeLock.lock();
        ArrayList<EmailVerificationLogItem> items = emailVerificationLogWithLock.items;
        for (int i = 0; i < items.size(); i++) {
            EmailVerificationLogItem item = items.get(i);
            if(item.username.equals(logItem.username)){
                if(item.emailVerificationCode.equals(logItem.emailVerificationCode)){
                    item.sentDate = logItem.sentDate;
                    item.sentEmailSuccess = logItem.sentEmailSuccess;
                }
            }
        }
        emailVerificationLogWithLock.writeLock.unlock();
    }


    public EmailVerificationLogResult logBeforeSent(TbAccount tbAccount){
        EmailVerificationLogResult logResult = new EmailVerificationLogResult();
        //预先获取邮箱地址
        String email = tbAccount.getEmail();
        //预先生成验证码
        String emailVerificationCode = generateEmailVerificationCode(myConst.getEmailVerificationCodeLength());
        //解决高并发读写时，发生脏写。为防止数据被覆盖的情况出现，
        // 使用 ConcurrentHashMap 的 putIfAbsent 方法进行实例化
        EmailVerificationLogWithLock logWithLock = emailVerificationMap.get(email);
        if( logWithLock == null  ){
            logWithLock = emailVerificationMap.putIfAbsent(email, new EmailVerificationLogWithLock() );
            if(logWithLock == null){
                logWithLock = emailVerificationMap.get(email);
            }
        }
        //预先检查是否满足发送邮件的频率限制（防止被滥用API，滥发邮件）
        logResult.sentEmailApiResult = isUnderSentEmailLimitation(tbAccount);
        //满足发邮件的频率限制
        if(logResult.sentEmailApiResult.equals(SentEmailApiResult.SUCCEED)) {
            //开启插入logItem的操作，先上锁，阻塞所有读写操作
            logWithLock.writeLock.lock();
            //防止 验证码碰撞 导致发完邮件后无法唯一确定 需要被更新的 EmailVerificationLogItem
            while (logWithLock.emailVerificationCodeSet.contains(emailVerificationCode)) {
                emailVerificationCode = generateEmailVerificationCode(myConst.getEmailVerificationCodeLength());
            }
            EmailVerificationLogItem logItem = new EmailVerificationLogItem();
            logItem.accountId = tbAccount.getAccountId().toString();
            logItem.username = tbAccount.getUsername();
            logItem.emailVerificationCode = emailVerificationCode;
            logWithLock.items.add(logItem);
            //添加 验证码 到set
            logWithLock.emailVerificationCodeSet.add(emailVerificationCode);
            //插入logItem的操作完成，解锁，允许其它所有读写操作
            logWithLock.writeLock.unlock();
            logResult.emailVerificationCode = emailVerificationCode;
        }
        return logResult;
    }

    @Deprecated
    public void insertEmailVerificationLog(
            EmailVerification emailVerification,
            EmailVerificationLogItem logItem){
        EmailVerificationLog log;
        //如果是第一次投递邮件
        if( MyStrIsOK.isEmpty(emailVerification.getVerificationLog()) ){
            log = new EmailVerificationLog();
        }
        else {
            log = JSON.parseObject(emailVerification.getVerificationLog(), EmailVerificationLog.class);
        }
        log.items.add(logItem);
        String jsonLog = (  (JSONObject) JSONObject.toJSON(log)  ).toJSONString();
        emailVerification.setVerificationLog(jsonLog);
    }

    /**
     * 转换 MimeMessage 对象为 EmailVerificationLogItem 对象
     * @param message 电子邮件实体
     * @param isSentSuccess 是否发送成功
     * @return  EmailVerificationLogItem 对象
     */
    public EmailVerificationLogItem generateEmailVerificationLogItem(
            MimeMessage message,
            boolean isSentSuccess) throws MessagingException, IOException {
        EmailVerificationLogItem emailVerificationLogItem = new EmailVerificationLogItem();
        emailVerificationLogItem.sentEmailSuccess = isSentSuccess;
        // 提取收件人邮箱地址
//        Address address =  message.getRecipients(Message.RecipientType.TO)[0];
//        String mail2address = emailService.getRecipientEmailAddress(address);
        // 获取电子邮件发送时间
        emailVerificationLogItem.sentDate = message.getSentDate();
        // 获取邮件生肉内容
        String emailContent = message.getContent().toString();
        // 获取邮件HTML document
        Document emailHtmlDocument = Jsoup.parse(emailContent);
        // 提取邮箱验证码
        emailVerificationLogItem.emailVerificationCode =
                emailHtmlDocument.getElementById(VerificationEmailHtmlIdField.ContentId.verificationCode)
                        .text();
        // 提取当时注册这个邮箱的用户名
        emailVerificationLogItem.username =
                emailHtmlDocument.getElementById(VerificationEmailHtmlIdField.ContentId.username)
                        .text();
        // 提取当时注册这个邮箱的账号ID
        emailVerificationLogItem.accountId =
                emailHtmlDocument.getElementById(VerificationEmailHtmlIdField.ContentId.accountId)
                        .text();
        return emailVerificationLogItem;
    }

    /**
     * 发送邮件的结果
     * @param message 最终邮件内容（实际发送邮件内容）
     * @param result       发送邮件的结果（成功了还是失败了）
     * @param causeDetail    邮件服务器回执
     */
    @Override
    public void afterSent(MimeMessage message, boolean result, String causeDetail) {
            try {//发验证码，肯定唯一确认地发给一个邮箱，收件地址只能是一个
                //获取Address对象，Address对象 被toString 之后会获得一个包含收件人和邮箱地址的字符串，需要提纯
                Address address =  message.getRecipients(Message.RecipientType.TO)[0];
                //提纯，获取收件人邮箱地址
                String mail2address = emailService.getRecipientEmailAddress(address);
                if (result) {
                    logger.info("发送验证码邮件给" + address + "已成功！");
                } else {
                    logger.error("发送验证码邮件给" + address + "失败！原因：" + causeDetail);
                }
                logAfterSent(mail2address, generateEmailVerificationLogItem(message, result));
//                    int affectedRowCount = 0;
//                    try {
//                        affectedRowCount = this.emailVerificationDao.insert(emailVerificationNewLine);
//                    } catch (DataAccessException e) {
//                        Throwable cause = e.getCause();
//                        if (cause instanceof MySQLIntegrityConstraintViolationException) {
////                                loginApiResult = LoginApiResult.DUPLICATE_KEY;
//                        } else {
//                            logger.error( "插入发验证邮件记录发生未知错误");
//                        }
//                    }

//                    System.exit(0);// debug
            } catch (MessagingException | IOException e) {
                e.printStackTrace();
            }
    }

    /**
     * 为防止被滥用API，滥发邮件，检查当前邮箱还能不能继续投递了。
     * 这是 sendEmail 函数的子逻辑，所以复用了 sendEmail 函数的API结果集。
     * 如果超过了安全限制，则不允许再被投递，将返回非 SUCCESS 结果。
     * @return 如果返回的不是 SUCCESS ，则意味着不允许继续投递邮件
     */
    public SentEmailApiResult isUnderSentEmailLimitation(TbAccount tbAccount){
        //预先设置一个错误
        SentEmailApiResult sentEmailApiResult = SentEmailApiResult.OTHER_FAILED;
        //获取邮箱
        String email = tbAccount.getEmail();
        //解决高并发读写时，发生脏写。为防止数据被覆盖的情况出现，
        // 使用 ConcurrentHashMap 的 putIfAbsent 方法进行实例化
        EmailVerificationLogWithLock logWithLock = emailVerificationMap.get(email);
        if( logWithLock == null  ){
            logWithLock = emailVerificationMap.putIfAbsent(email, new EmailVerificationLogWithLock() );
            if(logWithLock == null){
                logWithLock = emailVerificationMap.get(email);
            }
        }

        logWithLock.readLock.lock();
        // 先检查是否在符合发送邮件的限制要求（防止被滥用API，滥发邮件）
        //首次发送，当然允许
        int logArraySize = logWithLock.items.size();
        if( logArraySize == 0 ) {
            sentEmailApiResult = SentEmailApiResult.SUCCEED;
        }
        else {
            //是否达到或超过当天上限
            if(logArraySize < myConst.getSentEmailMaxFrequencyPeerDay()){
                ArrayList<EmailVerificationLogItem> items = logWithLock.items;
                //获取最后一条数据（最新的）
                EmailVerificationLogItem item = items.get(items.size() -1);
                //对比间隔时间是否超过最大频率
                long getSentEmailMaxFrequencyPeerMillinseconds = myConst.getSentEmailMaxFrequencyPeerSeconds() * 1000L;
                Date currDate = new Date();
                if( currDate.getTime() - item.sentDate.getTime() < getSentEmailMaxFrequencyPeerMillinseconds ){
                    sentEmailApiResult = SentEmailApiResult.SUCCEED;
                }
                sentEmailApiResult = SentEmailApiResult.FAILED_FREQUENCY_MAX;
            }
            sentEmailApiResult = SentEmailApiResult.FAILED_TODAY_MAX;
        }
        logWithLock.readLock.unlock();
        return sentEmailApiResult;
    }

    public SentEmailApiResult sendEmail(HttpSession session){
        TbAccount tbAccount = (TbAccount) session.getAttribute(MySessionAttributes.TB_ACCOUNT);
        if(tbAccount == null){
            return SentEmailApiResult.FAILED_PERMISSION_DENIED;
        }
        return sendEmail(tbAccount);
    }

    /**
     * 投递包含电子邮箱验证码的 电子邮件 到用户的注册邮箱
     * @param tbAccount 用户信息
     * @return SentEmailApiResult 结果集
     */
    public SentEmailApiResult sendEmail(TbAccount tbAccount) {
        // 先检查是否在符合发送邮件的限制要求（防止被滥用API，滥发邮件）
        EmailVerificationLogResult logResult = logBeforeSent(tbAccount);
        SentEmailApiResult sentEmailApiResult = logResult.sentEmailApiResult;
        //满足限制要求
        if( sentEmailApiResult.equals(SentEmailApiResult.SUCCEED) ){
            try {//生成邮件 并发送
                emailService.sendMimeMessage(
                        generateEmailObj(tbAccount, logResult.emailVerificationCode), this);
                sentEmailApiResult = SentEmailApiResult.SUCCEED;
            }
            catch (Exception e){
                logger.error(e.toString());
                sentEmailApiResult = SentEmailApiResult.OTHER_FAILED;
            }
        }
        return sentEmailApiResult;
    }

    /**
     * @return MimeMessage对象
     * @throws UnsupportedEncodingException InternetAddress类抛出的编码不支持异常
     * @throws MessagingException MimeMessage类抛出的参数设置异常
     */
    public MimeMessage generateEmailObj(TbAccount tbAccount, String emailVerificationCode)
            throws IOException, MessagingException {
        String mail2address = tbAccount.getEmail();
        String recipientName = tbAccount.getAccountId().toString();
        SimpleMimeMessageBuilder message = emailService.getSimpleMimeMessageBuilder();
        //   From: 发件人
        message.setFrom(new InternetAddress(emailService.getMyEmailAddress(), appInfo.getAuthor(), StandardCharsets.UTF_8.name()));
        //   To: 收件人
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(mail2address, recipientName, StandardCharsets.UTF_8.name()));
        //   Subject: 邮件主题
        message.setSubject("[" + appInfo.getAppName() + "] 邮箱验证码", StandardCharsets.UTF_8.name());
        //   Content: 邮件正文（可以使用html标签）
        message.setContent( generateEmailContent(tbAccount, emailVerificationCode), "text/html;charset=UTF-8");
//        logger.debug( " Test getContent = "+ message.build().getContent().toString());
        return message.build();
    }

    /**
     * 生成邮件正文内容
     * @return 邮件正文
     */
    public String generateEmailContent(TbAccount tbAccount, String emailVerificationCode) {
        Document emailHtmlDocument = StaticSpringApp.getVerificationEmailTemplate();
        // 设置邮件里显示的网站名称
        Element websiteNameElement = emailHtmlDocument.getElementById(VerificationEmailHtmlIdField.websiteNameId);
        websiteNameElement.text(appInfo.getAppName());
        // 设置邮件正文内容的标题（非邮件自身属性里的标题，这个是写在邮件正文里的）
        Element contentTitleElement = emailHtmlDocument.getElementById(VerificationEmailHtmlIdField.contentTitleId);
        contentTitleElement.text("绑定邮箱");
        // 设置邮件模板里的正文内容
        Element contentMainBodyElement = emailHtmlDocument.getElementById(VerificationEmailHtmlIdField.contentMainBodyId);
        // 先清除模板里的正文内容
        contentMainBodyElement.html("");

        //写入账号
        generateHtmlElement("p","您的账号是：",
                VerificationEmailHtmlIdField.ContentId.accountId, tbAccount.getAccountId().toString())
                .appendTo(contentMainBodyElement);

        //写入用户名
        generateHtmlElement("p","您的用户名是：",
                VerificationEmailHtmlIdField.ContentId.username, tbAccount.getUsername())
                .appendTo(contentMainBodyElement);

        //写入注册邮箱地址
        generateHtmlElement("p","您的注册邮箱是：",
                VerificationEmailHtmlIdField.ContentId.email, tbAccount.getEmail())
                .appendTo(contentMainBodyElement);

        //生成验证码HTML元素
        Element verificationCodeElement = generateHtmlElement("p","您的邮箱验证码是：",
                VerificationEmailHtmlIdField.ContentId.verificationCode, emailVerificationCode );
        //生成有效时间提示信息
        String vaildTimeWarnning = "（" + myConst.getEmailVerificationCodeVaildTimeInMinutes() + "分钟内有效）";
        //拼接 有效时间字符串 到 验证码HTML元素内部
        verificationCodeElement.appendText(vaildTimeWarnning);
        //写入验证码HTML元素到正文主体
        verificationCodeElement.appendTo(contentMainBodyElement);

        return emailHtmlDocument.toString();
    }

    /**
     * 生成邮箱验证码
     * 伪随机生成一个 codeLength 位 十六进制(HEX)的字符串
     * 不过事实上应该无法被预测
     *
     * @return codeLength 位的 十六进制(HEX)的伪随机字符串
     */
    public String generateEmailVerificationCode(int codeLength) {
        StringBuilder verificationCodeBuffer = new StringBuilder(codeLength);
        Random random = new Random(System.currentTimeMillis());
        byte[] commonHash = DigestUtils.sha1(StaticSpringApp.getUUID());
        long hash = 1315423911 + random.nextLong();
        byte[] takeNum = new byte[1];
        int finishCodeCount = 0;
        boolean toLowerCase = false;
        // 以下哈希算法我自己都不清楚发生碰撞的概率，但只求尽可能地难以被预测
        for (int i = 0; finishCodeCount < codeLength; ) {
            if (random.nextBoolean()) {//Justin Sobel写的一个 位操作 的哈希函数
                hash ^= ((hash << 5) + commonHash[i] + (hash >> 2));
                toLowerCase = false;
            } else {//变体算法
                hash ^= ((hash << 4) + commonHash[i] + (hash >> 1));
                toLowerCase = true;
            }
            takeNum[0] = (byte) ((hash & 0xFF)
                    + ((hash & 0xFF00) >> 8)
                    + ((hash & 0xFF0000) >> 16)
                    + ((hash & 0xFF000000) >> 24));
            String str = Hex.encodeHexString(takeNum, toLowerCase);
            if (myConst.getEmailVerificationCodeLength() - finishCodeCount >= 2) {
                verificationCodeBuffer.append(str);
                finishCodeCount += 2;
            } else {
                verificationCodeBuffer.append(str.charAt(0));
                finishCodeCount += 1;
            }
            if (i < commonHash.length - 1)
                i++;
            else
                i = 0;
        }
        return verificationCodeBuffer.toString();
    }

    private Element generateHtmlElement(String tagName, String introduction, String idOfValue, String mainValue){
        Element element = new Element(tagName);
        element.text(introduction);
        Element content = new Element("b");
        content.attr("id",idOfValue);
        content.text(mainValue);
        content.appendTo(element);
        return element;
    }


    /**
     * 激活邮箱API
     * @return 返回EmailVerificationApiResult API结果
     */
    public EmailVerificationApiResult activeEmail(HttpServletRequest request){
        TbAccount tbAccount = (TbAccount) request.getSession().getAttribute(MySessionAttributes.TB_ACCOUNT);
        //只有登录了才可以验证邮箱
        if(tbAccount == null){
            return EmailVerificationApiResult.FAILED_NO_LOGIN;
        }
        //格式化请求数据为JSON对象
        JSONObject requesetJsonObject = null;
        String code;
        try {
            requesetJsonObject = JsonRequestUtils.getJsonObject(request);
            code = (String) requesetJsonObject.get(EmailVerificationRequestField.EMAIL_VERIFICATION_CODE);
        }
        catch (Exception e){
            return EmailVerificationApiResult.FAILED_REQUEST_FORMAT_ERROR;
        }
        EmailVerificationApiResult apiResult = checkCode(tbAccount.getEmail(), code);
        if(apiResult.equals(EmailVerificationApiResult.SUCCEED)){
            //局部更新，单纯修改激活状态
            TbAccount activeAccount = new TbAccount();
            activeAccount.setAccountId(tbAccount.getAccountId());
            activeAccount.setAccountStatus(TbAccountField.ACCOUNT_STATUS.NORMAL);
            int affectedRowCount = 0;
            try {
                affectedRowCount = tbAccountService.update(activeAccount);
                //当且仅当成功修改到数据库账号信息，才算完成邮箱验证
                if(affectedRowCount == 1) {
                    tbAccount.setAccountStatus(TbAccountField.ACCOUNT_STATUS.NORMAL);
                    request.getSession().setAttribute(MySessionAttributes.TB_ACCOUNT, tbAccount);
                    logger.error("账号："+ tbAccount.getAccountId()
                            +"，用户名：" + tbAccount.getUsername()
                            +"，邮箱：" + tbAccount.getEmail() + "，已激活！");
                    apiResult = EmailVerificationApiResult.SUCCEED;
                }
            }
            catch (Exception e){
                apiResult = EmailVerificationApiResult.OTHER_FAILED;
                logger.error("激活 "+ tbAccount.getAccountId() +" 账号过程中出现未知错误！error="+ e);
            }
        }
        return apiResult;
    }

    public EmailVerificationApiResult checkCode(String email, String code){
        //预先设置结果为 验证码无效
        EmailVerificationApiResult apiResult = EmailVerificationApiResult.FAILED_INVALID;
        //优先检查长度问题
        if( code == null || code.length() != myConst.getEmailVerificationCodeLength() )
            return apiResult;
        //从内存里高速获取 发邮箱验证码 的历史
        EmailVerificationLogWithLock logWithLock = emailVerificationMap.get(email);
        //邮件都没发过，肯定无效 & 先看看验证码是否存在
        if( logWithLock == null || ! logWithLock.emailVerificationCodeSet.contains(code) )
            return apiResult;
        //高并发，获取锁，保证一致性读
        logWithLock.readLock.lock();
        ArrayList<EmailVerificationLogItem> items = logWithLock.items;
        //从最后一条数据（最新的）开始遍历
        EmailVerificationLogItem item;
        Date currDate = new Date();
        long codeVaildTimeInMilliseconds = myConst.getEmailVerificationCodeVaildTimeInMinutes() * 60L * 1000L;
        int i = items.size()-1;
        for (int count=0; count < myConst.getSentEmailMaxFrequencyPeerDay(); count++) {
            item = items.get(i);
            //对比间隔时间是否超过有效时间
            if (currDate.getTime() - item.sentDate.getTime() < codeVaildTimeInMilliseconds) {
                if( item.emailVerificationCode.equals(code) ){
                    //验证码匹配，验证有效
                    apiResult = EmailVerificationApiResult.SUCCEED;
                    break;
                }
            }
            if( i > 0 )
                i--;
            else
                break;
        }
        //有始有终，解锁
        logWithLock.readLock.unlock();
        return apiResult;
    }





    /**
     * 通过email查询数据
     *
     * @param email 电子邮箱
     * @return 实例对象
     */
    public EmailVerification queryByEmail(String email) {
        return this.emailVerificationDao.queryByEmail(email);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    public List<EmailVerification> queryAllByLimit(int offset, int limit) {
        return this.emailVerificationDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param emailVerification 实例对象
     * @return 实例对象
     */
    public EmailVerification insert(EmailVerification emailVerification) {
        this.emailVerificationDao.insert(emailVerification);
        return emailVerification;
    }

    /**
     * 修改数据
     *
     * @param emailVerification 实例对象
     * @return 实例对象
     */
    public EmailVerification update(EmailVerification emailVerification) {
        this.emailVerificationDao.update(emailVerification);
        return this.queryByEmail(emailVerification.getEmail());
    }

    /**
     * 通过email删除数据
     *
     * @param email 电子邮箱
     * @return 影响行数
     */
    public int deleteByEmail(String email) {
        return this.emailVerificationDao.deleteByEmail(email);
    }

}
