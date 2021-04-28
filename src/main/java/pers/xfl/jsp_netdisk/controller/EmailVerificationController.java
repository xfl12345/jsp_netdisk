package pers.xfl.jsp_netdisk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pers.xfl.jsp_netdisk.model.service.EmailVerificationService;
import pers.xfl.jsp_netdisk.model.service.TbAccountService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 邮箱验证 控制对象
 */
@Controller
public class EmailVerificationController {

    @Autowired
    private TbAccountService tbAccountService;

    @Autowired
    private EmailVerificationService emailVerificationService;

    @RequestMapping(value = "getEmailVerificationCode")
    public void registerAction(HttpServletRequest request, HttpServletResponse response){

    }

}
