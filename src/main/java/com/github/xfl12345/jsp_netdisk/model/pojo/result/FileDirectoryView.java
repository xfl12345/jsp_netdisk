package com.github.xfl12345.jsp_netdisk.model.pojo.result;

import com.github.xfl12345.jsp_netdisk.model.pojo.database.DirFile;
import com.github.xfl12345.jsp_netdisk.model.pojo.database.TbDirectory;
import com.github.xfl12345.jsp_netdisk.model.pojo.database.TbFile;
import com.github.xfl12345.jsp_netdisk.model.pojo.database.UserFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 用户浏览文件的视图所需数据
 */
public class FileDirectoryView extends DirectoryView {
    public HashMap<String, UserFile> files = new HashMap<>();

    public void setDirectoryView(DirectoryView directoryView){
        this.currDirectory = directoryView.currDirectory;
        this.directories = directoryView.directories;
        this.printWorkDirector = directoryView.printWorkDirector;
    }
}
