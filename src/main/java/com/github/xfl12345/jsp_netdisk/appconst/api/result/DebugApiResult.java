package com.github.xfl12345.jsp_netdisk.appconst.api.result;

public enum DebugApiResult {
    SUCCEED("请求成功",0),
    OTHER_FAILED("未知错误",-1);

    private final String name;
    private final int num;

    DebugApiResult(String str, int num){
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
