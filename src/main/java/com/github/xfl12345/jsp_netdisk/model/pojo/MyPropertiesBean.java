package com.github.xfl12345.jsp_netdisk.model.pojo;

import java.io.IOException;
import java.util.Properties;


/**
 * 简单粗暴的把Properties文件加载成一个Properties对象
 */
public class MyPropertiesBean extends Properties {

    public MyPropertiesBean(String propertiesFileRelativePath) throws IOException {
        super.load(MyPropertiesBean.class.getClassLoader().getResourceAsStream(propertiesFileRelativePath));
    }
}
