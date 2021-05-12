package com.github.xfl12345.jsp_netdisk.model.utils;

import org.apache.ibatis.io.Resources;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Properties;

public class MyPropertiesUtils {

    public MyPropertiesUtils(){}

    /**
     * 从 Properties 对象弹出某个key对应的值。弹出后，该Properties对象里再无该key。
     * @param properties 非空Properties对象
     * @param key Properties对象里的键
     * @return 返回Properties对象里的key对应的value。如果key不存在，则返回 null 。
     */
    public String popKeyFromProperties(Properties properties, String key){
        String value = null;
        if(properties.containsKey(key)){
            value = properties.getProperty(key);
            properties.remove(key);
        }
        return value;
    }

    /**
     * 加载properties文件为Properties对象
     * @param resPath 文件路径
     * @return 返回一个Properties对象
     */
    public Properties loadPropFromFile(String resPath){
        Properties properties = null;
        try {
//            InputStream inputStream = Resources.getResourceAsStream(resPath);
//            properties = new Properties();
//            properties.load(inputStream);
//            inputStream.close();
            properties = Resources.getResourceAsProperties(getClass().getClassLoader(),resPath);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return properties;
    }

    /**
     * @param resPath properties配置文件的路径
     * @param obj 要被通过暴力反射填装数据的对象
     * @return 返回一个Properties对象
     */
    public Properties loadProp2obj(String resPath, Object obj){
        Properties properties = null;
        try{
            properties = loadPropFromFile(resPath);
            Class cls = obj.getClass();
            Field[] fields = cls.getDeclaredFields();
            for(Field f : fields){
                f.setAccessible(true);// 暴力反射。 私有的也可以被访问。
                f.set(obj, properties.getProperty(f.getName()));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return properties;
    }

}
