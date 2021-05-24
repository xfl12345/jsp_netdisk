package com.github.xfl12345.jsp_netdisk.model.service.api;

import com.alibaba.fastjson.JSONObject;
import com.github.xfl12345.jsp_netdisk.StaticSpringApp;
import com.github.xfl12345.jsp_netdisk.appconst.api.request.JsonApiRequestField;
import com.github.xfl12345.jsp_netdisk.appconst.api.result.JsonApiResult;
import com.github.xfl12345.jsp_netdisk.model.pojo.api.request.BaseRequestObject;
import com.github.xfl12345.jsp_netdisk.model.pojo.api.response.JsonCommonApiResponseObject;
import com.github.xfl12345.jsp_netdisk.model.service.EmailVerificationService;
import com.github.xfl12345.jsp_netdisk.model.service.AccountService;
import com.github.xfl12345.jsp_netdisk.model.service.FileService;
import com.github.xfl12345.jsp_netdisk.model.service.TbPermissionService;
import com.github.xfl12345.jsp_netdisk.model.utility.JsonRequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service("jsonCommonApiService")
public class JsonCommonApiService {

    private final Logger logger = LoggerFactory.getLogger(JsonCommonApiService.class);

    @Autowired
    private AccountService accountService;

    @Autowired
    private TbPermissionService tbPermissionService;

    @Autowired
    private EmailVerificationService emailVerificationService;

    @Autowired
    private FileService fileService;

    //未定义版本号时，指定一个版本号
    public static final String undefinedVersion = "0";

    public static final String jsonApiVersion = "1";

    /**
     * 统一的JSON API获取结果必调函数
     *
     * @param request 包含JSON字符串的HTTP请求
     * @param call    格式化JSON字符串成功之后执行的函数
     * @return 返回 通用的 JSON API结果
     */
    public JsonCommonApiResponseObject resolver(
            HttpServletRequest request,
            JsonCommonApiCall call) {
        JsonCommonApiResponseObject responseObject = null;
        JSONObject requestObject = null;
        String jsonStr = null;
        try {
            jsonStr = JsonRequestUtils.getRequestString(request);
            StaticSpringApp.baseRequestObjectChecker.check(jsonStr);
            requestObject = JsonRequestUtils.getJsonObject(jsonStr);
        } catch (Exception e) {
            logger.error("Invaild request content,EOF>>>" + jsonStr + "\nEOF,error=" + e);
            responseObject = new JsonCommonApiResponseObject();
            responseObject.setApiResult(JsonApiResult.FAILED_REQUEST_FORMAT_ERROR);
            return responseObject;
        }
        try {
            responseObject = call.getResult(request, requestObject);
        } catch (Exception e) {
            logger.error(e.toString());
            responseObject = new JsonCommonApiResponseObject();
            responseObject.setApiResult(JsonApiResult.OTHER_FAILED);
        }
        return responseObject;
    }

    /**
     * 统一的JSON API获取结果必调函数
     *
     * @param request 包含JSON字符串的HTTP请求
     * @return 返回 通用的 JSON API结果
     */
    public JsonCommonApiResponseObject defaultResolver(HttpServletRequest request) {
        JsonCommonApiResponseObject responseObject = null;
        JSONObject jsonObject = null;
        BaseRequestObject requestObject = null;
        String jsonStr = null;
        try {
            jsonStr = JsonRequestUtils.getRequestString(request);
            StaticSpringApp.baseRequestObjectChecker.check(jsonStr);
            jsonObject = JsonRequestUtils.getJsonObject(jsonStr);
            requestObject = jsonObject.toJavaObject(BaseRequestObject.class);
            StaticSpringApp.baseRequestObjectChecker.check(requestObject.data.toJSONString());
        } catch (Exception e) {
            logger.error("Invaild request content,EOF>>>" + jsonStr + "\nEOF,error=" + e);
            responseObject = new JsonCommonApiResponseObject();
            responseObject.setApiResult(JsonApiResult.FAILED_REQUEST_FORMAT_ERROR);
            return responseObject;
        }
        try {
            //兼容旧API版本的请求
            switch (requestObject.version) {
                //当前版本（最新版本）
                case jsonApiVersion:
                    //选择操作
                    switch (requestObject.operation) {
                        //访问账号功能
                        case JsonApiRequestField.Operation.account:
                            responseObject = accountService.getResult(request, requestObject.data);
                            break;
                        //访问文件功能
                        case JsonApiRequestField.Operation.file:
                            responseObject = fileService.getResult(request, requestObject.data);
                            break;
                        default:
                            responseObject = getInvalidResponseObject();
                            break;
                    }
                    break;
                default:
                    responseObject = getInvalidResponseObject();
                    break;
            }
        } catch (Exception e) {
            logger.error(e.toString());
            responseObject = new JsonCommonApiResponseObject();
            responseObject.setApiResult(JsonApiResult.OTHER_FAILED);
        }
        return responseObject;
    }

    private JsonCommonApiResponseObject getInvalidResponseObject() {
        JsonCommonApiResponseObject responseObject = new JsonCommonApiResponseObject();
        responseObject.setApiResult(JsonApiResult.FAILED_INVALID);
        return responseObject;
    }

}
