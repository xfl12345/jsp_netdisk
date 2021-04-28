package pers.xfl.jsp_netdisk.model.utils.check;

import pers.xfl.jsp_netdisk.model.appconst.MyConst;
import pers.xfl.jsp_netdisk.model.utils.MyStrIsOK;

public class RegisterParamChecker {
    /**
     * 检查密码是否符合一定的复杂度
     * @param passwordStr 密码
     * @return 是否达标
     */
    public static boolean isPasswordComplexityEnough(String passwordStr){
        int okCount = 0;
        if(MyStrIsOK.isContainNum(passwordStr))
            okCount++;
        if(MyStrIsOK.isContainUppercaseLetter(passwordStr))
            okCount++;
        if(MyStrIsOK.isContainLowercaseLetter(passwordStr))
            okCount++;
        if(MyStrIsOK.isContainAllowedSpecialCharacter(passwordStr))
            okCount++;
        return okCount >= 3;
    }

    public static boolean isPasswordUnderLegal(String str){
        String tmpStr = str;
        tmpStr = MyStrIsOK.removeLetter( MyStrIsOK.removeNum(tmpStr) );
        tmpStr = MyStrIsOK.removeAllowedSpecialCharacter(tmpStr);
        return tmpStr.equals("");
    }

    public static boolean isUsernameUnderLegal(String str){
        String tmpStr = str;
        tmpStr = MyStrIsOK.removeLetter( MyStrIsOK.removeNum(tmpStr) );
        return ! MyStrIsOK.isContainAllowedSpecialCharacter(str);
    }

    public static boolean isGenderUnderLegal(String gender){
        return MyStrIsOK.isDigitOnly(gender) && (gender.length() == MyConst.tbAccountGenderLength);
    }

    public static boolean isEmailUnderLegal(String email){
        return (email.equals("")) || MyStrIsOK.isEmail(email);
    }

}
