package pers.xfl.jsp_netdisk.model.service;

import com.alibaba.fastjson.JSONObject;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import pers.xfl.jsp_netdisk.model.appconst.LoginApiResult;
import pers.xfl.jsp_netdisk.model.appconst.LogoutApiResult;
import pers.xfl.jsp_netdisk.model.appconst.MyConst;
import pers.xfl.jsp_netdisk.model.appconst.RegisterApiResult;
import pers.xfl.jsp_netdisk.model.dao.AccountSessionDao;
import pers.xfl.jsp_netdisk.model.pojo.database.AccountSession;
import pers.xfl.jsp_netdisk.model.pojo.result.RegisterResult;
import pers.xfl.jsp_netdisk.model.pojo.database.TbAccount;
import pers.xfl.jsp_netdisk.model.dao.TbAccountDao;
import pers.xfl.jsp_netdisk.model.utils.MyStrIsOK;
import org.springframework.stereotype.Service;
import pers.xfl.jsp_netdisk.model.utils.check.RegisterParamChecker;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;


/**
 * (TbAccount)表服务实现类
 *
 * @author makejava
 * @since 2021-04-19 16:00:51
 */
@Service("tbAccountService")
public class TbAccountService {

    @Autowired
    private TbAccountDao tbAccountDao;

    @Autowired
    private AccountSessionDao accountSessionDao;


