package pers.xfl.jsp_netdisk;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import pers.xfl.jsp_netdisk.model.appconst.AppInfo;
import pers.xfl.jsp_netdisk.model.utils.jdbc.MyDataSource;


public class StaticSpringApp implements ApplicationContextAware {
    public static ApplicationContext springAppContext;
    public static MyDataSource myDataSource;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        springAppContext = applicationContext;
        myDataSource = springAppContext.getBean("dataSource",MyDataSource.class);
    }

    public static  <T> T getBean(String name, Class<T> requiredType) throws BeansException{
        return springAppContext.getBean(name, requiredType);
    }

    public static SqlSessionTemplate getSqlSessionTemplate(){
        return springAppContext.getBean("sqlSessionTemplate",SqlSessionTemplate.class);
    }

    public static SqlSession getSqlSession() {
        return springAppContext.getBean("sqlSessionFactory", SqlSessionFactory.class).openSession();
    }

    public static AppInfo getAppInfo(){
        return springAppContext.getBean("appInfo",AppInfo.class);
    }


}
