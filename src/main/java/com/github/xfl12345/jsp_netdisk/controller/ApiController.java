package com.github.xfl12345.jsp_netdisk.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.xfl12345.jsp_netdisk.appconst.api.result.*;
import com.github.xfl12345.jsp_netdisk.appconst.field.LoginRequestField;
import com.github.xfl12345.jsp_netdisk.appconst.field.MySessionAttributes;
import com.github.xfl12345.jsp_netdisk.appconst.field.TbAccountField;
import com.github.xfl12345.jsp_netdisk.model.dao.TbDirectoryDao;
import com.github.xfl12345.jsp_netdisk.model.dao.TbPermissionDao;
import com.github.xfl12345.jsp_netdisk.model.pojo.database.TbAccount;
import com.github.xfl12345.jsp_netdisk.model.pojo.database.TbPermission;
import com.github.xfl12345.jsp_netdisk.model.pojo.result.BaseResponseObject;
import com.github.xfl12345.jsp_netdisk.model.pojo.result.RegisterResult;
import com.github.xfl12345.jsp_netdisk.model.service.EmailVerificationService;
import com.github.xfl12345.jsp_netdisk.model.service.TbAccountService;
import com.github.xfl12345.jsp_netdisk.model.service.TbPermissionService;
import com.github.xfl12345.jsp_netdisk.model.utils.JsonRequestUtils;
import com.github.xfl12345.jsp_netdisk.model.utils.MyBatisSqlUtils;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

import static com.github.xfl12345.jsp_netdisk.StaticSpringApp.appInfo;

@Controller
public class ApiController {

    /**
     * version值即 API 版本号，ApiController 自身的属性，
     * 粒度细到一个Controller的版本号，方便部分API迭代更新
     */
    public static final int version = 1;

    private final Logger logger= LoggerFactory.getLogger(ApiController.class);

    @Autowired
    private TbAccountService tbAccountService;

    @Autowired
    private EmailVerificationService emailVerificationService;

    @Autowired
    private TbPermissionService tbPermissionService;

    @Autowired
    private TbDirectoryDao tbDirectoryDao;

    /**
     * 对POST 登录请求，做出API响应
     * @param request http POST请求
     * @param response http响应
     */
    @RequestMapping(value = "user/login", method = RequestMethod.POST)
    public void loginAction(HttpServletRequest request, HttpServletResponse response) {
        //新建 JSON对象，后续再格式化为 JSON字符串 作为响应数据
        BaseResponseObject responseObject = new BaseResponseObject();
        responseObject.version = String.valueOf(version);
        responseObject.flag = false;
        String msg = "";
        //是否已登录？
        if(tbAccountService.checkIsLoggedIn(request.getSession())){//已登录
            msg = "已登录一个账号！请先注销当前账号！";
        }
        else {//格式化请求数据为JSON对象
            JSONObject requesetJsonObject = null;
            try {
                requesetJsonObject = JsonRequestUtils.getJsonObject(request);
//                logger(requesetJsonObject.toJSONString());
            }
            catch (Exception e){
                responseObject.msg = "请求格式错误！请使用JSON格式！";
                JsonRequestUtils.responseObjectAsJsonStr(response, responseObject);
                return;
            }
            //获取session对象
            HttpSession session = request.getSession();
            String username = (String) requesetJsonObject.get(LoginRequestField.USERNAME);
            String password =  (String) requesetJsonObject.get(LoginRequestField.PASSWORD);
            //验证
            LoginApiResult loginApiResult = tbAccountService.login(session, username, password);
            //成功唯一绑定 会话ID 和 账号ID
            if(loginApiResult.equals(LoginApiResult.SUCCEED)) {
                responseObject.flag = true;
            }
            msg = loginApiResult.getName();
        }
        responseObject.msg = msg;
        JsonRequestUtils.responseObjectAsJsonStr(response, responseObject);
    }

