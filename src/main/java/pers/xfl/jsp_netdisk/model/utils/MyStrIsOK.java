/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pers.xfl.jsp_netdisk.model.utils;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * @author xfl666
 */
public class MyStrIsOK {
    public static final String matchLetterAndDigitOnly = "^[a-z0-9A-Z]+$";
    public static final String matchLetterOnly = "^[a-zA-Z]+$";
    public static final String matchDigitOnly = "^[0-9]+$";
    public static final String matchNumWithSignOnly = "[+-]?[1-9]+[0-9]*(\\.[0-9]+)?";
    public static final String matchEmailOnly = "([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+";

    public static final Pattern containUppercaseLetter = Pattern.compile("[A-Z]");
    public static final Pattern containLowercaseLetter = Pattern.compile("[a-z]");
    public static final Pattern containLetter = Pattern.compile("[a-zA-Z]");
    public static final Pattern containNum = Pattern.compile("\\d");

    /**
     * 匹配如下特殊符号
     * ( ) ` ~ ! @ # $ % ^ & * - _ + = | { } [ ] : ; ' < > , . ? /
     */
    public static final Pattern containAllowedSpecialCharacter = Pattern.compile("[`~!@#$%^&*()+=|{}':;,\\[\\].\\\\<>/?—]");
    public static final Pattern containChineseInUTF8 = Pattern.compile("[\u4e00-\u9fa5]");

    public static boolean isNotEmpty(String str) {
        return str != null && !str.isEmpty();
    }

    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static boolean arrIsOK(ArrayList al) {
        boolean flag = true;
        for (int n = 0; n < al.size(); n++) {
            String str = (String) al.get(n);
            if (str == null) {
                flag = false;
                break;
            } else if (!"".equals(str)) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    public static boolean isLetterDigitOnly(String str) {
        if( isEmpty(str))
            return false;
        return str.matches(matchLetterAndDigitOnly);
    }

    public static boolean isLetterOnly(String str) {
        if( isEmpty(str))
            return false;
        return str.matches(matchLetterOnly);
    }

    public static boolean isDigitOnly(String str) {
        if( isEmpty(str))
            return false;
        return str.matches(matchDigitOnly);
    }

    public static boolean isEmail(String str){
        if( isEmpty(str))
            return false;
        return str.matches(matchEmailOnly);
    }


    public static boolean isContainUppercaseLetter(String str){
        if( isEmpty(str))
            return false;
        return containUppercaseLetter.matcher(str).find();
    }

    public static boolean isContainLowercaseLetter(String str){
        if( isEmpty(str))
            return false;
        return containLowercaseLetter.matcher(str).find();
    }

    public static boolean isContainNum(String str){
        if( isEmpty(str))
            return false;
        return containNum.matcher(str).find();
    }

    public static boolean isContainAllowedSpecialCharacter(String str) {
        if( isEmpty(str))
            return false;
        return containAllowedSpecialCharacter.matcher(str).find();
    }

    public static boolean isContainChineseInUTF8(String str){
        if( isEmpty(str))
            return false;
        return containChineseInUTF8.matcher(str).find();
    }

    /**
     * 去除字符串中的数字
     * @param str 给我一个字符串
     * @return 还你一个没有数字的字符串
     */
    public static String removeNum(String str){
        return containNum.matcher(str).replaceAll("").trim();
    }

    /**
     * 去除字符串中的小写英文字母
     * @param str 给我一个字符串
     * @return 还你一个没有小写英文字母的字符串
     */
    public static String removeLowercaseLetter(String str){
        return containLowercaseLetter.matcher(str).replaceAll("").trim();
    }

    /**
     * 去除字符串中的大写英文字母
     * @param str 给我一个字符串
     * @return 还你一个没有大写英文字母的字符串
     */
    public static String removeUppercaseLetter(String str){
        return containUppercaseLetter.matcher(str).replaceAll("").trim();
    }

    /**
     * 去除字符串中的英文字母
     * @param str 给我一个字符串
     * @return 还你一个没有英文字母的字符串
     */
    public static String removeLetter(String str){
        return containLetter.matcher(str).replaceAll("").trim();
    }

    /**
     * 去除字符串中的 合法的特殊字符
     * @param str 给我一个字符串
     * @return 还你一个没有合法的特殊字符的字符串
     */
    public static String removeAllowedSpecialCharacter(String str){
        return containAllowedSpecialCharacter.matcher(str).replaceAll("").trim();
    }

}
