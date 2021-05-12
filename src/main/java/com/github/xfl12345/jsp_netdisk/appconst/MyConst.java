package com.github.xfl12345.jsp_netdisk.appconst;

import com.github.xfl12345.jsp_netdisk.StaticSpringApp;
import com.github.xfl12345.jsp_netdisk.model.utils.MyReflectUtils;
import com.github.xfl12345.jsp_netdisk.model.utils.MyPropertiesUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Properties;

public class MyConst {
    public final static String INFORMATION_SCHEMA_TABLE_NAME = "information_schema";

    public final static int SHA_512_HEX_STR_LENGTH = 128;

    /**
     * 待发送的电子邮件的队列最大长度
     */
    private final int emailQueueMaxSize = 2000;

    /**
     * 电子邮箱验证码长度
     */
    private final int emailVerificationCodeLength = 8;

    /**
     * 电子邮箱验证码有效时间（单位：分钟）
     */
    private final int emailVerificationCodeVaildTimeInMinutes = 10;

    /**
     * 至少每隔多少秒才能再对同一个电子邮箱发邮件
     */
    private final int sentEmailMaxFrequencyPeerSeconds = 60 * 10;

    /**
     * 每天最多对同一个电子邮箱发邮件发送多少封邮件
     */
    private final int sentEmailMaxFrequencyPeerDay = 3;

    /**
     * 网盘文件的实际存储路径
     */
    private final String NETDISK_STORAGE_PATH = (new File("")).getPath() + "netdisk";


    public String getNetdiskStoragePath() {
        return NETDISK_STORAGE_PATH;
    }

    public int getEmailQueueMaxSize() {
        return emailQueueMaxSize;
    }

    public int getEmailVerificationCodeLength() {
        return emailVerificationCodeLength;
    }

    public int getEmailVerificationCodeVaildTimeInMinutes() {
        return emailVerificationCodeVaildTimeInMinutes;
    }

    public int getSentEmailMaxFrequencyPeerSeconds() {
        return sentEmailMaxFrequencyPeerSeconds;
    }

    public int getSentEmailMaxFrequencyPeerDay() {
        return sentEmailMaxFrequencyPeerDay;
    }

    public MyConst(){}

    public MyConst(String propertiesFileRelativePath) {
        Properties properties = StaticSpringApp.getBean(MyPropertiesUtils.class)
                .loadPropFromFile(propertiesFileRelativePath);
        loadFromProperties(properties);
    }

    public MyConst(Properties properties) {
        loadFromProperties(properties);
    }


    /**
     * 通过Properties对象和Java反射机制来完成赋值，
     * 将已有成员变量名作为键去获取对应的值并完成赋值操作。
     * @param properties 一个包含 属性名=值 的Properties对象
     */
    private void loadFromProperties(Properties properties) {
        MyReflectUtils myReflectUtils = StaticSpringApp.myReflectUtils;
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
                    f.set(this,  myReflectUtils.cast(f.getType(), (String) properties.get(key)) );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}