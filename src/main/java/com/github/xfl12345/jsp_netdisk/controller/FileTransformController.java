package com.github.xfl12345.jsp_netdisk.controller;

import com.github.xfl12345.jsp_netdisk.StaticSpringApp;
import com.github.xfl12345.jsp_netdisk.appconst.MyConst;
import com.github.xfl12345.jsp_netdisk.appconst.field.MySessionAttributes;
import com.github.xfl12345.jsp_netdisk.model.pojo.DownloadDetail;
import com.github.xfl12345.jsp_netdisk.model.pojo.api.response.JsonCommonApiResponseObject;
import com.github.xfl12345.jsp_netdisk.model.pojo.database.TbAccount;
import com.github.xfl12345.jsp_netdisk.model.service.FileService;
import com.github.xfl12345.jsp_netdisk.model.utility.JsonRequestUtils;
import com.github.xfl12345.jsp_netdisk.model.utility.MyStrIsOK;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class FileTransformController {

    private final Logger logger = LoggerFactory.getLogger(FileTransformController.class);

    @Autowired
    private FileService fileService;

    @RequestMapping(value = "thunderUpload", method = RequestMethod.POST)
    public void thunderUpload(
            @RequestParam("fileSize") Long givenFileSize,
            @RequestParam("md5Hex") String givenMD56Hex,
            @RequestParam("sha256Hex") String givenSHA256Hex,
            HttpServletRequest request,
            HttpServletResponse response) {

        if (givenFileSize == null ||
                MyStrIsOK.isEmpty(givenMD56Hex) ||
                MyStrIsOK.isEmpty(givenSHA256Hex)) {
            //TODO 待研发
        }

    }

    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public void normalUpload(
            @RequestParam("file") MultipartFile uploadFile,
            HttpServletRequest request,
            HttpServletResponse response) {
        StaticSpringApp.myReflectUtils.demo(uploadFile);
        JsonCommonApiResponseObject responseObject = new JsonCommonApiResponseObject(FileService.jsonApiVersion);
        fileService.setJsonApiResult(responseObject, fileService.upload(uploadFile, request.getSession()));
        JsonRequestUtils.responseObjectAsJsonStr(response, responseObject);
    }

    @RequestMapping(value = "download/{directoryId}/{name:.*}", method = RequestMethod.GET)
    public void download(@PathVariable("directoryId") Long directoryId,
                         @PathVariable("name") String name,
                         HttpServletRequest request,
                         HttpServletResponse response) throws Exception {
        //用于记录以完成的下载的数据量，单位是byte
        long sentLength = 0L;
        HttpSession session = request.getSession();
        ConcurrentHashMap<String, DownloadDetail> fileMap = null;
        TbAccount tbAccount = (TbAccount) session.getAttribute(MySessionAttributes.TB_ACCOUNT);
        //以匿名身份下载
        if (tbAccount == null) {
            fileMap = FileService.downloadLinkMap.get(MyConst.ANONYMOUS);
        }
        //以注册用户身份下载
        else {
            fileMap = FileService.downloadLinkMap.get(tbAccount.getAccountId().toString());
        }
        if (fileMap == null) {
            responseHttpStatus403(response);
        } else {
            DownloadDetail downloadDetail = fileMap.get(fileService.getDownloadDetailKey(directoryId.toString(), name));
            if (downloadDetail == null) {
                responseHttpStatus403(response);
            } else {
                downloadDetail.addDownloadCount();
                name = URLEncoder.encode(name, StandardCharsets.UTF_8.name());
                //设置响应头和客户端保存文件名
                response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                response.setContentType("multipart/form-data");
                String userAgent = request.getHeader(HttpHeaders.USER_AGENT);
                if (userAgent.indexOf("MSIE") > 0) {
                    response.setHeader("Content-Disposition", "attachment;name=" + name);
                } else {
                    response.setHeader("Content-Disposition", "attacher; filename*=UTF-8''" + name);
                }
                InputStream inputStream = null;
                OutputStream outputStream = null;
                try {
                    //打开本地文件流
                    inputStream = new FileInputStream(downloadDetail.fileInDisk);
                    //激活下载操作
                    outputStream = response.getOutputStream();

                    //循环写入输出流
                    byte[] b = new byte[2048];
                    int length;
                    while ((length = inputStream.read(b)) > 0) {
                        outputStream.write(b, 0, length);
                        sentLength += b.length;
                    }

                } catch (Exception e) {
                    logger.error(e.toString());
                }
                try {
                    //养成好习惯，关闭所有流
                    if (outputStream != null) {
                        outputStream.close();
                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                }
                catch (IOException exception) {
                    logger.error(exception.toString());
                }

                logger.debug("Download mission : sessionId=" + session.getId()
                        + ",directoryId=" + directoryId
                        + ",name" + name
                        + ",total sent size=" + sentLength);
            }
        }

    }

    public void responseHttpStatus403(HttpServletResponse response) {
        response.setStatus(HttpStatus.FORBIDDEN.value());
    }

}
