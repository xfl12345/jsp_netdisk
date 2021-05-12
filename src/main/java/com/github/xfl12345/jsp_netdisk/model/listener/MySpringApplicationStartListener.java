package com.github.xfl12345.jsp_netdisk.model.listener;
import com.github.xfl12345.jsp_netdisk.controller.AccountController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.context.WebApplicationContext;

public class MySpringApplicationStartListener implements ApplicationListener<ContextRefreshedEvent> {

    private final Logger logger = LoggerFactory.getLogger(MySpringApplicationStartListener.class);

    public static WebApplicationContext webApplicationContext;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if(contextRefreshedEvent.getApplicationContext().getParent() != null){
            webApplicationContext = (WebApplicationContext) contextRefreshedEvent.getApplicationContext();
        }
        logger.info("我的父容器为：" + contextRefreshedEvent.getApplicationContext().getParent());
        logger.info("初始化时我被调用了。");
    }
}