    /**
     * 对POST 注销登录请求，做出API响应
     * @param request http POST请求
     */
    @RequestMapping(value = "user/logout", method = RequestMethod.POST)
    public void logoutAction(HttpServletRequest request, HttpServletResponse response) {
        BaseResponseObject responseObject = new BaseResponseObject();
        responseObject.version = String.valueOf(version);
        responseObject.flag = false;
        String msg = "";
        String htmlCode = "";
//        boolean isAcceptHtmlCode = false;

        //格式化请求数据为JSON对象
        JSONObject requesetJsonObject = null;
        try {
            requesetJsonObject = JsonRequestUtils.getJsonObject(request);
//            Object obj = requesetJsonObject.getBoolean("isAcceptHtmlCode");
//            if(obj != null){
//                isAcceptHtmlCode = (boolean) obj;
//            }
        }
        catch (Exception e){
            msg = "请求格式错误！请使用JSON格式！";
            responseObject.msg = msg;
            JsonRequestUtils.responseObjectAsJsonStr(response, responseObject);
            return;
        }
        LogoutApiResult logoutApiResult = tbAccountService.logout(request.getSession());
        if( logoutApiResult == LogoutApiResult.SUCCEED){
            responseObject.flag = true;
//            if(isAcceptHtmlCode){
//                Element jump2index = new Element("a");
//                jump2index.attr("href","index");
//                jump2index.text("返回首页");
//                htmlCode += jump2index.toString();
//
//                Element jump2login = new Element("a");
//                jump2login.attr("href","login");
//                jump2login.text("再次登录");
//                htmlCode += jump2login.toString();
//            }

        }
        msg = logoutApiResult.getName();
        responseObject.msg = msg;
//        responseJsonObject.put("htmlCode",htmlCode);
        JsonRequestUtils.responseObjectAsJsonStr(response, responseObject);
    }

    /**
     * 对POST 注销登录请求，做出API响应
     * @param request http GET请求
     */
    @RequestMapping(value = "user/register", method = RequestMethod.POST)
    public void registerAction(HttpServletRequest request, HttpServletResponse response) {
        BaseResponseObject responseObject = new BaseResponseObject();
        responseObject.version = String.valueOf(version);
        responseObject.flag = false;
        String msg = "";
        //格式化请求数据为JSON对象
        JSONObject requesetJsonObject = null;
        try {
            requesetJsonObject = JsonRequestUtils.getJsonObject(request);
        }
        catch (Exception e){
            responseObject.msg = "请求格式错误！请使用JSON格式！";
            JsonRequestUtils.responseObjectAsJsonStr(response, responseObject);
            return;
        }
        RegisterResult registerResult = tbAccountService.register(request.getSession(), requesetJsonObject, 1);
        if(registerResult.registerApiResult.equals(RegisterApiResult.SUCCEED)){
            responseObject.flag = true;
            String accountIdStr = Long.toString(registerResult.tbAccount.getAccountId());
            Element accountIdDisplayElement = new Element("b");
            accountIdDisplayElement.attr("id", "accountIdStr");
            accountIdDisplayElement.text(accountIdStr);
            msg = "注册成功！您的账号是&nbsp;"+ accountIdDisplayElement
                    + "&nbsp;<br>欢迎使用&nbsp;" + appInfo.getAppName()
                    + "&nbsp;！";
            logger.info("新注册用户ID=" + registerResult.tbAccount.getAccountId());
        }
        else{
            msg = "注册失败！" + registerResult.registerApiResult.getName();
        }
        responseObject.msg = msg;
        JsonRequestUtils.responseObjectAsJsonStr(response, responseObject);
    }

    /**
     * 对POST 发送邮箱验证码请求，做出API响应
     * @param request http POST请求
     */
    @RequestMapping(value = "user/emailVerificationCodeSentEmail", method = RequestMethod.POST)
    public void emailVerificationCodeGet(HttpServletRequest request, HttpServletResponse response){
        BaseResponseObject responseObject = new BaseResponseObject();
        responseObject.version = String.valueOf(version);
        responseObject.flag = false;
        //格式化请求数据为JSON对象
        JSONObject requesetJsonObject = null;
        try {
            requesetJsonObject = JsonRequestUtils.getJsonObject(request);
        }
        catch (Exception e){
            responseObject.msg = "请求格式错误！请使用JSON格式！";
            JsonRequestUtils.responseObjectAsJsonStr(response, responseObject);
            return;
        }
        SentEmailApiResult apiResult = emailVerificationService.sendEmail(request.getSession());
        if(apiResult.equals(SentEmailApiResult.SUCCEED)){
            responseObject.flag = true;
        }
        responseObject.msg =apiResult.getName();
        JsonRequestUtils.responseObjectAsJsonStr(response, responseObject);
    }

