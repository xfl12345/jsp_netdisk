package com.github.xfl12345.jsp_netdisk.model.service;

import javax.mail.internet.MimeMessage;

public interface IEmailSentCallback {

    /**
     * 发送邮件的结果
     * @param message 最终邮件内容（实际发送邮件内容）
     * @param result 发送邮件的结果（成功了还是失败了）
     * @param cause 错误原因
     */
    void  afterSent(MimeMessage message, boolean result, String cause);
}
