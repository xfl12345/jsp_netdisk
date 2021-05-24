package com.github.xfl12345.jsp_netdisk.controller;

import com.github.xfl12345.jsp_netdisk.appconst.MyConst;
import com.github.xfl12345.jsp_netdisk.appconst.field.MySessionAttributes;
import com.github.xfl12345.jsp_netdisk.appconst.field.TbAccountField;
import com.github.xfl12345.jsp_netdisk.model.pojo.database.TbAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.github.xfl12345.jsp_netdisk.model.service.AccountService;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.github.xfl12345.jsp_netdisk.StaticSpringApp.appInfo;

/**
 * 账号管理 控制对象
 */
@Controller
@RequestMapping(value = "account")
public class AccountController {

    /**
     * version值即 API 版本号，AccountController 自身的属性，
     * 粒度细到一个Controller的版本号，方便部分API迭代更新
     */
    public static final int version = 1;

    private final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private AccountService accountService;


    /**
     * 对GET 登录请求，做出VIEW响应，返回登录界面
     * @param request http GET请求
     * @return 跳转VIEW界面的名称
     */
    @RequestMapping(value = "login", method = RequestMethod.GET)
    public ModelAndView loginView(HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        if(accountService.checkIsLoggedIn(request.getSession())){
            modelAndView.setViewName("redirect:home");
            return modelAndView;
        }
        else {
            modelAndView.addObject("title", appInfo.getAppName());
            modelAndView.addObject("divFormTitle", "登录，打开异次元世界！");
            modelAndView.setViewName("login");
        }
        return modelAndView;
    }

    /**
     * 对GET 注销登录请求，做出VIEW响应，返回登录界面
     */
    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public ModelAndView logoutView(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        if(accountService.checkIsLoggedIn(request.getSession())){
            modelAndView.setViewName("logout");
            return modelAndView;
        }
        else {
            modelAndView.setViewName("redirect:login");
        }
        return modelAndView;
    }

    /**
     * 对GET 注销登录请求，做出VIEW响应，返回登录界面
     * @param request http GET请求
     */
    @RequestMapping(value = "register", method = RequestMethod.GET)
    public String registerView(HttpServletRequest request) {
        return "register";
    }

    @RequestMapping(value = "home", method = RequestMethod.GET)
    public ModelAndView homeView(HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        if(accountService.checkIsLoggedIn(request.getSession())){
            TbAccount tbAccount = (TbAccount) request.getSession().getAttribute(MySessionAttributes.TB_ACCOUNT);
            if(tbAccount.getAccountStatus().equals(TbAccountField.ACCOUNT_STATUS.EMAIL_NOT_ACTIVATED)){
                modelAndView.setViewName("redirect:activeEmail");
            }
            else {
                modelAndView.setViewName("home");
                // TODO

            }
        }
        else {
            setJumpViewOfLogin(modelAndView);
        }
        return modelAndView;
    }

    @RequestMapping(value = "activeEmail", method = RequestMethod.GET)
    public ModelAndView activeEmailView(HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        if(accountService.checkIsLoggedIn(request.getSession())){
            TbAccount tbAccount = (TbAccount) request.getSession().getAttribute(MySessionAttributes.TB_ACCOUNT);
            if(tbAccount.getAccountStatus().equals(TbAccountField.ACCOUNT_STATUS.EMAIL_NOT_ACTIVATED)){
                modelAndView.setViewName("activeEmail");
                modelAndView.addObject("title", "激活邮箱");
                modelAndView.addObject("divFormTitle", "激活邮箱，解除封印！");
                modelAndView.addObject("msg", "您的邮箱暂未激活，请到邮箱查收验证码。");
            }
            else {
                modelAndView.setViewName("redirect:home");
            }
        }
        else {
            setJumpViewOfLogin(modelAndView);
        }
        return modelAndView;
    }

    @RequestMapping(value = "username", method = RequestMethod.GET)
    public void getUsername(HttpServletRequest request, HttpServletResponse response){
        String username = MyConst.ANONYMOUS;
        if(accountService.checkIsLoggedIn(request.getSession())){
            TbAccount tbAccount = (TbAccount) request.getSession().getAttribute(MySessionAttributes.TB_ACCOUNT);
            username = tbAccount.getUsername();
        }
        try {
            response.getWriter().print(username);
        }
        catch (Exception e){
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }


    /**
     * 配置倒计时转跳页面到登录页面
     * 适用于未登录或登录状态超时的用户
     */
    public void setJumpViewOfLogin(ModelAndView modelAndView){
        modelAndView.setViewName("jump");
        modelAndView.addObject("title", "请登录");
        modelAndView.addObject("msg", "您的登录状态已失效，请重新登录。");
        modelAndView.addObject("jumpView", "index");
        modelAndView.addObject("jumpCountdown", 3);
    }

}
