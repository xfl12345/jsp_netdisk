package com.github.xfl12345.jsp_netdisk.model.pojo.database;

import java.io.Serializable;

/**
 * (EmailVerification)实体类
 *
 * @author makejava
 * @since 2021-05-03 17:53:56
 */
public class EmailVerification implements Serializable {
    private static final long serialVersionUID = -54438104091274362L;
    /**
     * 账号绑定的电子邮箱
     */
    private String email;
    /**
     * 发送过的验证码都以JSON文本形式记录下来
     */
    private String verificationLog;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVerificationLog() {
        return verificationLog;
    }

    public void setVerificationLog(String verificationLog) {
        this.verificationLog = verificationLog;
    }

}
