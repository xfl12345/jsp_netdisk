import pers.xfl.jsp_netdisk.model.utils.MyStrIsOK;
import pers.xfl.jsp_netdisk.model.utils.check.RegisterParamChecker;

public class PatternMatchTest {
    public static void main(String[] args) {
        String[] strArr = {"零0","1","01",
                "012345","0123abc","abc0123",
                "abc0123abc","123abc0123","helloworld",
                "九sss","十emmm","十一asdasdas...sdadasd",
                "十二asdasd12312312.1213asdasd","十三...123asdas", "十四.",
                "十五[","十六]\\", "十七///dsada",
                "十八ad12ed\\asd123231","十九dasdaAA11e1312\\"};
        String password = "github@xfl12345";

        System.out.println("isOnlyDigit test");
        for (int i = 0; i < strArr.length; i++) {
            System.out.println(i+":"+MyStrIsOK.isDigitOnly(strArr[i]));
        }

        System.out.println("isOnlyLetter test");
        for (int i = 0; i < strArr.length; i++) {
            System.out.println(i+":"+MyStrIsOK.isLetterOnly(strArr[i]));
        }

        System.out.println("isLetterDigit test");
        for (int i = 0; i < strArr.length; i++) {
            System.out.println(i+":"+MyStrIsOK.isLetterDigitOnly(strArr[i]));
        }

        System.out.println("isContainNum test");
        for (int i = 0; i < strArr.length; i++) {
            System.out.println(i+":"+MyStrIsOK.isContainNum(strArr[i]));
        }

        System.out.println("isContainUppercaseLetter test");
        for (int i = 0; i < strArr.length; i++) {
            System.out.println(i+":"+MyStrIsOK.isContainUppercaseLetter(strArr[i]));
        }

        System.out.println("isContainLowercaseLetter test");
        for (int i = 0; i < strArr.length; i++) {
            System.out.println(i+":"+MyStrIsOK.isContainLowercaseLetter(strArr[i]));
        }

        System.out.println("isContainAllowedSpecialCharacter test");
        for (int i = 0; i < strArr.length; i++) {
            System.out.println(i+":"+MyStrIsOK.isContainAllowedSpecialCharacter(strArr[i]));
        }

        System.out.println("isContainChineseInUTF8 test");
        for (int i = 0; i < strArr.length; i++) {
            System.out.println(i+":"+MyStrIsOK.isContainChineseInUTF8(strArr[i]));
        }

        System.out.println("removeNum test");
        for (int i = 0; i < strArr.length; i++) {
            System.out.println(i+":"+MyStrIsOK.removeNum(strArr[i]));
        }

        System.out.println("removeLowercaseLetter test");
        for (int i = 0; i < strArr.length; i++) {
            System.out.println(i+":"+MyStrIsOK.removeLowercaseLetter(strArr[i]));
        }

        System.out.println("removeLetter test");
        for (int i = 0; i < strArr.length; i++) {
            System.out.println(i+":"+MyStrIsOK.removeLetter(strArr[i]));
        }


        System.out.println("test password string");
        System.out.println(RegisterParamChecker.isPasswordComplexityEnough(password));
        System.out.println(RegisterParamChecker.isPasswordUnderLegal(password));

        System.out.println("test email string");
        System.out.println(MyStrIsOK.isEmail("111@qq.com"));

    }
}
