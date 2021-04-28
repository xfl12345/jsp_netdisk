package pers.xfl.jsp_netdisk.model.pojo.database;

import java.io.Serializable;

/**
 * (AccountFile)实体类
 *
 * @author makejava
 * @since 2021-04-19 16:00:46
 */
public class AccountFile implements Serializable {
    private static final long serialVersionUID = -88506515402598380L;
    /**
     * 账号ID
     */
    private Long accountId;
    /**
     * 账号拥有的文件
     */
    private Long fileId;


    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

}
