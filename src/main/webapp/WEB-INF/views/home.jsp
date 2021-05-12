<%--
  Created by IntelliJ IDEA.
  User: xfl666
  Date: 2021/4/20
  Time: 14:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" language="java" %>
<%@ page import="com.github.xfl12345.jsp_netdisk.StaticSpringApp" %>
<%@ page import="com.github.xfl12345.jsp_netdisk.appconst.AppInfo" %>
<%@ page import="com.github.xfl12345.jsp_netdisk.model.utils.MyStrIsOK" %>
<%@ page import="com.github.xfl12345.jsp_netdisk.model.pojo.database.TbAccount" %>
<%@ page import="com.github.xfl12345.jsp_netdisk.appconst.field.MySessionAttributes" %>
<%
    TbAccount tbAccount = (TbAccount) request.getSession().getAttribute(MySessionAttributes.TB_ACCOUNT);

%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>用户空间</title>
    <link rel="stylesheet" href="https://unpkg.com/element-ui/lib/theme-chalk/index.css">
</head>
<body>
<%
    String str = "";

%>


登录成功！
<a href="logout">注销</a>

</body>
<script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
<script src="https://unpkg.com/element-ui/lib/index.js"></script>

<script type="text/javascript">

</script>
</html>
