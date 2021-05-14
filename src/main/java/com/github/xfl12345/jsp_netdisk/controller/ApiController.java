package com.github.xfl12345.jsp_netdisk.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.xfl12345.jsp_netdisk.appconst.api.result.*;
import com.github.xfl12345.jsp_netdisk.model.dao.TbDirectoryDao;
import com.github.xfl12345.jsp_netdisk.model.dao.TbPermissionDao;
import com.github.xfl12345.jsp_netdisk.model.pojo.api.response.JsonCommonApiResponseObject;
import com.github.xfl12345.jsp_netdisk.model.pojo.database.TbAccount;
import com.github.xfl12345.jsp_netdisk.model.pojo.database.TbPermission;
import com.github.xfl12345.jsp_netdisk.model.pojo.api.response.BaseResponseObject;
import com.github.xfl12345.jsp_netdisk.model.service.EmailVerificationService;
import com.github.xfl12345.jsp_netdisk.model.service.TbAccountService;
import com.github.xfl12345.jsp_netdisk.model.service.TbPermissionService;
import com.github.xfl12345.jsp_netdisk.model.service.api.JsonCommonApiService;
import com.github.xfl12345.jsp_netdisk.model.utility.JsonRequestUtils;
import com.github.xfl12345.jsp_netdisk.model.utility.MyBatisSqlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping(value = "api", method = RequestMethod.POST)
public class ApiController {

    /**
     * version值即 API 版本号，ApiController 自身的属性，
     * 粒度细到一个Controller的版本号，方便部分API迭代更新
     */
    public static final int version = 1;

    private final Logger logger= LoggerFactory.getLogger(ApiController.class);

    @Autowired
    private TbAccountService tbAccountService;

    @Autowired
    private EmailVerificationService emailVerificationService;

    @Autowired
    private TbPermissionService tbPermissionService;

    @Autowired
    private TbDirectoryDao tbDirectoryDao;

    @Autowired
    private JsonCommonApiService jsonCommonApiService;

    /**
     * 对POST JSON API 请求，做出API响应
     * @param request http POST请求
     */
    @RequestMapping(value = "json", method = RequestMethod.POST)
    public void jsonApiAction(HttpServletRequest request, HttpServletResponse response){
        JsonCommonApiResponseObject responseObject = jsonCommonApiService.defaultResolver(request);
        JsonRequestUtils.responseObjectAsJsonStr(response, responseObject);
    }







    /**
     * 对POST debug请求，做出API响应
     * @param request http POST请求
     */
    @RequestMapping(value = "debug", method = RequestMethod.POST)
    public void debugAction(HttpServletRequest request, HttpServletResponse response){
        BaseResponseObject responseObject = new BaseResponseObject();
        responseObject.version = String.valueOf(version);
        responseObject.success = true;
        String msg = DebugApiResult.SUCCEED.getName();
        //格式化请求数据为JSON对象
        JSONObject requesetJsonObject = null;
        try {
            requesetJsonObject = JsonRequestUtils.getJsonObject(request);
        }
        catch (Exception e){
            msg = "请求格式错误！请使用JSON格式！";
            responseObject.message = msg;
            JsonRequestUtils.responseObjectAsJsonStr(response, responseObject);
            return;
        }

        try{
            TbAccount tbAccountResult = tbAccountService.queryById(66666L);
            if(tbAccountResult == null){
                logger.info("MyBatis查不到数据的时候，如果设定返回不是集合而是bean实体类，则返回的确实是null");
            }
            TbAccount tbAccount = new TbAccount();
            tbAccount.setAccountId(6666666L);
            List<TbAccount> tbAccountList = tbAccountService.queryAllByLimit(tbAccount, 0, 100);
            logger.info("tbAccountList.size()="+tbAccountList.size());
            TbPermission tbPermission = new TbPermission();
            tbPermission.setPermissionId(1L);
            logger.info(MyBatisSqlUtils.getSql("queryAll", tbPermission, TbPermissionDao.class));
            int affectedRowCount = tbPermissionService.insert(tbPermission);
//        TbDirectory tbDirectoryInsert = new TbDirectory();
//        tbDirectoryInsert.setParentDirectoryId(0L);
//        tbDirectoryInsert.setAccountId(1L);
//        tbDirectoryInsert.setDirectoryName("just4test");
//        logger.info("affected row count = " + tbDirectoryDao.insert(tbDirectoryInsert));
//        TbDirectory tbDirectory = new TbDirectory();
//        tbDirectory.setAccountId(1L);
//        tbDirectory.setParentDirectoryId(0L);
//        logger.info("count="+tbDirectoryDao.countAll(tbDirectory));
        }
        catch (Exception e){
            msg = DebugApiResult.OTHER_FAILED.getName();
            responseObject.success = false;
        }
        responseObject.message = msg;
        JsonRequestUtils.responseObjectAsJsonStr(response, responseObject);
    }


}
