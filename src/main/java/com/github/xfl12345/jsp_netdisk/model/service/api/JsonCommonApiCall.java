package com.github.xfl12345.jsp_netdisk.model.service.api;

import com.alibaba.fastjson.JSONObject;
import com.github.xfl12345.jsp_netdisk.model.pojo.api.request.BaseRequestObject;
import com.github.xfl12345.jsp_netdisk.model.pojo.api.response.JsonCommonApiResponseObject;

import javax.servlet.http.HttpServletRequest;

public interface JsonCommonApiCall {

    /**
     * 检查完JSON数据结构是否合法之后，调用具体的业务函数
     */
    JsonCommonApiResponseObject getResult(HttpServletRequest request, JSONObject object);
}
