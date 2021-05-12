package com.github.xfl12345.jsp_netdisk.model.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;


/**
 * 这个类主要是用于消灭 Tomcat 9的警告："org.apache.jasper.servlet.TldScanner.scanJars 至少有一个JAR被扫描用于TLD但尚未包含TLD。"
 * 但是没有修复成功。。。
 * source code URL=https://blog.csdn.net/yucaifu1989/article/details/39555261
 */
public class DateTag extends SimpleTagSupport {

    @Override
    public void doTag() throws JspException, IOException {
        PageContext ctx = (PageContext) getJspContext();
        JspWriter out = ctx.getOut();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        out.print(sdf.format(new Date()));
    }

}
