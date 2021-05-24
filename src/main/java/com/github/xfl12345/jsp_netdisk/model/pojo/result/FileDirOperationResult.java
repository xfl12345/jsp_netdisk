package com.github.xfl12345.jsp_netdisk.model.pojo.result;

import com.github.xfl12345.jsp_netdisk.model.pojo.database.DirFile;
import com.github.xfl12345.jsp_netdisk.model.pojo.database.TbDirectory;
import com.github.xfl12345.jsp_netdisk.model.pojo.database.TbFile;

import java.util.HashMap;

public class FileDirOperationResult {
    public long fileSuccessCount = 0L;
    public long fileFailCount = 0L;
    public long directorySuccessCount = 0L;
    public long directoryFailCount = 0L;

    public HashMap<String, UserFileOperationResult> files = new HashMap<>();
    public HashMap<String, DirOperationResult> directories = new HashMap<>();
}