    /**
     * 对POST 提交邮箱验证码 请求，做出API响应
     * @param request http POST请求
     */
    @RequestMapping(value = "user/emailVerificationCode", method = RequestMethod.POST)
    public void emailVerificationCodeAction(HttpServletRequest request, HttpServletResponse response){
        BaseResponseObject responseObject = new BaseResponseObject();
        responseObject.version = String.valueOf(version);
        responseObject.flag = false;
        TbAccount tbAccount = (TbAccount) request.getSession().getAttribute(MySessionAttributes.TB_ACCOUNT);
        if(tbAccount.getAccountStatus().equals(TbAccountField.ACCOUNT_STATUS.NORMAL)){
            responseObject.flag = true;
            responseObject.msg = "邮箱已激活";
        }
        else{
            EmailVerificationApiResult apiResult = emailVerificationService.activeEmail(request);
            if( apiResult.equals(EmailVerificationApiResult.SUCCEED)){
                responseObject.flag = true;
            }
            responseObject.msg = apiResult.getName();
        }
        JsonRequestUtils.responseObjectAsJsonStr(response, responseObject);
    }







    /**
     * 对POST debug请求，做出API响应
     * @param request http POST请求
     */
    @RequestMapping(value = "debug", method = RequestMethod.POST)
    public void debugAction(HttpServletRequest request, HttpServletResponse response){
        BaseResponseObject responseObject = new BaseResponseObject();
        responseObject.version = String.valueOf(version);
        responseObject.flag = true;
        String msg = DebugApiResult.SUCCEED.getName();
        //格式化请求数据为JSON对象
        JSONObject requesetJsonObject = null;
        try {
            requesetJsonObject = JsonRequestUtils.getJsonObject(request);
        }
        catch (Exception e){
            msg = "请求格式错误！请使用JSON格式！";
            responseObject.msg = msg;
            JsonRequestUtils.responseObjectAsJsonStr(response, responseObject);
            return;
        }

        try{
            TbAccount tbAccountResult = tbAccountService.queryById(66666L);
            if(tbAccountResult == null){
                logger.info("MyBatis查不到数据的时候，如果设定返回不是集合而是bean实体类，则返回的确实是null");
            }
            TbAccount tbAccount = new TbAccount();
            tbAccount.setAccountId(6666666L);
            List<TbAccount> tbAccountList = tbAccountService.queryAllByLimit(tbAccount, 0, 100);
            logger.info("tbAccountList.size()="+tbAccountList.size());
            TbPermission tbPermission = new TbPermission();
            tbPermission.setPermissionId(1L);
            logger.info(MyBatisSqlUtils.getSql("queryAll", tbPermission, TbPermissionDao.class));
            int affectedRowCount = tbPermissionService.insert(tbPermission);
//        TbDirectory tbDirectoryInsert = new TbDirectory();
//        tbDirectoryInsert.setParentDirectoryId(0L);
//        tbDirectoryInsert.setAccountId(1L);
//        tbDirectoryInsert.setDirectoryName("just4test");
//        logger.info("affected row count = " + tbDirectoryDao.insert(tbDirectoryInsert));
//        TbDirectory tbDirectory = new TbDirectory();
//        tbDirectory.setAccountId(1L);
//        tbDirectory.setParentDirectoryId(0L);
//        logger.info("count="+tbDirectoryDao.countAll(tbDirectory));
        }
        catch (Exception e){
            msg = DebugApiResult.OTHER_FAILED.getName();
            responseObject.flag = false;
        }
        responseObject.msg = msg;
        JsonRequestUtils.responseObjectAsJsonStr(response, responseObject);
    }


}
