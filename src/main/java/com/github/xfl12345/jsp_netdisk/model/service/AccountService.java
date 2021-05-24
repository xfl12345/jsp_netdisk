package com.github.xfl12345.jsp_netdisk.model.service;

import com.alibaba.fastjson.JSONObject;
import com.github.xfl12345.jsp_netdisk.StaticSpringApp;
import com.github.xfl12345.jsp_netdisk.appconst.api.result.JsonApiResult;
import com.github.xfl12345.jsp_netdisk.appconst.api.result.*;
import com.github.xfl12345.jsp_netdisk.appconst.MyConst;
import com.github.xfl12345.jsp_netdisk.appconst.api.request.JsonApiRequestField;
import com.github.xfl12345.jsp_netdisk.appconst.api.request.RegisterRequestField;
import com.github.xfl12345.jsp_netdisk.appconst.field.AccountOperationField;
import com.github.xfl12345.jsp_netdisk.appconst.field.MySessionAttributes;
import com.github.xfl12345.jsp_netdisk.appconst.field.TbAccountField;
import com.github.xfl12345.jsp_netdisk.appconst.field.TbDirectoryField;
import com.github.xfl12345.jsp_netdisk.model.dao.AccountSessionDao;
import com.github.xfl12345.jsp_netdisk.model.dao.TbAccountDao;
import com.github.xfl12345.jsp_netdisk.model.pojo.api.request.BaseRequestObject;
import com.github.xfl12345.jsp_netdisk.model.pojo.api.request.payload.LoginRequestData;
import com.github.xfl12345.jsp_netdisk.model.pojo.database.AccountSession;
import com.github.xfl12345.jsp_netdisk.model.pojo.database.TbAccount;
import com.github.xfl12345.jsp_netdisk.model.pojo.api.response.JsonCommonApiResponseObject;
import com.github.xfl12345.jsp_netdisk.model.pojo.result.DirectoryView;
import com.github.xfl12345.jsp_netdisk.model.pojo.result.RegisterResult;
import com.github.xfl12345.jsp_netdisk.model.service.api.JsonCommonApiCall;
import com.github.xfl12345.jsp_netdisk.model.utility.MyStrIsOK;
import com.github.xfl12345.jsp_netdisk.model.utility.check.RegisterFieldChecker;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

import static com.github.xfl12345.jsp_netdisk.StaticSpringApp.myConst;


/**
 * (TbAccount)表服务实现类
 *
 * @author makejava
 * @since 2021-04-19 16:00:51
 */
@Service("tbAccountService")
public class AccountService implements JsonCommonApiCall {

    private final Logger logger = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    private TbAccountDao tbAccountDao;

    @Autowired
    private AccountSessionDao accountSessionDao;

    @Autowired
    private TbPermissionService tbPermissionService;

    @Autowired
    private EmailVerificationService emailVerificationService;

    @Autowired
    private FileService fileService;

    public static final String jsonApiVersion = "1";


    /**
     * 检查完JSON数据结构是否合法之后，调用具体的业务函数
     */
    @Override
    public JsonCommonApiResponseObject getResult(HttpServletRequest request, JSONObject object) {
        JsonCommonApiResponseObject responseObject = new JsonCommonApiResponseObject(jsonApiVersion);
        BaseRequestObject baseRequestObject = object.toJavaObject(BaseRequestObject.class);
        try {
            //兼容旧API版本的请求
            switch (baseRequestObject.version) {
                //当前版本（最新版本）
                case jsonApiVersion:
                    //选择操作
                    switch (baseRequestObject.operation) {
                        //登录
                        case AccountOperationField.login:
                            LoginRequestData loginRequestData = baseRequestObject.data.toJavaObject(LoginRequestData.class);
                            LoginApiResult loginApiResult = login(request.getSession(), loginRequestData.username, loginRequestData.password);
                            responseObject.success = loginApiResult.equals(LoginApiResult.SUCCEED);
                            responseObject.code = loginApiResult.getNum();
                            responseObject.message = loginApiResult.getName();
                            responseObject.version = jsonApiVersion;
                            break;
                        case AccountOperationField.logout:
                            LogoutApiResult logoutApiResult = logout(request.getSession());
                            responseObject.success = logoutApiResult.equals(LogoutApiResult.SUCCEED);
                            responseObject.code = logoutApiResult.getNum();
                            responseObject.message = logoutApiResult.getName();
                            responseObject.version = jsonApiVersion;
                            break;
                        default:
                            responseObject.setApiResult(JsonApiResult.FAILED_INVALID);
                    }
                    break;
                default:
                    responseObject.setApiResult(JsonApiResult.FAILED_INVALID);
                    break;
            }
        } catch (Exception e) {
            logger.error(e.toString());
            responseObject.setApiResult(JsonApiResult.FAILED_INVALID);
        }
        return responseObject;
    }


