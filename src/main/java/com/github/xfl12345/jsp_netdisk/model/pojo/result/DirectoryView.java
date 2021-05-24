package com.github.xfl12345.jsp_netdisk.model.pojo.result;

import com.github.xfl12345.jsp_netdisk.model.pojo.database.TbDirectory;
import com.github.xfl12345.jsp_netdisk.model.pojo.database.TbFile;

import java.util.HashMap;
import java.util.List;

/**
 * 用户浏览文件的视图所需数据
 */
public class DirectoryView {
    public String printWorkDirector;
    public TbDirectory currDirectory;
    public HashMap<String, TbDirectory> directories = new HashMap<>();
}
