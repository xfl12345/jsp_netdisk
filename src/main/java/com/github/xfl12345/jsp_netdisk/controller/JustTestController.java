package com.github.xfl12345.jsp_netdisk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class JustTestController {

    @RequestMapping("hello3")
    public ModelAndView helloSpringmvcView(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("hello_springmvc");
        modelAndView.addObject("title","这是个可以在Controller里改的Title");
        modelAndView.addObject("msg","Hello SpringMVC!");
//        logger("yes it is");
        return modelAndView;
    }

}
