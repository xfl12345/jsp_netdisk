package com.github.xfl12345.jsp_netdisk.model.pojo.database;


import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Date;

public class UserFile {
    /**
     * 账号ID
     */
    private Long accountId;
    /**
     * 目录ID
     */
    private Long directoryId;
    /**
     * 目录下的文件
     */
    private Long fileId;

    /**
     * 用户自定义的文件名
     */
    private String userCustomFileName;

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getDirectoryId() {
        return directoryId;
    }

    public void setDirectoryId(Long directoryId) {
        this.directoryId = directoryId;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getUserCustomFileName() {
        return userCustomFileName;
    }

    public void setUserCustomFileName(String userCustomFileName) {
        this.userCustomFileName = userCustomFileName;
    }

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
     * 文件MD5哈希值
     */
    private String fileHashMd5;
    /**
     * 文件SHA256哈希值
     */
    private String fileHashSha256;


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

    public boolean isAllFieldNotNull(){
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field f : fields) {
            try {
                //跳过final修饰的属性
                if (Modifier.isFinal(f.getModifiers()))
                    continue;
                if( f.get(this) == null )
                    return false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

}
