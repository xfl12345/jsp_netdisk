package pers.xfl.jsp_netdisk.model.utils.jdbc;

import com.mysql.jdbc.AbandonedConnectionCleanupThread;
import pers.xfl.jsp_netdisk.StaticSpringApp;

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
    private String getMyClassName(){
        return this.getClass().getName();
    }

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
                if(d.getClass().getName().equals(StaticSpringApp.myDataSource.getDriver())){
                    DriverManager.deregisterDriver(d);
                    System.out.println(String.format(getMyClassName()+":Driver %s deregistered", d));
                }
                else if(isFirstOneDeregistered){
                    DriverManager.deregisterDriver(d);
                    System.out.println(String.format(getMyClassName()+":Driver %s deregistered", d));
                }
            } catch (SQLException ex) {
                System.out.println(String.format(getMyClassName()+":Error deregistering driver %s", d) + ":" + ex);
            }
        }
//        try {
//              AbandonedConnectionCleanupThread.shutdown();
//        } catch (InterruptedException e) {
//            System.out.println("ContextFinalizer:SEVERE problem cleaning up: " + e.getMessage());
//            e.printStackTrace();
//        }
        System.out.println(getMyClassName()+":jdbc Driver closing.");
        AbandonedConnectionCleanupThread.checkedShutdown();
        System.out.println(getMyClassName()+":clean thread success.");
    }
}
