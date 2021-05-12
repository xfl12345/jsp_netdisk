package com.github.xfl12345.jsp_netdisk.appconst.api.result;

public enum LogoutApiResult {
    SUCCEED("注销成功",0),
    FAILED("注销失败",1),
    OTHER_FAILED("未知错误",2),
    NO_LOGIN("未登录",3),
    SESSION_EXPIRE("会话过期，已注销",4);
    private final String name;
    private final int num;

    LogoutApiResult(String str, int num){
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
