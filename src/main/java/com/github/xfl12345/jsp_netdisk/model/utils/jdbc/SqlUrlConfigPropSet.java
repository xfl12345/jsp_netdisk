package com.github.xfl12345.jsp_netdisk.model.utils.jdbc;

import com.mysql.jdbc.ConnectionPropertiesImpl;
import com.github.xfl12345.jsp_netdisk.StaticSpringApp;
import com.github.xfl12345.jsp_netdisk.model.utils.MyReflectUtils;

import java.util.TreeSet;

public class SqlUrlConfigPropSet {
    public static TreeSet<String> confPropSet;
    static {
        MyReflectUtils myReflectUtils = StaticSpringApp.getBean(MyReflectUtils.class);
        try {
            confPropSet = myReflectUtils.getFieldNamesAsTreeSet( ConnectionPropertiesImpl.class );
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }
}
