package com.github.xfl12345.jsp_netdisk.model.interceptor;

import com.github.xfl12345.jsp_netdisk.StaticSpringApp;
import com.github.xfl12345.jsp_netdisk.appconst.api.result.JsonApiResult;
import com.github.xfl12345.jsp_netdisk.model.pojo.api.response.JsonCommonApiResponseObject;
import com.github.xfl12345.jsp_netdisk.model.service.AccountService;
import com.github.xfl12345.jsp_netdisk.model.utility.JsonRequestUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyUploadInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
//        System.out.println("拦截请求");
        // TODO Auto-generated method stub
        if( StaticSpringApp.getBean(AccountService.class).checkIsLoggedIn(request.getSession()) ){

            return true;
        }
        //没登录，不给上传操作
        else {
            JsonCommonApiResponseObject responseObject = new JsonCommonApiResponseObject();
            responseObject.setApiResult(JsonApiResult.FAILED_NO_LOGIN);
            JsonRequestUtils.responseObjectAsJsonStr(response, responseObject);
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
//        System.out.println("拦截响应");
        // TODO Auto-generated method stub


    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
//        System.out.println("拦截渲染");
        // TODO Auto-generated method stub

    }

}