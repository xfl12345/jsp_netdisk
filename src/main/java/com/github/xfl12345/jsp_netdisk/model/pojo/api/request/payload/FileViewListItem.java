package com.github.xfl12345.jsp_netdisk.model.pojo.api.request.payload;

import com.github.xfl12345.jsp_netdisk.model.pojo.database.DirFile;
import com.github.xfl12345.jsp_netdisk.model.pojo.database.TbDirectory;
import com.github.xfl12345.jsp_netdisk.model.service.DirFileService;
import com.github.xfl12345.jsp_netdisk.model.service.TbDirectoryService;

public class FileViewListItem {
    private Long accountId;

    private Long directoryId;

    private Long fileId;

    private String userCustomName;

    private String extendName;

    private Long fileSize;

    private Boolean itemIsDir;

    private String sha256Hex;

    private String md56Hex;

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

    public String getUserCustomName() {
        return userCustomName;
    }

    public void setUserCustomName(String userCustomName) {
        this.userCustomName = userCustomName;
    }

    public String getExtendName() {
        return extendName;
    }

    public void setExtendName(String extendName) {
        this.extendName = extendName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Boolean getItemIsDir() {
        return itemIsDir;
    }

    public void setItemIsDir(Boolean dir) {
        itemIsDir = dir;
    }

    public String getSha256Hex() {
        return sha256Hex;
    }

    public void setSha256Hex(String sha256Hex) {
        this.sha256Hex = sha256Hex;
    }

    public String getMd56Hex() {
        return md56Hex;
    }

    public void setMd56Hex(String md56Hex) {
        this.md56Hex = md56Hex;
    }

    public DirFile toDirFile() throws Exception {
        DirFile dirFile = new DirFile();
        dirFile.setAccountId(this.accountId);
        dirFile.setDirectoryId(this.directoryId);
        dirFile.setFileId(this.fileId);
        dirFile.setUserCustomFileName(this.userCustomName);
        if(dirFile.isAllFieldNotNull())
            return  dirFile;
        else
            throw new Exception("文件关联对象存在空值");
    }

    public DirFile toDirFile(DirFileService service) throws Exception {
        DirFile dirFile = toDirFile();
        long count = service.countAll(dirFile);
        if(count == 0)
            throw new Exception("文件关联对象存在空值");
        else if(count > 1)
            throw new Exception("文件关联对象 的数据存在注入攻击");
        return dirFile;
    }

    public TbDirectory toTbDirectory(TbDirectoryService service) throws Exception {
        TbDirectory tbDirectory = service.queryById(this.directoryId);
        if(tbDirectory == null)
            throw new Exception("目录不存在");
        else
            return  tbDirectory;
    }

}
