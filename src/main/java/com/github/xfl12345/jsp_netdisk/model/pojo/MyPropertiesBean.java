package com.github.xfl12345.jsp_netdisk.model.pojo;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * 简单粗暴的把Properties文件加载成一个Properties对象
 */
public class MyPropertiesBean extends Properties {

    public MyPropertiesBean(String propertiesFileRelativePath) throws IOException {
        InputStream inputStream = MyPropertiesBean.class.getClassLoader().getResourceAsStream(propertiesFileRelativePath);
        this.load(inputStream);
        inputStream.close();
    }
}
