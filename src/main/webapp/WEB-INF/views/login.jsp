<%--
    Document   : login.jsp
    Created on : 2020-8-27, 8:33:32
    Author     : xfl666
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" language="java" %>
<%@ page import="pers.xfl.jsp_netdisk.model.utils.MyStrIsOK" %>
<%@ page import="pers.xfl.jsp_netdisk.model.appconst.MyConst" %>
<!doctype html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>登录</title>
    <link href="static/css/login_common.css" rel="stylesheet" type="text/css"/>
    <link href="static/css/login_special_use.css" rel="stylesheet" type="text/css"/>
    <!--
    <audio src="ziyuan/mp3/yournameAtDusk.mp3" id="audio1" hidden="true" autoplay="true" loop="true">
    </audio>
    -->

    <!--
        <embed src="ziyuan/mp3/yournameAtDusk.mp3" hidden="true" autostart="true" loop="true" />
    -->
</head>
<body background="static/pic/yourname_dusk.jpg">
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
                        <input type="text" name="userName" id="username" style="height:100%;"
                               maxlength="<%=(int)MyConst.tbAccountUsernameMaxLength%>">
                    </td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td></td>
                </tr>
                <tr>
                    <td class="inputTableLeft">密码</td>
                    <td class="inputTableRight">
                        <input type="password" name="password" id="password" style="height:100%;"
                               minlength="<%=(int)MyConst.tbAccountPasswordMinLength%>"
                               maxlength="<%=(int)MyConst.tbAccountPasswordMaxLength%>">
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
        <div id="bottomDiv" style="position: relative;text-align: center;">
            <a href="index" style="color:green;">返回首页</a>
        </div>
        <br>
    </div>
    <br>
</div>
</body>
<script src="static/js/warn_user_input.js"></script>
<script src="static/js/hashes.js"></script>
<script src="static/js/ajax_util.js"></script>
<script type="text/javascript">
    var jump2loginBtnEleId = "btn1";
    var jump2loginBtnEle = document.getElementById(jump2loginBtnEleId);
    var loginSuccessPage = "home";
    var postForm = function () {
        console.log("Login button has been pressed.")
        var SHA512 = new Hashes.SHA512;
        var jsInsertDivId = "jsInsertDiv";
        var formTableId = "formTable";
        var formTableEle = document.getElementById(formTableId);
        var username = document.getElementById("username").value + "";
        var password = document.getElementById("password").value + "";
        // console.log(password);
        // console.log(SHA512.hex(password));
        password = SHA512.hex(password);

        var json_data = {};
        json_data["username"] = username;
        json_data["password"] = password;
        ajax({
            url: "login",
            type: 'post',
            data: json_data,
            dataType: 'json',
            timeout: 50000,  //5秒超时
            contentType: "application/json;charset=utf-8",
            success: function (recvData) {
                console.log(recvData);
                //服务器返回响应，根据响应结果，分析是否登录成功
                var testJsonStr = "{\"flag\":false,\"StudentID\":\"1\",\"CourseID\":\"1\",\"score\":\"80\"}";
                var recvDataObj = JSON.parse(recvData);
                console.log(recvDataObj.flag);
                if (recvDataObj.flag) {//成功
                    jump2loginBtnEle.innerText = recvDataObj.msg + "";
                    formTableEle.style.display = "None";
                    document.getElementById("bottomDiv").style.display = "None";
                    jump2loginBtnEle.onclick = function () {
                        window.location.href = loginSuccessPage;
                    };

                    let t = 1;//设定跳转的时间
                    var jumpFunction = setInterval(
                        function () {
                            document.getElementById(jsInsertDivId).innerText = "登录成功！" + t + "秒后转跳！";
                            if (t === 0) {
                                clearInterval(jumpFunction);
                                window.location.href = loginSuccessPage; //设定跳转的链接地址
                            }
                            t--;
                        }, 1000); //启动1秒定时
                } else { //失败
                    document.getElementById(jsInsertDivId).innerText = "登录失败！" + recvDataObj.msg + "。";
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
    jump2loginBtnEle.onclick = function () {
        postForm();
    };
</script>
</html>