package com.github.xfl12345.jsp_netdisk.model.pojo.database;

import java.io.Serializable;

/**
 * (TbFile)实体类
 *
 * @author makejava
 * @since 2021-04-19 16:00:53
 */
public class AccountSession implements Serializable {
    private static final long serialVersionUID = -19312079077241905L;

    /**
     * 账号ID
     */
    private Long accountId;

    /**
     * 会话ID
     */
    private String sessionId;


    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

}
