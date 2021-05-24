package com.github.xfl12345.jsp_netdisk.appconst.api.result;

import org.springframework.http.HttpStatus;

public enum JsonApiResult {
    SUCCEED("成功", HttpStatus.OK.value()),
    FAILED_INVALID("参数无效", HttpStatus.FORBIDDEN.value()),
    FAILED_NOT_SUPPORT("操作不支持", HttpStatus.FORBIDDEN.value()),
    FAILED_FREQUENCY_MAX("操作过于频繁", HttpStatus.TOO_MANY_REQUESTS.value()),
    FAILED_NO_LOGIN("未登录", HttpStatus.UNAUTHORIZED.value()),
    FAILED_REQUEST_FORMAT_ERROR("请求数据格式错误", HttpStatus.UNPROCESSABLE_ENTITY.value()),
    OTHER_FAILED("未知错误", HttpStatus.INTERNAL_SERVER_ERROR.value());

    private final String name;
    private final int num;

    JsonApiResult(String str, int num) {
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
