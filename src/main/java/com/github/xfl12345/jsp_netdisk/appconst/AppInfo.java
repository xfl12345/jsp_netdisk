package com.github.xfl12345.jsp_netdisk.appconst;

import com.github.xfl12345.jsp_netdisk.StaticSpringApp;
import com.github.xfl12345.jsp_netdisk.model.utility.MyPropertiesUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Properties;

import static com.github.xfl12345.jsp_netdisk.StaticSpringApp.myReflectUtils;

/**
 * webapp自己的版本信息
 */
public class AppInfo {
    private final int version = 1;
    private final String appName = "jsp_netdisk";
    private final String author = "xfl";

    public int getVersion() {
        return version;
    }

    public String getAppName() {
        return appName;
    }

    public String getAuthor() {
        return author;
    }

    public AppInfo(){}

    public AppInfo(String propertiesFileRelativePath){
        Properties properties = StaticSpringApp.getBean(MyPropertiesUtils.class)
                .loadPropFromFile(propertiesFileRelativePath);
        loadFromProperties(properties);
    }

    public AppInfo(Properties properties) {
        loadFromProperties(properties);
    }

    /**
     * 通过Properties对象和Java反射机制来完成赋值，
     * 将已有成员变量名作为键去获取对应的值并完成赋值操作。
     * @param properties 一个包含 属性名=值 的Properties对象
     */
    private void loadFromProperties(Properties properties){
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field f : fields) {
            try {
                //跳过final修饰的属性
                if (Modifier.isFinal(f.getModifiers()))
                    continue;
                //采用 "类名.属性" 的格式作为键值
                String key = this.getClass().getSimpleName()
                        + "." + f.getName();
                //完成赋值任务
                if(properties.containsKey(key))
                    f.set(this,  myReflectUtils.castToWrapperClassObject(f.getType(), (String) properties.get(key)) );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
