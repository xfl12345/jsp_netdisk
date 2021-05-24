<%--
  Created by IntelliJ IDEA.
  User: xfl666
  Date: 2021/4/19
  Time: 13:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="zh">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${title}</title>
    <link href="<%=request.getContextPath() %>/static/css/login_common.css" rel="stylesheet" type="text/css"/>
    <link href="<%=request.getContextPath() %>/static/css/login_special_use.css" rel="stylesheet" type="text/css"/>
</head>
<body background="<%=request.getContextPath() %>/static/images/yourname_dusk.jpg">

<div id="div1" class="makeDivCenterParent">
    <div id="div2" class="broaderBreath">
        <div id="div3" class="divForm">
            <br>
            <span id="divFormTitle" class="divFormTitle" >${divFormTitle}</span>
            <br><br>
            <div id="btn1" class="mySubmitBtn simpleMakeDivCenter" onclick="window.location.href='${btn1url}'" >
                ${btn1text}
            </div>
            <br>
            <div id="btn2" class="mySubmitBtn simpleMakeDivCenter" onclick="window.location.href='${btn2url}'" >
                ${btn2text}
            </div>
            <br>
            <br>
        </div>
    </div>
</div>
</body>
</html>