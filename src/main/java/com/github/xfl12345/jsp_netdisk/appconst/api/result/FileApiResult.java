package com.github.xfl12345.jsp_netdisk.appconst.api.result;

import org.springframework.http.HttpStatus;

public enum FileApiResult {
    SUCCEED("操作成功", HttpStatus.OK.value()),
    FAILED_NAME_ILLEGAL("名称非法",1),
    FAILED_FREQUENCY_MAX("操作过于频繁",HttpStatus.TOO_MANY_REQUESTS.value()),
    FAILED_PERMISSION_DENIED("无权操作",HttpStatus.FORBIDDEN.value()),
    FAILED_REQUEST_FORMAT_ERROR("请求数据格式错误", HttpStatus.UNPROCESSABLE_ENTITY.value()),
    FAILED_NO_LOGIN("未登录",HttpStatus.UNAUTHORIZED.value()),
    OTHER_FAILED("未知错误",HttpStatus.INTERNAL_SERVER_ERROR.value());

    private final String name;
    private final int num;

    FileApiResult(String str, int num){
        this.name = str;
        this.num = num;
    }
    public String getName() {
        return name;
    }

    public int getNum() {
        return num;
    }
}
