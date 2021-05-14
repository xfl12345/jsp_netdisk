package com.github.xfl12345.jsp_netdisk.model.pojo.api.response;

import com.github.xfl12345.jsp_netdisk.appconst.api.result.JsonApiResult;
import com.github.xfl12345.jsp_netdisk.model.service.api.JsonCommonApiService;

public class JsonCommonApiResponseObject extends BaseResponseObject {
    public int code;
    public Object data;

    public JsonCommonApiResponseObject() {
        this.version = JsonCommonApiService.undefinedVersion;//未定义版本号
    }

    public JsonCommonApiResponseObject(String apiVersion) {
        this.version = apiVersion;
    }

    public void setApiResult(JsonApiResult apiResult){
        this.success = apiResult.equals(JsonApiResult.SUCCEED);
        this.message = apiResult.getName();
    }

}
