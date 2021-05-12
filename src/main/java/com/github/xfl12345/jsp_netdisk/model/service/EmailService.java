package com.github.xfl12345.jsp_netdisk.model.service;

import com.github.xfl12345.jsp_netdisk.model.utils.MyPropertiesUtils;
import com.github.xfl12345.jsp_netdisk.model.utils.MyStrIsOK;
import com.github.xfl12345.jsp_netdisk.model.utils.SimpleMimeMessageBuilder;
import org.apache.ibatis.io.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Service;
import com.github.xfl12345.jsp_netdisk.StaticSpringApp;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;

import static com.github.xfl12345.jsp_netdisk.StaticSpringApp.myConst;

/**
 * 发邮件 的 服务类
 * source code URL=https://blog.csdn.net/xuemengrui12/article/details/78530594
 */
@Service
public class EmailService implements DisposableBean {

    private final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private class EmailDequeItem {
        public MimeMessage message;
        public IEmailSentCallback callback;

        public EmailDequeItem(MimeMessage message,
                              IEmailSentCallback callback) {
            this.message = message;
            this.callback = callback;
        }
    }

//    private class EmailLogin{
//        public String myEmailAccount;
//        public String myEmailPassword;
//    }
//    private EmailLogin emailLogin;

    private final Session session;
    private final Transport transport;
    private final String myEmailAddress;
    private boolean isConnected = false;
    private boolean threadRunningflag = true; // 线程运行的标识，用于控制线程
    private final LinkedBlockingQueue<EmailDequeItem> emailDeque;
    private final String myEmailAccount;
    private final String myEmailPassword;

    public String getMyEmailAddress() {
        return myEmailAddress;
    }

    public EmailService() throws IOException, MessagingException {
        this("email.properties");
    }

    public EmailService(String email_prop_file_relative_path) throws IOException, MessagingException {
        MyPropertiesUtils myPropertiesUtils = StaticSpringApp.getBean(MyPropertiesUtils.class);

        // 1. 读取用于配置连接邮件服务器的参数
        InputStream inputStream = Resources.getResourceAsStream(email_prop_file_relative_path);
        Properties myEmailProperties = new Properties();
        myEmailProperties.load(inputStream);
        myEmailAddress = myPropertiesUtils.popKeyFromProperties(myEmailProperties, "myEmailAddress");
        myEmailAccount = myPropertiesUtils.popKeyFromProperties(myEmailProperties, "myEmailAccount");
        myEmailPassword = myPropertiesUtils.popKeyFromProperties(myEmailProperties, "myEmailPassword");
        boolean sessionSetDebug = Boolean.parseBoolean(myPropertiesUtils.popKeyFromProperties(myEmailProperties, "session.setDebug"));
        // 2. 根据配置创建会话对象, 用于和邮件服务器交互
        session = Session.getInstance(myEmailProperties);
        // 设置debug模式, 可以查看详细的发送 log
        session.setDebug(sessionSetDebug);
        // 创建一条邮件队列，支持高并发处理待发邮件
        emailDeque = new LinkedBlockingQueue<EmailDequeItem>(myConst.getEmailQueueMaxSize());
        // 3. 根据 Session 获取邮件传输对象
        transport = session.getTransport();
        // 独立开辟一条线程，用以处理邮件队列
        new Thread() {
            @Override
            public void run() {
                try {
                    processEmail();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void processEmail() throws InterruptedException {
        while (threadRunningflag) {
            EmailDequeItem item = emailDeque.take();
            if( ! threadRunningflag )
                continue;
            try {
                if( ! transport.isConnected()){
                    // 4. 使用 邮箱账号 和 密码 连接邮件服务器, 这里认证的邮箱必须与 message 中的发件人邮箱一致, 否则报错
                    //
                    //    PS_01: 如果连接服务器失败, 都会在控制台输出相应失败原因的log。
                    //    仔细查看失败原因, 有些邮箱服务器会返回错误码或查看错误类型的链接,
                    //    根据给出的错误类型到对应邮件服务器的帮助网站上查看具体失败原因。
                    //
                    //    PS_02: 连接失败的原因通常为以下几点, 仔细检查代码:
                    //           (1) 邮箱没有开启 SMTP 服务;
                    //           (2) 邮箱密码错误, 例如某些邮箱开启了独立密码;
                    //           (3) 邮箱服务器要求必须要使用 SSL 安全连接;
                    //           (4) 请求过于频繁或其他原因, 被邮件服务器拒绝服务;
                    //           (5) 如果以上几点都确定无误, 到邮件服务器网站查找帮助。
                    transport.connect(myEmailAccount, myEmailPassword);
                    isConnected = true;
                    logger.info("email server connected.");
                }
                if (transport.isConnected()) {
                    // 设置显示的发件时间
                    item.message.setSentDate(new Date());
                    // 保存设置
                    item.message.saveChanges();
                    // 6. 发送邮件, 发到所有的收件地址, message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
                    transport.sendMessage(item.message, item.message.getAllRecipients());
                    // 回调，继续完成发完邮件之后的任务
                    item.callback.afterSent(item.message, true, null);
                } else {
                    logger.error("connecting to email server failed!");
                }
            } catch (MessagingException sendFailedException) {//邮箱地址无效 或 连接异常
                sendFailedException.printStackTrace();
                item.callback.afterSent(item.message, false, sendFailedException.getMessage());
            }

        }
        logger.info("processEmail thread exit.");
    }


    public MimeMessage getMimeMessage() {
        return new MimeMessage(session);
    }

    public SimpleMimeMessageBuilder getSimpleMimeMessageBuilder() {
        // 5. 写一封电子邮件
        return SimpleMimeMessageBuilder.aSession(session);
    }

    public void sendMimeMessage(MimeMessage message, IEmailSentCallback callback) throws InterruptedException {
        emailDeque.put(new EmailDequeItem(message, callback));
        logger.debug("发送邮件给 " + getRecipientsEmailAddress(message)[0] + " 已加入队列！");
    }

    /**
     * 关闭与邮箱服务器的连接
     *
     * @throws MessagingException 抛出 MessagingException 异常
     */
    @Override
    public void destroy() throws MessagingException, InterruptedException {
        threadRunningflag = false;
        emailDeque.put(new EmailDequeItem(null, null));
        boolean flag = transport.isConnected();
        if (flag) {
            // 7. 关闭连接
            transport.close();
            flag = transport.isConnected();
        }
        if (flag) {
            logger.error("disconnect from email server failed!");
        } else {
            isConnected = false;
            logger.info("disconnect from email server succeed.");
        }

    }

    public String[] getRecipientsEmailAddress(MimeMessage message) {
        String[] emailAddresses = new String[0];
        try {
            Address[] addresses = message.getRecipients(Message.RecipientType.TO);
            emailAddresses = new String[addresses.length];
            for (int i = 0; i < addresses.length; i++) {
                emailAddresses[i] = getRecipientEmailAddress(addresses[i]);
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return emailAddresses;
    }

    public String getRecipientEmailAddress(Address address) {
        ArrayList<String> emailAddrList = MyStrIsOK.getEmailFromString(address.toString());
        return emailAddrList.get(emailAddrList.size() - 1);
    }
}
