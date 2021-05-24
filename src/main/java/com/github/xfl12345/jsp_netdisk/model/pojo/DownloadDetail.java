package com.github.xfl12345.jsp_netdisk.model.pojo;

import com.github.xfl12345.jsp_netdisk.model.pojo.database.DirFile;
import com.github.xfl12345.jsp_netdisk.model.pojo.database.TbFile;

import java.io.File;
import java.util.Date;

public class DownloadDetail {
    public TbFile tbFile;
    public File fileInDisk;

    public Date linkCreateDate = new Date();
    public Date linkExpireDate;

    private long downloadCount = 0;

    public synchronized void addDownloadCount(){
        downloadCount++;
    }

    public long getDownloadCount(){
        return downloadCount;
    }

}
