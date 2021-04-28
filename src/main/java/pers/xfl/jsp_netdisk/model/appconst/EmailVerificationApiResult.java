package pers.xfl.jsp_netdisk.model.appconst;

public enum EmailVerificationApiResult {
    SUCCEED("成功发送电子邮件",0),
    ILLEGAL_EMAIL("邮箱格式错误",6),
    OTHER_FAILED("未知错误",-1);

    private final String name;
    private final long num;

    EmailVerificationApiResult(String str, long num){
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
