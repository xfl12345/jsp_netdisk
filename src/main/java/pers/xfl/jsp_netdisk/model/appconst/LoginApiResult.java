package pers.xfl.jsp_netdisk.model.appconst;

public enum LoginApiResult {
    SUCCEED("登录成功",0),
    FAILED("用户名或密码错误",1),
    OTHER_FAILED("未知错误",2),
    DUPLICATE_KEY("账号已被他人登录",3);
    private final String name;
    private final long num;

    LoginApiResult(String str, long num){
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
