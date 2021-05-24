package com.github.xfl12345.jsp_netdisk.appconst.field;

public class FileOperationField {
    // 文件和文件夹（目录）共有功能
//    public static final String create = "create";
    public static final String delete = "delete";
    public static final String find = "find";
    public static final String changeName = "changeName";
    public static final String copy = "copy";
    public static final String move = "move";
    //上传下载文件（包括递归上传、下载）
    public static final String upload = "upload";
    public static final String download = "download";

    //文件专属功能
    public static final String getSHA256Hex = "getSHA256Hex";
    public static final String getMD5Hex = "getMD5Hex";

    //文件夹（目录）专属功能
    public static final String makeDirectory = "mkdir";
    public static final String listFiles = "ls";
    public static final String changeDirectory = "cd";
    public static final String printWorkDirector = "pwd";

}
