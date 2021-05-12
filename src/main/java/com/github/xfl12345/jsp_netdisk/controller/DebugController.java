package com.github.xfl12345.jsp_netdisk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class DebugController {

    @RequestMapping(value = "debug", method = RequestMethod.GET)
    public String debugView(){
        return "debug";
    }


}
