package com.github.xfl12345.jsp_netdisk.model.pojo.database;

import java.io.Serializable;

/**
 * (DirFile)实体类
 *
 * @author makejava
 * @since 2021-04-19 16:00:51
 */
public class DirFile implements Serializable {
    private static final long serialVersionUID = -98512128020992632L;
    /**
     * 目录ID
     */
    private Long directoryId;
    /**
     * 目录下的文件
     */
    private Long fileId;


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

}
