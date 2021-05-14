package com.github.xfl12345.jsp_netdisk.model.utility.jdbc;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Properties;

import static com.github.xfl12345.jsp_netdisk.StaticSpringApp.myReflectUtils;

public class BaseSqlConnectionUrlParameter {
    protected String sqlServerDriverName;

    protected String sqlProtocal = "jdbc";
    protected String sqlSubProtocal = "mysql";

    protected String sqlServerAddress;
    protected String sqlServerPort;

    protected String specificDbName = "jsp_netdisk";

    public BaseSqlConnectionUrlParameter() {}

    public BaseSqlConnectionUrlParameter(Properties properties){
        loadFromProperties(properties);
    }

    /**
     * 通过Properties对象和Java反射机制来完成赋值，
     * 将已有成员变量名作为键去获取对应的值并完成赋值操作。
     * @param properties 一个包含 属性名=值 的Properties对象
     */
    private void loadFromProperties(Properties properties) {
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field f : fields) {
            try {
                //跳过final修饰的属性
                if (Modifier.isFinal(f.getModifiers()))
                    continue;
                String key = f.getName();
                //完成赋值任务
                if(properties.containsKey(key))
                    f.set(this,  myReflectUtils.castToWrapperClassObject(f.getType(), (String) properties.get(key)) );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 通过克隆来构造对象
     * @param base 同一个类的对象
     */
    public BaseSqlConnectionUrlParameter(BaseSqlConnectionUrlParameter base) {
        this.sqlServerDriverName = base.getSqlServerDriverName();
        this.sqlProtocal = base.getSqlProtocal();
        this.sqlSubProtocal = base.getSqlSubProtocal();
        this.sqlServerAddress = base.getSqlServerAddress();
        this.sqlServerPort = base.getSqlServerPort();
        this.specificDbName = base.getSpecificDbName();
    }

    public String getSqlServerDriverName() {
        return sqlServerDriverName;
    }

    public void setSqlServerDriverName(String sqlServerDriverName) {
        this.sqlServerDriverName = sqlServerDriverName;
    }

    public String getSqlProtocal() {
        return sqlProtocal;
    }

    public void setSqlProtocal(String sqlProtocal) {
        this.sqlProtocal = sqlProtocal;
    }

    public String getSqlSubProtocal() {
        return sqlSubProtocal;
    }

    public void setSqlSubProtocal(String sqlSubProtocal) {
        this.sqlSubProtocal = sqlSubProtocal;
    }

    public String getSqlServerAddress() {
        return sqlServerAddress;
    }

    public void setSqlServerAddress(String sqlServerAddress) {
        this.sqlServerAddress = sqlServerAddress;
    }

    public String getSqlServerPort() {
        return sqlServerPort;
    }

    public void setSqlServerPort(String sqlServerPort) {
        this.sqlServerPort = sqlServerPort;
    }

    public String getSpecificDbName() {
        return specificDbName;
    }

    public void setSpecificDbName(String specificDbName) {
        this.specificDbName = specificDbName;
    }

}
