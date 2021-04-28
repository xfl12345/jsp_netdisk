package pers.xfl.jsp_netdisk.model.appconst;

public enum RegisterApiResult {
    SUCCEED("注册成功",0),
    FAILED_USERNAME_EXIST("用户名已被注册",1),
    FAILED_EMAIL_EXIST("邮箱已被注册",2),
    ILLEGAL_USERNAME("用户名格式错误",3),
    ILLEGAL_PASSWORD("密码不符合要求",4),
    ILLEGAL_GENDER("性别数据异常",5),
    ILLEGAL_EMAIL("邮箱格式错误",6),
    OTHER_FAILED("未知错误",-1);

    private final String name;
    private final long num;

    RegisterApiResult(String str, long num){
        this.name = str;
        this.num = num;
    }
    public String getName() {
        return name;
    }

    public long getNum() {
        return num;
    }

}
