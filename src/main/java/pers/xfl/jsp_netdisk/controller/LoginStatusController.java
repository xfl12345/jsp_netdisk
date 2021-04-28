package pers.xfl.jsp_netdisk.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import pers.xfl.jsp_netdisk.StaticSpringApp;
import pers.xfl.jsp_netdisk.model.appconst.LoginApiResult;
import pers.xfl.jsp_netdisk.model.appconst.LogoutApiResult;
import pers.xfl.jsp_netdisk.model.pojo.database.TbAccount;
import pers.xfl.jsp_netdisk.model.service.TbAccountService;
import pers.xfl.jsp_netdisk.model.utils.JsonRequestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 账号登录状态控制对象
 */
@Controller
public class LoginStatusController {

    /**
     * version值即 API 版本号，LoginStatusController 自身的属性，
     * 粒度细到一个Controller的版本号，方便部分API迭代更新
     */
    public static final int version = 1;

    @Autowired
    private TbAccountService tbAccountService;

    /**
     * 对GET 登录请求，做出VIEW响应，返回登录界面
     * @param request http GET请求
     * @return 跳转VIEW界面的名称
     */
    @RequestMapping(value = "login", method = RequestMethod.GET)
    public ModelAndView loginView(HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        if(tbAccountService.checkIsLoggedIn(request)){
            modelAndView.setViewName("redirect:home");
            return modelAndView;
        }
        else {
            modelAndView.addObject("title", StaticSpringApp.getAppInfo().appName);
            modelAndView.addObject("divFormTitle", "登录，打开异次元世界！");
            modelAndView.setViewName("login");
        }
        return modelAndView;
    }

    /**
     * 对POST 登录请求，做出API响应
     * @param request http POST请求
     * @param response http响应
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public void loginAction(HttpServletRequest request, HttpServletResponse response) {
        //新建 JSON对象，后续再格式化为 JSON字符串 作为响应数据
        JSONObject responseJsonObject = new JSONObject();
        responseJsonObject.put("version", version);
        responseJsonObject.put("flag",false);
        String msg = "";
        //是否已登录？
        if(tbAccountService.checkIsLoggedIn(request)){//已登录
            msg = "已登录一个账号！请先注销当前账号！";
        }
        else {//格式化请求数据为JSON对象
            JSONObject requesetJsonObject = null;
            try {
                requesetJsonObject = JsonRequestUtils.getJsonObject(request);
//                System.out.println(requesetJsonObject.toJSONString());
            }
            catch (Exception e){
                msg = "请求格式错误！请使用JSON格式！";
                responseJsonObject.put("msg",msg);
                JsonRequestUtils.responseJsonStr(response, responseJsonObject);
                return;
            }
            //获取session对象
            HttpSession session = request.getSession();
            //获取sessionID
            String sessionId = session.getId();
            String username = (String) requesetJsonObject.get("username");
            String password =  (String) requesetJsonObject.get("password");
            TbAccount tbAccount = new TbAccount();
            tbAccount.setUsername(username);
            // 验证，顺便获取账号ID
            LoginApiResult loginApiResult = tbAccountService.login(tbAccount, password, sessionId);
            //成功唯一绑定 会话ID 和 账号ID
            if(loginApiResult.equals(LoginApiResult.SUCCEED)) {
                session.setAttribute("accountId", tbAccount.getAccountId()+"");
                responseJsonObject.replace("flag", true);
            }
            msg = loginApiResult.getName();
        }
        responseJsonObject.put("msg",msg);
        JsonRequestUtils.responseJsonStr(response, responseJsonObject);
    }

    /**
     * 对GET 注销登录请求，做出VIEW响应，返回登录界面
     * @param request http GET请求
     */
    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public String logoutView(HttpServletRequest request) {
        return "logout";
    }

    /**
     * 对POST 注销登录请求，做出API响应
     * @param request http POST请求
     */
    @RequestMapping(value = "logout", method = RequestMethod.POST)
    public void logoutAction(HttpServletRequest request, HttpServletResponse response) {
        JSONObject responseJsonObject = new JSONObject();
        responseJsonObject.put("version", version);
        responseJsonObject.put("flag",false);
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
            responseJsonObject.put("msg",msg);
            JsonRequestUtils.responseJsonStr(response, responseJsonObject);
            return;
        }

        String accountIdStr = (String) request.getSession().getAttribute("accountId");
        LogoutApiResult logoutApiResult = tbAccountService.logout(accountIdStr);
        if( logoutApiResult == LogoutApiResult.SUCCEED){
            request.getSession().setAttribute("accountId", null);
            responseJsonObject.replace("flag",true);
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
        responseJsonObject.put("msg",msg);
//        responseJsonObject.put("htmlCode",htmlCode);
        JsonRequestUtils.responseJsonStr(response, responseJsonObject);
    }

}
