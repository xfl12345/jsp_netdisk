package com.github.xfl12345.jsp_netdisk.model.pojo.database;

import java.io.Serializable;

/**
 * (TbAccount)实体类
 *
 * @author makejava
 * @since 2021-04-19 16:00:51
 */
public class TbAccount implements Serializable {
    private static final long serialVersionUID = 461039296624433981L;
    /**
     * 账号ID
     */
    private Long accountId;
    /**
     * 用户名
     */
    private String username;
    /**
     * 账号密码的哈希值
     */
    private String passwordHash;
    /**
     * 账号密码的哈希值计算的佐料
     */
    private String passwordSalt;
    /**
     * 账号权限ID
     */
    private Long permissionId;
    /**
     * 注册时间，精确至秒
     */
    private String registerTime;
    /**
     * 注册时间之毫秒
     */
    private Integer registerTimeInMs;
    /**
     * 账号绑定的电子邮箱
     */
    private String email;
    /**
     * 用户性别
     */
    private String gender;
    /**
     * 账号状态代码
     */
    private Integer accountStatus;


    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    public Integer getRegisterTimeInMs() {
        return registerTimeInMs;
    }

    public void setRegisterTimeInMs(Integer registerTimeInMs) {
        this.registerTimeInMs = registerTimeInMs;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(Integer accountStatus) {
        this.accountStatus = accountStatus;
    }

}