    /**
     * 检查当前会话是否存在有效登录
     *
     * @param request 客户端请求
     * @return 是否已登录
     */
    public boolean checkIsLoggedIn(HttpServletRequest request) {
        return checkIsLoggedIn(request.getSession());
    }

    /**
     * 检查当前会话是否存在有效登录
     *
     * @param session 客户端会话
     * @return 是否已登录
     */
    public boolean checkIsLoggedIn(HttpSession session) {
        TbAccount tbAccount = (TbAccount) session.getAttribute(MySessionAttributes.TB_ACCOUNT);
        if(tbAccount != null){
            AccountSession accountSession = accountSessionDao.queryByAccountId(tbAccount.getAccountId());
            if(accountSession == null || ! accountSession.getSessionId().equals(session.getId())){
                session.invalidate();
            }
            else {
                return true;
            }
        }
        return false;
    }


    /**
     * 登录，验证用户名和密码是否正确。如果通过验证，将设置accountId到tbAccount对象里面
     *
     * @param session      当前会话，用于获取会话ID，以及设置session变量
     * @param username     用户名
     * @param passwordHash 来自客户端发送来的密码SHA512 HEX值
     * @return 是否通过验证
     */
    public LoginApiResult login(HttpSession session, String username, String passwordHash) {
        LoginApiResult loginApiResult = LoginApiResult.FAILED;
        if (checkIsLoggedIn(session)){
            return LoginApiResult.FAILED_ALREADY_LOGINED;
        }
        //检查两者 是否 非空且合法
        if ( RegisterFieldChecker.isUsernameUnderLegal(username) &&
                MyStrIsOK.isLetterDigitOnly(passwordHash) &&
                username.length() <= TbAccountField.USERNAME_MAX_LENGTH &&
                passwordHash.length() == MyConst.SHA_512_HEX_STR_LENGTH) {
            //从数据库拉取用户信息
            TbAccount tbAccountInDb = queryByUsername(username);
            //如果用户名不存在，则立即返回
            if (tbAccountInDb == null)
                return loginApiResult;
            //默认前端已对密码文本已完成SHA512哈希值计算。这行补全完整的单向加密。这样，哪怕被拖库，也可以保证密码安全。
            byte[] passwordHashFromRequest = DigestUtils.md5(passwordHash + tbAccountInDb.getPasswordSalt());
            //对比密码哈希值是否一致，使用 时间定长 的比较方法，防止试探性攻击。
            byte[] passwordHashFromDatabase;
            boolean passwordCorrect = false;
            try {
                passwordHashFromDatabase = Hex.decodeHex(tbAccountInDb.getPasswordHash());
                passwordCorrect = true;
                for (int i = 0; i < passwordHashFromDatabase.length; i++) {
                    if ((passwordHashFromRequest[i] ^ passwordHashFromDatabase[i]) != 0) {
                        passwordCorrect = false;
                    }
                }
            } catch (DecoderException e) {
                e.printStackTrace();
                logger.error("数据库密码格式错误，从16进制字符 转码 到二进制失败！");
            }
            if (passwordCorrect) {//密码正确，执行插入数据库操作。
                loginApiResult = login(session, tbAccountInDb);
            }
        }
        return loginApiResult;
    }

    public LoginApiResult login(HttpSession session, TbAccount tbAccountInDb) {
        LoginApiResult loginApiResult = LoginApiResult.OTHER_FAILED;
        AccountSession accountSession = new AccountSession();
        accountSession.setSessionId(session.getId());
        accountSession.setAccountId(tbAccountInDb.getAccountId());
        int affectedRowCount = 0;
        try {
            affectedRowCount = accountSessionDao.insert(accountSession);
            session.setAttribute(MySessionAttributes.TB_ACCOUNT, tbAccountInDb);
            DirectoryView currDirView = fileService.getDirectoryView(tbAccountInDb.getRootDirectoryId());
            session.setAttribute(MySessionAttributes.CURRENT_WORK_DIRECTOR, currDirView);
            loginApiResult = LoginApiResult.SUCCEED;
        } catch (DataAccessException e) {
            Throwable cause = e.getCause();
            if (cause instanceof MySQLIntegrityConstraintViolationException) {
                loginApiResult = LoginApiResult.DUPLICATE_KEY;
            }
            logger.warn(e.toString());
        }
        return loginApiResult;
    }

