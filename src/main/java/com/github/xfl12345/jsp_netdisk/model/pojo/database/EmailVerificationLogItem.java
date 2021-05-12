package com.github.xfl12345.jsp_netdisk.model.pojo.database;

import java.util.Date;

public class EmailVerificationLogItem {
    /**
     * 当时绑定这个邮箱的用户名
     */
    public String username;
    /**
     * 当时绑定这个邮箱的账号ID
     */
    public String accountId;
    /**
     * 邮箱验证码
     */
    public String emailVerificationCode;
    /**
     * 电子邮件完成投递到邮箱服务器的时刻
     */
    public Date sentDate;
    /**
     * 标注邮件是否发送成功 （这将成为判断是否为无效邮箱的有力证据）
     */
    public boolean sentEmailSuccess;
}
