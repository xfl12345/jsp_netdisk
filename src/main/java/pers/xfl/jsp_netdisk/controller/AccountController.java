package pers.xfl.jsp_netdisk.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pers.xfl.jsp_netdisk.model.service.TbAccountService;
import pers.xfl.jsp_netdisk.model.utils.JsonRequestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 账号管理 控制对象
 */
@Controller
public class AccountController {

    /**
     * version值即 API 版本号，AccountController 自身的属性，
     * 粒度细到一个Controller的版本号，方便部分API迭代更新
     */
    public static final int version = 1;

    @Autowired
    private TbAccountService tbAccountService;

    /**
     * 对GET 注销登录请求，做出VIEW响应，返回登录界面
     * @param request http GET请求
     */
    @RequestMapping(value = "register", method = RequestMethod.GET)
    public String registerView(HttpServletRequest request) {
        return "register";
    }

    /**
     * 对POST 注销登录请求，做出API响应
     * @param request http GET请求
     */
    @RequestMapping(value = "register", method = RequestMethod.POST)
    public void registerAction(HttpServletRequest request, HttpServletResponse response) {
        JSONObject responseJsonObject = new JSONObject();
        String msg;
        //格式化请求数据为JSON对象
        JSONObject requesetJsonObject = null;
        try {
            requesetJsonObject = JsonRequestUtils.getJsonObject(request);
        }
        catch (Exception e){
            msg = "请求格式错误！请使用JSON格式！";
            responseJsonObject.put("msg",msg);
            JsonRequestUtils.responseJsonStr(response, responseJsonObject);
            return;
        }
        System.out.println(requesetJsonObject.toJSONString());



    }


}