    /**
     * 通过判断用户请求来智能注销登录
     *
     * @param session 客户端会话
     * @return 是否成功
     */
    public LogoutApiResult logout(HttpSession session) {
        LogoutApiResult logoutApiResult = LogoutApiResult.OTHER_FAILED;
        TbAccount tbAccount = (TbAccount) session.getAttribute(MySessionAttributes.TB_ACCOUNT);
        //检查是否存在有效的账号ID
        if (tbAccount != null) {
            long accountId = tbAccount.getAccountId();
            AccountSession accountSession = this.accountSessionDao.queryByAccountId(accountId);
            if (accountSession == null) {
                logoutApiResult = LogoutApiResult.SESSION_EXPIRE;
            } else {
                if (logoutByAccountId(accountId)) {
                    session.removeAttribute(MySessionAttributes.TB_ACCOUNT);
                    logoutApiResult = LogoutApiResult.SUCCEED;
                } else {
                    logoutApiResult = LogoutApiResult.FAILED;
                }
            }
        } else {
            logoutApiResult = LogoutApiResult.NO_LOGIN;
        }
        return logoutApiResult;
    }

    /**
     * 注册账号
     *
     * @param registerJson 客户端请求发来的JSON对象
     * @return 返回注册结果
     */
    public RegisterResult register(HttpSession session, JSONObject registerJson, int callCount) {
        RegisterResult registerResult = new RegisterResult();
        //优先检查字段是否合法
        RegisterApiResult registerApiResult = RegisterFieldChecker.autoCheck(registerJson);
        registerResult.tbAccount = null;
        if (registerApiResult.equals(RegisterApiResult.SUCCEED)) {
            String username = (String) registerJson.get(RegisterRequestField.USERNAME);//获取用户名字段
            String passwordStr = (String) registerJson.get(RegisterRequestField.PASSWORD);//获取密码字段
            String email = (String) registerJson.get(RegisterRequestField.EMAIL);//获取邮箱字段
            String gender = (String) registerJson.get(RegisterRequestField.GENDER);//获取性别字段
            //检查用户名是否被注册
            if (userQueryByUsername(username) == null) {//用户名未被注册
                //检查邮箱是否被注册
                if (userQueryByEmail(email) == null) {//未注册
                    Date date = new Date();
                    String passwordSalt = generatePasswordSalt();
                    String passwordHash = generatePasswordHash(passwordStr, passwordSalt);
                    String currDate = StaticSpringApp.getSimpleDateFormat().format(date);
                    String currMillisecond = StaticSpringApp.getMillisecondFormatter().format(date);

                    TbAccount tbAccount = new TbAccount();
                    tbAccount.setUsername(username);
                    tbAccount.setPasswordHash(passwordHash);
                    tbAccount.setPasswordSalt(passwordSalt);
                    tbAccount.setPermissionId(tbPermissionService.gePermissionIdOfEmailNotActivatedAccount());
                    tbAccount.setRegisterTime(currDate);
                    tbAccount.setRegisterTimeInMs(Integer.valueOf(currMillisecond));
                    tbAccount.setRootDirectoryId(TbDirectoryField.DIRECTORY_ID.blackhouse.id);
                    tbAccount.setEmail(email);
                    tbAccount.setGender(gender);
                    tbAccount.setAccountStatus(TbAccountField.ACCOUNT_STATUS.EMAIL_NOT_ACTIVATED);
                    SentEmailApiResult sentEmailApiResult = emailVerificationService.isUnderSentEmailLimitation(tbAccount);
                    if (sentEmailApiResult.equals(SentEmailApiResult.SUCCEED)) {
                        int affectedRowCount = 0;
                        TbAccount tbAccountInDb;
                        try {//注册并获取账号ID
                            affectedRowCount = insert(tbAccount);
                            tbAccountInDb = userQueryByEmail(tbAccount.getEmail());
                            registerResult.tbAccount = tbAccountInDb;
                            login(session, tbAccountInDb);
                            registerApiResult = RegisterApiResult.SUCCEED;
                            emailVerificationService.sendEmail(tbAccount);
                        } catch (DataAccessException e) {
                            Throwable cause = e.getCause();
                            if (cause instanceof MySQLIntegrityConstraintViolationException) {
                                if (callCount == 1) {
                                    // 对瞬时重复注册情况的处理（有且只有 1 端注册有效）
                                    RegisterResult registerResult2 = register(session, registerJson, callCount + 1);
                                    registerApiResult = registerResult2.registerApiResult;
                                } else {
                                    logger.error("注册功能 发生了严重的错误！已终止无限递归！请立即排查原因！！error=" + e);
                                    registerApiResult = RegisterApiResult.OTHER_FAILED;
                                }
                            } else {
                                logger.error("注册功能 发生了未知错误！请立即排查原因！！error=" + e);
                                registerApiResult = RegisterApiResult.OTHER_FAILED;
                            }
                        }
                    } else {
                        if (sentEmailApiResult.equals(SentEmailApiResult.FAILED_FREQUENCY_MAX))
                            registerApiResult = RegisterApiResult.FAILED_FREQUENCY_MAX;
                        else if (sentEmailApiResult.equals(SentEmailApiResult.FAILED_TODAY_MAX))
                            registerApiResult = RegisterApiResult.FAILED_TODAY_MAX;
                    }
                } else {
                    registerApiResult = RegisterApiResult.FAILED_EMAIL_EXIST;
                }
            } else {
                registerApiResult = RegisterApiResult.FAILED_USERNAME_EXIST;
            }
        }
        registerResult.registerApiResult = registerApiResult;
        return registerResult;
    }

