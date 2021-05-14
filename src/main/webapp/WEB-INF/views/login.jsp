<%--
    Document   : login.jsp
    Created on : 2020-8-27, 8:33:32
    Author     : xfl666
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" language="java" %>
<%@ page import="com.github.xfl12345.jsp_netdisk.model.utility.MyStrIsOK" %>
<%@ page import="com.github.xfl12345.jsp_netdisk.appconst.MyConst" %>
<%@ page import="com.github.xfl12345.jsp_netdisk.appconst.api.request.LoginRequestField" %>
<%@ page import="com.github.xfl12345.jsp_netdisk.appconst.field.TbAccountField" %>
<!doctype html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>登录</title>
    <link href="<%=request.getContextPath() %>/static/css/login_common.css" rel="stylesheet" type="text/css"/>
    <link href="<%=request.getContextPath() %>/static/css/login_special_use.css" rel="stylesheet" type="text/css"/>
    <!--
    <audio src="ziyuan/mp3/yournameAtDusk.mp3" id="audio1" hidden="true" autoplay="true" loop="true">
    </audio>
    -->

    <!--
        <embed src="ziyuan/mp3/yournameAtDusk.mp3" hidden="true" autostart="true" loop="true" />
    -->
</head>
<body background="<%=request.getContextPath() %>/static/pic/yourname_dusk.jpg">
<div id="div1" class="makeDivCenterParent">
    <div id="div2" class="broaderBreath">
        <div id="div3" class="divForm">
            <br>
            <span id="divFormTitle" class="divFormTitle">${divFormTitle}</span>
            <br><br>
            <table border="0" cellspacing="0" id="formTable" style="width:100%;">
                <tr>
                    <td class="inputTableLeft">用户名</td>
                    <td class="inputTableRight">
                        <input type="text" name="<%=LoginRequestField.USERNAME %>" id="username" style="height:100%;"
                               maxlength="<%=TbAccountField.USERNAME_MAX_LENGTH%>">
                    </td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td></td>
                </tr>
                <tr>
                    <td class="inputTableLeft">密码</td>
                    <td class="inputTableRight">
                        <input type="password" name="<%=LoginRequestField.PASSWORD %>" id="password" style="height:100%;"
                               minlength="<%=TbAccountField.PASSWORD_MIN_LENGTH%>"
                               maxlength="<%=TbAccountField.PASSWORD_MAX_LENGTH%>">
                    </td>
                </tr>

            </table>
            <br>
            <div id="btn1" class="mySubmitBtn simpleMakeDivCenter">
                登录
            </div>
            <div id="jsInsertDiv" class="jsInsert simpleMakeDivCenter"></div>
        </div>

        <br>

        <div id="bottomDiv" style="position: relative;text-align: center;">没有账号？
            <a href="register" style="color:green;">立即注册！</a>
            <%--            &nbsp;&nbsp;忘记密码？--%>
            <%--            <a href="wjmm.php" style="color:red;">立即找回！</a>--%>
            <br><br>
            <a href="<%=request.getContextPath() %>/index" style="color:green;">返回首页</a>
        </div>

        <br>
    </div>
    <br>
</div>
</body>
<script src="<%=request.getContextPath() %>/static/js/warn_user_input.js"></script>
<script src="<%=request.getContextPath() %>/static/js/hashes.js"></script>
<script src="<%=request.getContextPath() %>/static/js/ajax_util.js"></script>
<script type="text/javascript">
    let postLocation = "login";
    let successPage = "home";

    let btnEleId = "btn1";
    let jsInsertDivId = "jsInsertDiv";
    let formTableId = "formTable";
    let btnEle = document.getElementById(btnEleId);
    let formTableEle = document.getElementById(formTableId);

    let postForm = function () {
        console.log("button has been pressed.")
        let allInputEle = formTableEle.getElementsByTagName("input");
        let json_data = {};
        for (let i = 0; i < allInputEle.length; i++) {
            let currEle = allInputEle[i];
            let inputEleName = currEle.getAttribute("name");
            console.log(inputEleName + " found!");
            if (currEle.type.toUpperCase() === "radio".toUpperCase()) {
                if (currEle.checked) {
                    json_data[inputEleName] = "" + currEle.value;
                }
            } else {
                if (inputEleName.toUpperCase() === "password2".toUpperCase())
                    continue;
                json_data[inputEleName] = "" + currEle.value;
            }
        }

        let SHA512 = new Hashes.SHA512;
        let passwordInJsonKey = "<%=LoginRequestField.PASSWORD %>";
        json_data[passwordInJsonKey] = SHA512.hex(json_data[passwordInJsonKey]);

        // json_data['operation'] = "update";
        ajax({
            url: postLocation,
            type: 'post',
            data: json_data,
            dataType: 'json',
            timeout: 50000,  //5秒超时
            contentType: "application/json;charset=utf-8",
            success: function (recvData) {
                console.log(recvData);
                //服务器返回响应，根据响应结果，分析是否登录成功
                let testJsonStr = "{\"flag\":false,\"StudentID\":\"1\",\"CourseID\":\"1\",\"score\":\"80\"}";
                let recvDataObj = JSON.parse(recvData);
                console.log(recvDataObj.success);
                if (recvDataObj.success) {//成功
                    btnEle.innerText = recvDataObj.message + "";
                    formTableEle.style.display = "None";
                    document.getElementById("bottomDiv").style.display = "None";
                    btnEle.onclick = function () {
                        window.location.href = successPage;
                    };

                    let t = 1;//设定跳转的时间
                    let jumpFunction = setInterval(
                        function () {
                            document.getElementById(jsInsertDivId).innerText = "登录成功！" + t + "秒后转跳！";
                            if (t === 0) {
                                clearInterval(jumpFunction);
                                window.location.href = successPage; //设定跳转的链接地址
                            }
                            t--;
                        }, 1000); //启动1秒定时
                } else { //失败
                    document.getElementById(jsInsertDivId).innerText = "登录失败！" + recvDataObj.message + "。";
                }
            },
            //异常处理
            error: function (e) {
                console.log(e);
            },
            ontimeout: function (event) {
                console.log(event);
                document.getElementById(jsInsertDivId).innerText = "请求超时。";
            }
        });
    }
    btnEle.onclick = function () {
        postForm();
    };
</script>
</html>