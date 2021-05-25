package com.github.xfl12345.jsp_netdisk.model.pojo.database;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * (DirFile)实体类
 *
 * @author makejava
 * @since 2021-04-19 16:00:51
 */
public class DirFile implements Serializable, Cloneable {
    private static final long serialVersionUID = -98512128020992632L;
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

    @Override
    public DirFile clone(){
        Object obj=null;
        //调用Object类的clone方法，返回一个Object实例
        try {
            obj= super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return (DirFile) obj;
    }

}