    public String generatePasswordHash(String passwordStr, String salt) {
        return DigestUtils.md5Hex(Hex.encodeHexString(DigestUtils.sha512(passwordStr)) + salt);
    }

    public String generatePasswordSalt() {
        return emailVerificationService.generateEmailVerificationCode(TbAccountField.PASSWORD_SALT_LENGTH);
    }


    /**
     * 通过电子邮箱查询单条数据
     *
     * @param email 电子邮箱
     * @return 实例对象
     */
    public TbAccount userQueryByEmail(String email) {
        return this.tbAccountDao.userQueryByEmail(email);
    }

    /**
     * 按账号ID注销登录
     *
     * @param accountId 账号ID
     * @return 是否成功
     */
    public boolean logoutByAccountId(Long accountId) {
        return this.accountSessionDao.deleteByAccountId(accountId) == 1;
    }

    /**
     * 按会话ID注销登录
     *
     * @param sessionId 会话ID
     * @return 是否成功
     */
    public boolean logoutBySessionId(String sessionId) {
        return this.accountSessionDao.deleteBySessionId(sessionId) == 1;
    }

    /**
     * 通过用户名查询单条数据
     *
     * @param username 用户名
     * @return 实例对象
     */
    public TbAccount queryByUsername(String username) {
        return this.tbAccountDao.queryByUsername(username);
    }

    /**
     * 根据用户名查询允许提供给普通用户的单条数据
     *
     * @param username 用户名
     * @return 实例对象
     */
    TbAccount userQueryByUsername(String username) {
        return this.tbAccountDao.userQueryByUsername(username);
    }

    /**
     * 通过用户名查询登录验证所需要的数据
     *
     * @param username 用户名
     * @return 实例对象
     */
    TbAccount queryValidationInformationByUsername(String username) {
        return this.tbAccountDao.queryValidationInformationByUsername(username);
    }

    /**
     * 通过ID查询单条数据
     *
     * @param accountId 主键
     * @return 实例对象
     */
    public TbAccount queryById(Long accountId) {
        return this.tbAccountDao.queryById(accountId);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    public List<TbAccount> queryAllByLimit(TbAccount tbAccount, int offset, int limit) {
        return this.tbAccountDao.queryAllByLimit(tbAccount, offset, limit);
    }

    /**
     * 新增数据
     *
     * @param tbAccount 实例对象
     * @return 实例对象
     */
    public int insert(TbAccount tbAccount) {
        return this.tbAccountDao.insert(tbAccount);
    }

    /**
     * 根据账号ID，修改账号数据
     * 更新成功后，同步更新当前账号的session对象
     *
     * @param tbAccount 实例对象
     * @return 实例对象
     */
    public synchronized int update(TbAccount tbAccount) {
        int affectedRow = this.tbAccountDao.update(tbAccount);
        TbAccount updatedTbAccount = this.tbAccountDao.queryById(tbAccount.getAccountId());
        AccountSession accountSession = accountSessionDao.queryByAccountId(tbAccount.getAccountId());
        if(accountSession != null){
            HttpSession session = StaticSpringApp.getSessionById(accountSession.getSessionId());
            if(session != null){
                try {
                    session.setAttribute(MySessionAttributes.TB_ACCOUNT, updatedTbAccount);
                }
                catch (Exception e){
                    logger.error("更新账号 "+ tbAccount.getAccountId()
                            + " 完成后，同步更新session对象失败！error=" + e);
                }
            }
        }
        return affectedRow;
    }

    /**
     * 通过主键删除数据
     *
     * @param accountId 主键
     * @return 是否成功
     */
    public boolean deleteById(Long accountId) {
        return this.tbAccountDao.deleteById(accountId) == 1;
    }

}
