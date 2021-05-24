/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.xfl12345.jsp_netdisk.model.utility;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FilenameUtils;

/**
 * @author xfl666
 */
public class MyStrIsOK {
    public static final String matchLetterAndDigitOnly = "^[a-z0-9A-Z]+$";
    public static final String matchLetterOnly = "^[a-zA-Z]+$";
    public static final String matchDigitOnly = "^[0-9]+$";
    public static final String matchNumWithSignOnly = "[+-]?[1-9]+[0-9]*(\\.[0-9]+)?";
    public static final String matchEmailOnly = "([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+";
    public static final String matchFilename = "[^\\s\\\\/:\\*\\?\\\"<>\\|](\\x20|[^\\s\\\\/:\\*\\?\\\"<>\\|])*[^\\s\\\\/:\\*\\?\\\"<>\\|\\.]$";

    public static final Pattern containUppercaseLetter = Pattern.compile("[A-Z]");
    public static final Pattern containLowercaseLetter = Pattern.compile("[a-z]");
    public static final Pattern containLetter = Pattern.compile("[a-zA-Z]");
    public static final Pattern containNum = Pattern.compile("\\d");
    public static final Pattern containLetterAndDigit = Pattern.compile("[a-z0-9A-Z]");

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

    public static boolean arrIsOK(ArrayList<String> al) {
        boolean flag = true;
        for (String s : al) {
            if (s == null) {
                flag = false;
                break;
            } else if ( ! "".equals(s)) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    public static boolean isLetterDigitOnly(String str) {
        if( isEmpty(str) )
            return false;
        return str.matches(matchLetterAndDigitOnly);
    }

    public static boolean isLetterOnly(String str) {
        if( isEmpty(str) )
            return false;
        return str.matches(matchLetterOnly);
    }

    public static boolean isDigitOnly(String str) {
        if( isEmpty(str) )
            return false;
        return str.matches(matchDigitOnly);
    }

    public static boolean isEmail(String str){
        if( isEmpty(str) )
            return false;
        return str.matches(matchEmailOnly);
    }


    public static boolean isContainUppercaseLetter(String str){
        if( isEmpty(str) )
            return false;
        return containUppercaseLetter.matcher(str).find();
    }

    public static boolean isContainLowercaseLetter(String str){
        if( isEmpty(str) )
            return false;
        return containLowercaseLetter.matcher(str).find();
    }

    public static boolean isContainNum(String str){
        if( isEmpty(str) )
            return false;
        return containNum.matcher(str).find();
    }

    public static boolean isContainAllowedSpecialCharacter(String str) {
        if( isEmpty(str) )
            return false;
        return containAllowedSpecialCharacter.matcher(str).find();
    }

    public static boolean isContainChineseInUTF8(String str){
        if( isEmpty(str) )
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
     * 去除字符串中的英文字母和阿拉伯数字
     * @param str 给我一个字符串
     * @return 还你一个没有英文字母、没有阿拉伯数字的字符串
     */
    public static String removeLetterAndDigit(String str){
        return containLetterAndDigit.matcher(str).replaceAll("").trim();
    }

    /**
     * 去除字符串中的 合法的特殊字符
     * @param str 给我一个字符串
     * @return 还你一个没有合法的特殊字符的字符串
     */
    public static String removeAllowedSpecialCharacter(String str){
        return containAllowedSpecialCharacter.matcher(str).replaceAll("").trim();
    }

    /**
     * 从字符串里提取邮箱地址
     * @param str 输入字符串
     * @return 以数组的形式，返回str字符串中包含的所有邮箱地址
     */
    public static ArrayList<String> getEmailFromString(String str){
        ArrayList<String> email = new ArrayList<>();
        Pattern p = Pattern.compile(matchEmailOnly);
        Matcher m = p.matcher(str);
        while(m.find()) {
            email.add(m.group());
        }
        return email;
    }

    /**
     * 判断一个文件名是否合法（目录名称通用）
     * @param filename 文件名
     * @return 是否合法
     */
    public static boolean isValidFileName(String filename) {
        if (filename == null || "".equals(filename) || filename.length() > 255)
            return false;
        else
            return filename.matches(matchFilename);
    }

}
