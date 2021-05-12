package com.github.xfl12345.jsp_netdisk.appconst.api.result;

import org.springframework.http.HttpStatus;

public enum SentEmailApiResult {
    SUCCEED("成功发送电子邮件",HttpStatus.ACCEPTED.value()),
    FAILED_TODAY_MAX("今日该电子邮箱已达发送次数上限",HttpStatus.LOCKED.value()),
    FAILED_FREQUENCY_MAX("操作过于频繁", HttpStatus.TOO_MANY_REQUESTS.value()),
    FAILED_PERMISSION_DENIED("无权限发送电子邮件",HttpStatus.UNAUTHORIZED.value()),
    OTHER_FAILED("未知错误",-1);

    private final String name;
    private final int num;

    SentEmailApiResult(String str, int num){
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
