package com.github.xfl12345.jsp_netdisk.controller;

import com.github.xfl12345.jsp_netdisk.StaticSpringApp;

import com.github.xfl12345.jsp_netdisk.model.listener.MySessionEventListener;
import com.github.xfl12345.jsp_netdisk.model.listener.MySpringApplicationStartListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractRefreshableApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 * source code URL=https://blog.csdn.net/fly910905/article/details/87779283
 */
@Controller
@RequestMapping("reload")
public class ReloadController {

    private final Logger logger = LoggerFactory.getLogger(ReloadController.class);

    @ResponseBody
    @RequestMapping("reloadAll")
    public String reloadAll(HttpServletRequest request) {
        WebApplicationContext springMVCContext;
        ApplicationContext parent;

        if(MySpringApplicationStartListener.webApplicationContext == null)
        {
            springMVCContext = RequestContextUtils.findWebApplicationContext(request);
            parent = springMVCContext.getParent();
//            parent = ContextLoader.getCurrentWebApplicationContext();
//            parent = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
        }
        else {
            springMVCContext = MySpringApplicationStartListener.webApplicationContext;
            parent = MySpringApplicationStartListener.webApplicationContext.getParent();
        }

        if (parent != null) {
            logger.info("Root webApplicationContext was found.Refreshing...");
            ((AbstractRefreshableApplicationContext) parent).refresh();
        }
        else{
            logger.info("Current webApplicationContext has no parent.");
        }
        ((AbstractRefreshableApplicationContext) springMVCContext).refresh();
        return "success";
    }

    @ResponseBody
    @RequestMapping("reloadSpringContext")
    public String reloadSpringContext() {
        AbstractRefreshableApplicationContext context = (AbstractRefreshableApplicationContext) StaticSpringApp.springAppContext;
        context.refresh();
        return "success";
    }

}