    /**
     * 检查当前会话是否存在有效登录
     *
     * @param request 客户端请求
     * @return 是否已登录
     */
    public boolean checkIsLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String accountIdStr = (String) session.getAttribute("accountId");
        return checkIsLoggedIn(accountIdStr);
    }

    /**
     * 检查当前会话是否存在有效登录
     *
     * @param accountIdStr 字符串形式的账号ID
     * @return 是否已登录
     */
    public boolean checkIsLoggedIn(String accountIdStr) {
        boolean result = false;
        //检查是否存在有效的账号ID
        if (MyStrIsOK.isNotEmpty(accountIdStr)) {
            long accountId = Long.parseLong(accountIdStr);
            AccountSession accountSession = this.accountSessionDao.queryByAccountId(accountId);
            //可能存在账号被突然冻结、管理员强行注销登录状态的情况
            if (accountSession != null) {
                result = true;
            }
        }
        return result;
    }

    /**
     * 登录，验证用户名和密码是否正确。如果通过验证，将设置accountId到tbAccount对象里面
     *
     * @param tbAccount 包含用户名的TbAccount对象，方便回传账号ID
     * @param password  来自客户端发送来的密码SHA512 HEX值
     * @param sessionId 会话ID
     * @return 是否通过验证
     */
    public LoginApiResult login(TbAccount tbAccount, String password, String sessionId) {
        LoginApiResult loginApiResult = LoginApiResult.FAILED;
        String username = tbAccount.getUsername();
        //检查两者 是否 非空且合法
        if ( (!MyStrIsOK.isEmpty(username)) &&
                (!MyStrIsOK.isContainAllowedSpecialCharacter(username)) &&
                MyStrIsOK.isLetterDigitOnly(password) &&
                username.length() <= MyConst.tbAccountUsernameMaxLength &&
                password.length() == MyConst.sha512HexStrLength) {
            //从数据库拉取验证所必须的信息
            TbAccount tbAccountInDb = queryValidationInformationByUsername(username);
            //如果用户名不存在，则立即返回
            if (tbAccountInDb == null)
                return loginApiResult;
            //默认前端已对密码文本已完成SHA512哈希值计算。这行补全完整的单向加密。
            byte[] passwordHashFromRequest = DigestUtils.md5(password + tbAccountInDb.getPasswordSalt());
            //对比密码哈希值是否一致，使用 时间定长 的比较方法，防止试探性攻击。
            byte[] passwordHashFromDatabase = new byte[1];
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
                System.out.println("数据库密码格式错误，从16进制字符 转码 到二进制失败！");
            }
            if (passwordCorrect) {
                AccountSession accountSession = new AccountSession();
                accountSession.setSessionId(sessionId);
                accountSession.setAccountId(tbAccountInDb.getAccountId());
                int affectedRowCount = 0;
                try {
                    affectedRowCount = this.accountSessionDao.insert(accountSession);
                    loginApiResult = LoginApiResult.SUCCEED;
                    //回传账号ID
                    tbAccount.setAccountId(tbAccountInDb.getAccountId());
                } catch (DataAccessException e) {
                    Throwable cause = e.getCause();
                    if (cause instanceof MySQLIntegrityConstraintViolationException) {
                        loginApiResult = LoginApiResult.DUPLICATE_KEY;
                    } else {
                        loginApiResult = LoginApiResult.OTHER_FAILED;
                    }
                }
            }
        }
        return loginApiResult;
    }

    /**
     * 通过判断用户请求来智能注销登录
     *
     * @param accountIdStr 字符串形式的账号ID
     * @return 是否成功
     */
    public LogoutApiResult logout(String accountIdStr) {
        LogoutApiResult logoutApiResult = LogoutApiResult.OTHER_FAILED;
        //检查是否存在有效的账号ID
        if (MyStrIsOK.isNotEmpty(accountIdStr)) {
            long accountId = Long.parseLong(accountIdStr);
            AccountSession accountSession = this.accountSessionDao.queryByAccountId(accountId);
            if(accountSession == null){
                logoutApiResult = LogoutApiResult.SESSION_EXPIRE;
            }
            else{
                if (this.logoutByAccountId(accountId)) {
                    logoutApiResult = LogoutApiResult.SUCCEED;
                }
                else {
                    logoutApiResult = LogoutApiResult.FAILED;
                }
            }
        }
        else {
            logoutApiResult = LogoutApiResult.NO_LOGIN;
        }
        return logoutApiResult;
    }

    /**
     * 注册账号
     * @param registerJson
     * @return
     */
    public RegisterResult register(JSONObject registerJson){
        RegisterResult registerResult = new RegisterResult();
        RegisterApiResult registerApiResult = RegisterApiResult.OTHER_FAILED;
        String username;
        String passwordStr;
        String email;
        String gender;
        //获取用户名字段
        username = (String) registerJson.get("username");
        //检查用户名数据是否合法
        if(RegisterParamChecker.isUsernameUnderLegal(username)){
            //判断用户名是否被注册了
            if( userQueryByUsername(username) == null){//用户名未被注册
                //获取性别字段
                gender = (String) registerJson.get("gender");
                //检查性别数据是否合法
                if(RegisterParamChecker.isGenderUnderLegal(gender)){
                    //获取密码字段
                    passwordStr = (String) registerJson.get("password");
                    //检查密码复杂度是否符合要求
                    if(RegisterParamChecker.isPasswordComplexityEnough(passwordStr)){
                        //获取电子邮箱字段
                        email = (String) registerJson.get("email");
                        if(RegisterParamChecker.isEmailUnderLegal(email)){
                            if(email.equals("")){//如果未填注册邮箱

                            }
                            else{
                                //检查邮箱是否被注册
                                if(this.tbAccountDao.userQueryByEmail(email) == null){//未注册
                                    System.out.println("可以发邮件验证");
                                }
                                else {
                                    registerApiResult = RegisterApiResult.FAILED_EMAIL_EXIST;
                                }
                            }

                        }
                        else {
                            registerApiResult = RegisterApiResult.ILLEGAL_EMAIL;
                        }
                    }
                    else {
                        registerApiResult = RegisterApiResult.ILLEGAL_PASSWORD;
                    }
                }
                else{
                    registerApiResult = RegisterApiResult.ILLEGAL_GENDER;
                }
            }
            else {
                registerApiResult = RegisterApiResult.FAILED_USERNAME_EXIST;
            }
        }
        else {
            registerApiResult = RegisterApiResult.ILLEGAL_USERNAME;
        }






        return registerResult;
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
    public List<TbAccount> queryAllByLimit(int offset, int limit) {
        return this.tbAccountDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param tbAccount 实例对象
     * @return 实例对象
     */
    public TbAccount insert(TbAccount tbAccount) {
        this.tbAccountDao.insert(tbAccount);
        return tbAccount;
    }

    /**
     * 修改数据
     *
     * @param tbAccount 实例对象
     * @return 实例对象
     */
    public TbAccount update(TbAccount tbAccount) {
        this.tbAccountDao.update(tbAccount);
        return this.queryById(tbAccount.getAccountId());
    }

    /**
     * 通过主键删除数据
     *
     * @param accountId 主键
     * @return 是否成功
     */
    public boolean deleteById(Long accountId) {
        return this.tbAccountDao.deleteById(accountId) > 0;
    }
}
