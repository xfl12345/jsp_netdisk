package pers.xfl.jsp_netdisk.model.pojo.database;

import java.io.Serializable;

/**
 * (TbDirectory)实体类
 *
 * @author makejava
 * @since 2021-04-19 16:00:52
 */
public class TbDirectory implements Serializable {
    private static final long serialVersionUID = -13019937913780378L;
    /**
     * 目录ID
     */
    private Long directoryId;
    /**
     * 父目录ID
     */
    private Long parentDirectoryId;
    /**
     * 目录所属账号
     */
    private Long accountId;
    /**
     * 目录名称
     */
    private String directoryName;


    public Long getDirectoryId() {
        return directoryId;
    }

    public void setDirectoryId(Long directoryId) {
        this.directoryId = directoryId;
    }

    public Long getParentDirectoryId() {
        return parentDirectoryId;
    }

    public void setParentDirectoryId(Long parentDirectoryId) {
        this.parentDirectoryId = parentDirectoryId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getDirectoryName() {
        return directoryName;
    }

    public void setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
    }

}
