<%--
  Created by IntelliJ IDEA.
  User: xfl666
  Date: 2021/4/20
  Time: 14:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" language="java" %>
<%@ page import="pers.xfl.jsp_netdisk.StaticSpringApp" %>
<%@ page import="pers.xfl.jsp_netdisk.model.appconst.AppInfo" %>
<%@ page import="pers.xfl.jsp_netdisk.model.utils.MyStrIsOK" %>
<%
%>
<html>
<head>
    <title>用户空间</title>
</head>
<body>
<%
    String str = "";
    StaticSpringApp.getBean("appInfo", AppInfo.class);

%>

登录成功！
<a href="logout">注销</a>

</body>
</html>
