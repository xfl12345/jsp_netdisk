package com.github.xfl12345.jsp_netdisk.model.pojo.html;

import org.apache.ibatis.io.Resources;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class VerificationEmailTemplate {
    private final Document emailHtmlDocument;
    private final Element websiteNameElement;
    private final Element contentTitleElement;
    private final Element contentMainBodyElement;


    public VerificationEmailTemplate(String staticVerificationEmailFilePath) throws IOException{
        File input = Resources.getResourceAsFile(staticVerificationEmailFilePath);
        emailHtmlDocument = Jsoup.parse(input, StandardCharsets.UTF_8.name());
        // 邮件里显示的网站名称
        websiteNameElement = emailHtmlDocument.getElementById("websiteName");
        // 邮件正文内容的标题（非邮件自身属性里的标题，这个是写在邮件正文里的）
        contentTitleElement = emailHtmlDocument.getElementById("contentTitle");
        // 邮件里的正文内容
        contentMainBodyElement = emailHtmlDocument.getElementById("contentMainBody");
    }

    public VerificationEmailTemplate() throws IOException {
        this("staticVerificationEmail.html");
    }

    public Document getEmailHtmlDocument() {
        return emailHtmlDocument.clone();
    }

    public Element getContentMainBodyElement() {
        return contentMainBodyElement.clone();
    }

    public Element getContentTitleElement() {
        return contentTitleElement.clone();
    }

    public Element getWebsiteNameElement() {
        return websiteNameElement.clone();
    }
}
