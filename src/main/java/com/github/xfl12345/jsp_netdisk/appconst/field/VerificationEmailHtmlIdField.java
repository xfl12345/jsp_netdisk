package com.github.xfl12345.jsp_netdisk.appconst.field;

import com.github.xfl12345.jsp_netdisk.model.pojo.database.TbAccount;

public class VerificationEmailHtmlIdField {
    /**
     * 邮件正文内容的ID，getElementById就能获得完整的正文内容
     * 不包括标题，不包括正文div以外的任何东西
     */
    public final static String contentMainBodyId = "contentMainBody";
    /**
     * 内容标题，不是电子邮件的属性，是包含在HTML body文本里的东西
     */
    public final static String contentTitleId = "contentTitle";
    /**
     * 网站名称
     */
    public final static String websiteNameId = "websiteName";
    /**
     * 正文内容里的其它关键信息元素的ID名称
     */
    public static final class ContentId{
        /**
         * TBAccount里的账号ID字段
         */
        public static final String accountId = "accountId";
        /**
         * TBAccount里的用户名字段
         */
        public static final String username = "username";
        public static final String email = "email";
        /**
         * 邮箱验证码
         */
        public final static String verificationCode = "verificationCode";

    }
}
