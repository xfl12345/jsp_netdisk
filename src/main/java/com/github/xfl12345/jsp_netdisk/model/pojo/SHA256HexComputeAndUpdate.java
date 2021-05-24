package com.github.xfl12345.jsp_netdisk.model.pojo;

import com.github.xfl12345.jsp_netdisk.StaticSpringApp;
import com.github.xfl12345.jsp_netdisk.model.pojo.database.TbFile;
import com.github.xfl12345.jsp_netdisk.model.service.TbFileService;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public class SHA256HexComputeAndUpdate extends Thread{
    private final Logger logger = LoggerFactory.getLogger(SHA256HexComputeAndUpdate.class);

    private final InputStream inputStream;
    private final TbFile tbFile;
    private String result;

    public SHA256HexComputeAndUpdate(InputStream inputStream, TbFile tbFile){
        this.inputStream = inputStream;
        this.tbFile = tbFile;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        try {
            result = DigestUtils.sha256Hex(inputStream);
            TbFileService tbFileService = StaticSpringApp.getBean(TbFileService.class);
            tbFile.setFileHashSha256(result);
            try {
                tbFileService.update(tbFile);
            }
            catch (Exception e){
                logger.error("可能重复插入了同一个文件，请检查程序设计逻辑"
                        + ",file_id=" + tbFile.getFileId()
                        + ",file_name=" + tbFile.getFileName()
                        + ",file_size=" + tbFile.getFileSize()
                        + ",file_hash_md5=" + tbFile.getFileHashMd5()
                        + ",file_hash_sha256=" + tbFile.getFileHashSha256()
                        + ",error=" + e);
            }
        } catch (IOException exception) {
            logger.error(exception.toString());
        }
    }

    public String getHexStrHashResult(){
        return result;
    }

}
