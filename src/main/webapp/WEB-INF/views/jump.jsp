<%@ page import="java.util.ArrayList" %>
<%@ page import="com.alibaba.fastjson.JSONObject" %><%--
  Created by IntelliJ IDEA.
  User: xfl666
  Date: 2021/4/23
  Time: 11:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${title}</title>
    <%--    <link rel="stylesheet" href="https://unpkg.com/element-ui/lib/theme-chalk/index.css">--%>
    <link href="<%=request.getContextPath() %>/static/css/login_common.css" rel="stylesheet" type="text/css"/>
    <link href="<%=request.getContextPath() %>/static/css/login_special_use.css" rel="stylesheet" type="text/css"/>
</head>
<body>
    <h1 id="h1"></h1>
    <div id="additionalContent">
    </div>
</body>

<%--<script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>--%>
<%--<script src="https://unpkg.com/element-ui/lib/index.js"></script>--%>

<script src="<%=request.getContextPath() %>/static/js/ajax_util.js"></script>
<script type="text/javascript">
    let jsInsertEle = document.getElementById("h1");
    let jumpView = "<%=request.getContextPath() %>/${jumpView}";

    window.onload = function () {
        let h1Ele = document.getElementById("h1");

        let t = ${jumpCountdown};//设定跳转的时间
        let jumpFunction = setInterval(
            function () {
                jsInsertEle.innerText = "${msg}" + '\n' + t + "秒后将自动转跳！";
                if (t === 0) {
                    clearInterval(jumpFunction);
                    window.location.href = jumpView; //设定跳转的链接地址
                }
                t--;
            }, 1000); //启动1秒定时
        
    }
</script>
</html>

