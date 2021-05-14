package com.github.xfl12345.jsp_netdisk.controller;

import com.github.xfl12345.jsp_netdisk.appconst.AppInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.github.xfl12345.jsp_netdisk.model.service.TbAccountService;

import javax.servlet.http.HttpServletRequest;

import static com.github.xfl12345.jsp_netdisk.StaticSpringApp.appInfo;

@Controller
public class IndexController {

    @Autowired
    private TbAccountService tbAccountService;

    @RequestMapping("index")
    public ModelAndView indexView(HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("title", appInfo.getAppName());
        modelAndView.setViewName("index");
        modelAndView.addObject("divFormTitle","欢迎来到"+ appInfo.getAppName() + "！");

        if(tbAccountService.checkIsLoggedIn(request.getSession())) {
//            modelAndView.addObject("divFormTitle","您已登录！");
            modelAndView.addObject("btn1text", "进入网盘");
            modelAndView.addObject("btn1url", "account/home");
            modelAndView.addObject("btn2text", "注销登录");
            modelAndView.addObject("btn2url", "account/logout");
        }
        else{
            modelAndView.addObject("btn1text", "登录");
            modelAndView.addObject("btn1url", "account/login");
            modelAndView.addObject("btn2text", "注册");
            modelAndView.addObject("btn2url", "account/register");
        }
        return modelAndView;
    }

    @RequestMapping("hello_springmvc")
    public ModelAndView helloSpringmvcView(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("hello_springmvc");
        modelAndView.addObject("title","这是个可以在Controller里改的Title");
        modelAndView.addObject("msg","Hello SpringMVC!");
//        logger("yes it is");
        return modelAndView;
    }
}
