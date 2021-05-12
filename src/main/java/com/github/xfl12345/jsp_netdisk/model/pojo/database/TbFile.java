package com.github.xfl12345.jsp_netdisk.model.pojo.database;

import java.util.Date;
import java.io.Serializable;

/**
 * (TbFile)实体类
 *
 * @author makejava
 * @since 2021-04-19 16:00:53
 */
public class TbFile implements Serializable {
    private static final long serialVersionUID = -19312079077241905L;
    /**
     * 文件ID
     */
    private Long fileId;
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 文件大小
     */
    private Long fileSize;
    /**
     * 文件类型
     */
    private Long fileType;
    /**
     * 文件状态代码
     */
    private Integer fileStatus;
    /**
     * 文件上传时间
     */
    private Date fileUploadTime;
    /**
     * 文件MD5哈希值
     */
    private String fileHashMd5;
    /**
     * 文件SHA256哈希值
     */
    private String fileHashSha256;


    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Long getFileType() {
        return fileType;
    }

    public void setFileType(Long fileType) {
        this.fileType = fileType;
    }

    public Integer getFileStatus() {
        return fileStatus;
    }

    public void setFileStatus(Integer fileStatus) {
        this.fileStatus = fileStatus;
    }

    public Date getFileUploadTime() {
        return fileUploadTime;
    }

    public void setFileUploadTime(Date fileUploadTime) {
        this.fileUploadTime = fileUploadTime;
    }

    public String getFileHashMd5() {
        return fileHashMd5;
    }

    public void setFileHashMd5(String fileHashMd5) {
        this.fileHashMd5 = fileHashMd5;
    }

    public String getFileHashSha256() {
        return fileHashSha256;
    }

    public void setFileHashSha256(String fileHashSha256) {
        this.fileHashSha256 = fileHashSha256;
    }

}
