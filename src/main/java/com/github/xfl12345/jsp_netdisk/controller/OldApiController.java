package com.github.xfl12345.jsp_netdisk.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.xfl12345.jsp_netdisk.appconst.api.request.LoginRequestField;
import com.github.xfl12345.jsp_netdisk.appconst.api.result.*;
import com.github.xfl12345.jsp_netdisk.appconst.field.MySessionAttributes;
import com.github.xfl12345.jsp_netdisk.appconst.field.TbAccountField;
import com.github.xfl12345.jsp_netdisk.model.dao.TbDirectoryDao;
import com.github.xfl12345.jsp_netdisk.model.pojo.api.response.BaseResponseObject;
import com.github.xfl12345.jsp_netdisk.model.pojo.database.TbAccount;
import com.github.xfl12345.jsp_netdisk.model.pojo.result.RegisterResult;
import com.github.xfl12345.jsp_netdisk.model.service.EmailVerificationService;
import com.github.xfl12345.jsp_netdisk.model.service.TbAccountService;
import com.github.xfl12345.jsp_netdisk.model.service.TbPermissionService;
import com.github.xfl12345.jsp_netdisk.model.utility.JsonRequestUtils;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static com.github.xfl12345.jsp_netdisk.StaticSpringApp.appInfo;

@Controller
@RequestMapping(value = "account", method = RequestMethod.POST)
public class OldApiController {


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
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public void loginAction(HttpServletRequest request, HttpServletResponse response) {
        //新建 JSON对象，后续再格式化为 JSON字符串 作为响应数据
        BaseResponseObject responseObject = new BaseResponseObject();
        responseObject.version = String.valueOf(version);
        responseObject.success = false;
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
                responseObject.message = "请求格式错误！请使用JSON格式！";
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
                responseObject.success = true;
            }
            msg = loginApiResult.getName();
        }
        responseObject.message = msg;
        JsonRequestUtils.responseObjectAsJsonStr(response, responseObject);
    }

    /**
     * 对POST 注销登录请求，做出API响应
     * @param request http POST请求
     */
    @RequestMapping(value = "logout", method = RequestMethod.POST)
    public void logoutAction(HttpServletRequest request, HttpServletResponse response) {
        BaseResponseObject responseObject = new BaseResponseObject();
        responseObject.version = String.valueOf(version);
        responseObject.success = false;
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
            responseObject.message = msg;
            JsonRequestUtils.responseObjectAsJsonStr(response, responseObject);
            return;
        }
        LogoutApiResult logoutApiResult = tbAccountService.logout(request.getSession());
        if( logoutApiResult == LogoutApiResult.SUCCEED){
            responseObject.success = true;
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
        responseObject.message = msg;
//        responseJsonObject.put("htmlCode",htmlCode);
        JsonRequestUtils.responseObjectAsJsonStr(response, responseObject);
    }

    /**
     * 对POST 注销登录请求，做出API响应
     * @param request http GET请求
     */
    @RequestMapping(value = "register", method = RequestMethod.POST)
    public void registerAction(HttpServletRequest request, HttpServletResponse response) {
        BaseResponseObject responseObject = new BaseResponseObject();
        responseObject.version = String.valueOf(version);
        responseObject.success = false;
        String msg = "";
        //格式化请求数据为JSON对象
        JSONObject requesetJsonObject = null;
        try {
            requesetJsonObject = JsonRequestUtils.getJsonObject(request);
        }
        catch (Exception e){
            responseObject.message = "请求格式错误！请使用JSON格式！";
            JsonRequestUtils.responseObjectAsJsonStr(response, responseObject);
            return;
        }
        RegisterResult registerResult = tbAccountService.register(request.getSession(), requesetJsonObject, 1);
        if(registerResult.registerApiResult.equals(RegisterApiResult.SUCCEED)){
            responseObject.success = true;
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
        responseObject.message = msg;
        JsonRequestUtils.responseObjectAsJsonStr(response, responseObject);
    }

    /**
     * 对POST 发送邮箱验证码请求，做出API响应
     * @param request http POST请求
     */
    @RequestMapping(value = "emailVerificationCodeSentEmail", method = RequestMethod.POST)
    public void emailVerificationCodeGet(HttpServletRequest request, HttpServletResponse response){
        BaseResponseObject responseObject = new BaseResponseObject();
        responseObject.version = String.valueOf(version);
        responseObject.success = false;
        //格式化请求数据为JSON对象
        JSONObject requesetJsonObject = null;
        try {
            requesetJsonObject = JsonRequestUtils.getJsonObject(request);
        }
        catch (Exception e){
            responseObject.message = "请求格式错误！请使用JSON格式！";
            JsonRequestUtils.responseObjectAsJsonStr(response, responseObject);
            return;
        }
        SentEmailApiResult apiResult = emailVerificationService.sendEmail(request.getSession());
        if(apiResult.equals(SentEmailApiResult.SUCCEED)){
            responseObject.success = true;
        }
        responseObject.message =apiResult.getName();
        JsonRequestUtils.responseObjectAsJsonStr(response, responseObject);
    }

    /**
     * 对POST 提交邮箱验证码 请求，做出API响应
     * @param request http POST请求
     */
    @RequestMapping(value = "emailVerificationCode", method = RequestMethod.POST)
    public void emailVerificationCodeAction(HttpServletRequest request, HttpServletResponse response){
        BaseResponseObject responseObject = new BaseResponseObject();
        responseObject.version = String.valueOf(version);
        responseObject.success = false;
        TbAccount tbAccount = (TbAccount) request.getSession().getAttribute(MySessionAttributes.TB_ACCOUNT);
        if(tbAccount.getAccountStatus().equals(TbAccountField.ACCOUNT_STATUS.NORMAL)){
            responseObject.success = true;
            responseObject.message = "邮箱已激活";
        }
        else{
            EmailVerificationApiResult apiResult = emailVerificationService.activeEmail(request);
            if( apiResult.equals(EmailVerificationApiResult.SUCCEED)){
                responseObject.success = true;
            }
            responseObject.message = apiResult.getName();
        }
        JsonRequestUtils.responseObjectAsJsonStr(response, responseObject);
    }
}
