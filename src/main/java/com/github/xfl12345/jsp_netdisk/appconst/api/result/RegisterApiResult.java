package com.github.xfl12345.jsp_netdisk.appconst.api.result;

public enum RegisterApiResult {
    SUCCEED("注册成功",0),
    FAILED_USERNAME_EXIST("用户名已被注册",1),
    FAILED_EMAIL_EXIST("邮箱已被注册",2),
    ILLEGAL_USERNAME("用户名格式错误",3),
    ILLEGAL_PASSWORD("密码不符合要求",4),
    ILLEGAL_GENDER("性别数据异常",5),
    ILLEGAL_EMAIL("邮箱格式错误",6),
    OTHER_FAILED("未知错误",-1),

    FAILED_TODAY_MAX("今日该电子邮箱已达发送次数上限",11),
    FAILED_FREQUENCY_MAX("操作过于频繁",12);

    private final String name;
    private final int num;

    RegisterApiResult(String str, int num){
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
