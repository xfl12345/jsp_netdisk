package com.github.xfl12345.jsp_netdisk.model.utility.jdbc;

import com.github.xfl12345.jsp_netdisk.StaticSpringApp;
import com.mysql.jdbc.AbandonedConnectionCleanupThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

/**
 * 这是一个用来结束JDBC驱动的类，防止redeploy时被警告存在内存泄露风险
 */
@WebListener
public class ContextFinalizer implements ServletContextListener {

    private final Logger logger = LoggerFactory.getLogger(ContextFinalizer.class);

    public void contextInitialized(ServletContextEvent sce) {
    }

    public void contextDestroyed(ServletContextEvent sce) {
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        Driver d = null;
        boolean isFirstOneDeregistered = false;
        while (drivers.hasMoreElements()) {
            try {
                d = drivers.nextElement();
                //优先卸载JDBC驱动
                if(isFirstOneDeregistered){
                    DriverManager.deregisterDriver(d);
                    logger.info(String.format("Driver %s deregistered", d));
                }
                else if(  d.getClass().getName().equals( StaticSpringApp.myDataSource.getDriver() )  ){
                    DriverManager.deregisterDriver(d);
                    logger.info(String.format("Driver %s deregistered", d));
                    isFirstOneDeregistered = true;
                }
            } catch (SQLException ex) {
               logger.error(String.format("Error deregistering driver %s", d) + ":" + ex);
            }
        }
        logger.info("jdbc Driver closing.");
        // AbandonedConnectionCleanupThread.shutdown();
        AbandonedConnectionCleanupThread.checkedShutdown();
        int sleepForSeconds = 2;
        try {
            //尝试休眠2秒，等待JDBC驱动完全从内存释放，防止过快被热部署重新注册JDBC驱动
            for (int i = 0; i < sleepForSeconds; i++) {
                Thread.sleep(1000L);
                logger.info("sleep count="+(i+1)+" sec");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("clean thread success.");
    }
}
