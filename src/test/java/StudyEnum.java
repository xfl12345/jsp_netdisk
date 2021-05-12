import com.github.xfl12345.jsp_netdisk.appconst.api.result.LoginApiResult;

import java.util.Arrays;



public class StudyEnum {
    public static void main(String[] args) {

        System.out.println(Arrays.toString(LoginApiResult.values()));
        System.out.println(LoginApiResult.valueOf("SUCCEED"));
        System.out.println(LoginApiResult.SUCCEED);
        System.out.println(LoginApiResult.SUCCEED.name());
        System.out.println(LoginApiResult.SUCCEED.getName());

        System.out.println();
        TestEnum.SUCCEED.setNum(6666L);
        System.out.println(TestEnum.SUCCEED);
        System.out.println(TestEnum.SUCCEED.name);
        System.out.println(TestEnum.SUCCEED.getName());
        System.out.println(TestEnum.SUCCEED.num);
        System.out.println(TestEnum.SUCCEED.getNum());

//        TestEnum testEnum = new TestEnum("注册成功",666);

    }

    private enum TestEnum {
        SUCCEED("注册成功",0),
        OTHER_FAILED("未知错误",1);

        public String getName() {
            return name;
        }

        public long getNum() {
            return num;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setNum(long num) {
            this.num = num;
        }

        private String name;
        private long num;

        TestEnum(String str, long num){
            this.name = str;
            this.num = num;
        }
    }

}
