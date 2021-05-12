package com.github.xfl12345.jsp_netdisk.model.pojo.database;

import java.io.Serializable;

/**
 * (TbPermission)实体类
 *
 * @author makejava
 * @since 2021-04-19 16:00:54
 */
public class TbPermission implements Serializable {
    private static final long serialVersionUID = 116750378318234900L;
    /**
     * 权限条目ID
     */
    private Long permissionId;
    /**
     * 上传文件权限的代码
     */
    private Integer uploadFile;
    /**
     * 下载文件权限的代码
     */
    private Integer downloadFile;
    /**
     * 管理员权限的代码
     */
    private Integer accountOperation;
    /**
     * 账号信息公开程度的代码
     */
    private Integer accountInfoOperation;
    /**
     * 文件操作权限的代码
     */
    private Integer fileOperation;


    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }

    public Integer getUploadFile() {
        return uploadFile;
    }

    public void setUploadFile(Integer uploadFile) {
        this.uploadFile = uploadFile;
    }

    public Integer getDownloadFile() {
        return downloadFile;
    }

    public void setDownloadFile(Integer downloadFile) {
        this.downloadFile = downloadFile;
    }

    public Integer getAccountOperation() {
        return accountOperation;
    }

    public void setAccountOperation(Integer accountOperation) {
        this.accountOperation = accountOperation;
    }

    public Integer getAccountInfoOperation() {
        return accountInfoOperation;
    }

    public void setAccountInfoOperation(Integer accountInfoOperation) {
        this.accountInfoOperation = accountInfoOperation;
    }

    public Integer getFileOperation() {
        return fileOperation;
    }

    public void setFileOperation(Integer fileOperation) {
        this.fileOperation = fileOperation;
    }

}
