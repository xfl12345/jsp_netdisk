package com.github.xfl12345.jsp_netdisk;

import com.fasterxml.uuid.Generators;
import com.github.xfl12345.jsp_netdisk.appconst.AppInfo;
import com.github.xfl12345.jsp_netdisk.appconst.MyConst;
import com.github.xfl12345.jsp_netdisk.model.listener.MySessionEventListener;
import com.github.xfl12345.jsp_netdisk.model.utility.check.JsonSchemaCheck;
import com.github.xfl12345.jsp_netdisk.model.utility.MyPropertiesUtils;
import com.github.xfl12345.jsp_netdisk.model.utility.MyReflectUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.jsoup.nodes.Document;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import com.github.xfl12345.jsp_netdisk.model.pojo.html.VerificationEmailTemplate;
import com.github.xfl12345.jsp_netdisk.model.utility.jdbc.MyDataSource;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;


public class StaticSpringApp implements ApplicationContextAware {
    public static ApplicationContext springAppContext;
    public static MyReflectUtils myReflectUtils;
    public static MyPropertiesUtils myPropertiesUtils;
    public static MyConst myConst;
    public static AppInfo appInfo;
    public static MyDataSource myDataSource;
    public static JsonSchemaCheck baseRequestObjectChecker;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        springAppContext = applicationContext;
        //MyReflectUtils必须放到最前面
        myReflectUtils = springAppContext.getBean(MyReflectUtils.class);
        myPropertiesUtils = springAppContext.getBean(MyPropertiesUtils.class);
        myConst = springAppContext.getBean(MyConst.class);
        appInfo = springAppContext.getBean(AppInfo.class);
        myDataSource = springAppContext.getBean(MyDataSource.class);
        baseRequestObjectChecker = getJsonSchemaCheck("baseRequestObjectChecker");
    }

    public static <T> T getBean(Class<T> requiredType) throws BeansException{
        return springAppContext.getBean(requiredType);
    }

    public static SqlSessionFactory getSqlSessionFactory() throws Exception {
        return springAppContext.getBean(SqlSessionFactoryBean.class).getObject();
    }

    public static SqlSession getSqlSession() throws Exception {
        return getSqlSessionFactory().openSession();
    }

    public static String getUUID(){
        return Generators.timeBasedGenerator().generate().toString();
    }

    public static Document getVerificationEmailTemplate(){
        return springAppContext.getBean(VerificationEmailTemplate.class).getEmailHtmlDocument();
    }

    public static SimpleDateFormat getSimpleDateFormat(){
        return springAppContext.getBean("simpleDateFormat",SimpleDateFormat.class);
    }
    public static SimpleDateFormat getMillisecondFormatter(){
        return springAppContext.getBean("millisecondFormatter",SimpleDateFormat.class);
    }

    public static JsonSchemaCheck getJsonSchemaCheck(String beanId){
        return springAppContext.getBean(beanId, JsonSchemaCheck.class);
    }

    public static HttpSession getSessionById(String sessionId){
        return springAppContext.getBean(MySessionEventListener.class).getSessionById(sessionId);
    }
}
