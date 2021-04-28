package pers.xfl.jsp_netdisk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pers.xfl.jsp_netdisk.StaticSpringApp;
import pers.xfl.jsp_netdisk.model.appconst.AppInfo;
import pers.xfl.jsp_netdisk.model.service.TbAccountService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    @Autowired
    private TbAccountService tbAccountService;

    @RequestMapping("index")
    public ModelAndView indexView(HttpServletRequest request){
        AppInfo appInfo = StaticSpringApp.getBean("appInfo",AppInfo.class);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("title", appInfo.appName);
        modelAndView.setViewName("index");
        modelAndView.addObject("divFormTitle","欢迎来到"+ appInfo.appName + "！");

        if(tbAccountService.checkIsLoggedIn(request)) {
//            modelAndView.addObject("divFormTitle","您已登录！");
            modelAndView.addObject("btn1text", "进入网盘");
            modelAndView.addObject("btn1url", "home");
            modelAndView.addObject("btn2text", "注销登录");
            modelAndView.addObject("btn2url", "logout");
        }
        else{
            modelAndView.addObject("btn1text", "登录");
            modelAndView.addObject("btn1url", "login");
            modelAndView.addObject("btn2text", "注册");
            modelAndView.addObject("btn2url", "register");
        }
        return modelAndView;
    }

    @RequestMapping("hello_springmvc")
    public ModelAndView helloSpringmvcView(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("hello_springmvc");
        modelAndView.addObject("title","这是个可以在Controller里改的Title");
        modelAndView.addObject("msg","Hello SpringMVC!");
//        System.out.println("yes it is");
        return modelAndView;
    }
}
