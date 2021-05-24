package com.github.xfl12345.jsp_netdisk.model.pojo.result;

import com.alibaba.fastjson.JSONObject;
import com.github.xfl12345.jsp_netdisk.appconst.api.result.FileApiResult;
import com.github.xfl12345.jsp_netdisk.appconst.api.result.JsonApiResult;
import com.github.xfl12345.jsp_netdisk.model.pojo.api.response.BaseResponseObject;
import com.github.xfl12345.jsp_netdisk.model.pojo.api.response.JsonCommonApiResponseObject;
import com.github.xfl12345.jsp_netdisk.model.service.api.JsonCommonApiService;

public class DirectoryOperationResult {
    public int code;
    public FileDirectoryView currFileDirView = new FileDirectoryView();
    public FileApiResult apiResult;

    public DirectoryOperationResult(){}

    public void setApiResult(FileApiResult apiResult){
        this.apiResult = apiResult;
    }
    public FileApiResult getApiResult(FileApiResult apiResult){
        return apiResult;
    }

}
