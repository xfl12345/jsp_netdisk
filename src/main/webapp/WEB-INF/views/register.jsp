<%--
  Created by IntelliJ IDEA.
  User: xfl666
  Date: 2021/4/20
  Time: 10:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" language="java" %>
<%@ page import="com.github.xfl12345.jsp_netdisk.appconst.api.request.RegisterRequestField" %>
<%@ page import="com.github.xfl12345.jsp_netdisk.appconst.field.TbAccountField" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>注册</title>
    <link href="<%=request.getContextPath() %>/static/css/login_common.css" rel="stylesheet" type="text/css"/>
    <link href="<%=request.getContextPath() %>/static/css/login_special_use.css" rel="stylesheet" type="text/css"/>
</head>
<body background="<%=request.getContextPath() %>/static/pic/yourname_dusk.jpg">
<div id="div1" class="makeDivCenterParent">
    <div id="div2" style="background-color: rgba(0, 0, 0, 0.68);">
        <div id="div3" class="divForm">
            <form id="formTable" method="post">
                <br>
                <span style="color: yellow;">什么？</span>
                <span style="color: hotpink;">你要和我签订魔法协议？（快去注册啦！）</span><br><br>
                <table border="0" cellspacing="0" id="table1" style="width:100%;">
                    <tr>
                        <td class="inputTableLeft">用户名</td>
                        <td class="inputTableRight">
                            <input type="text" name="<%=RegisterRequestField.USERNAME %>" id="userName"
                                   style="height:100%;"
                                   minlength="<%=TbAccountField.USERNAME_MIN_LENGTH %>"
                                   maxlength="<%=TbAccountField.USERNAME_MAX_LENGTH %>">
                        </td>
                    </tr>
                    <tr>
                        <td>&nbsp;</td>
                        <td></td>
                    </tr>
                    <tr>
                        <td class="inputTableLeft">密码</td>
                        <td class="inputTableRight">
                            <input type="password" name="<%=RegisterRequestField.PASSWORD %>" id="password"
                                   style="height:100%;"
                                   minlength="<%=TbAccountField.PASSWORD_MIN_LENGTH%>"
                                   maxlength="<%=TbAccountField.PASSWORD_MAX_LENGTH%>">
                        </td>
                    </tr>
                    <tr>
                        <td>&nbsp;</td>
                        <td></td>
                    </tr>
                    <tr>
                        <td class="inputTableLeft">重复密码</td>
                        <td class="inputTableRight">
                            <input type="password" id="password2" style="height:100%;"
                                   name="<%=RegisterRequestField.PASSWORD %>2"
                                   minlength="<%=TbAccountField.PASSWORD_MIN_LENGTH%>"
                                   maxlength="<%=TbAccountField.PASSWORD_MAX_LENGTH %>">
                        </td>
                    </tr>
                    <tr>
                        <td>&nbsp;</td>
                        <td></td>
                    </tr>
                    <tr>
                        <td colspan="2" style="text-align: center;font-family:'楷体';font-size: large;color: hotpink;">
                            设置一下你的用于找回密码的电子邮箱：
                        </td>
                    </tr>

                    <tr>
                        <td class="inputTableLeft">电子邮箱</td>
                        <td class="inputTableRight">
                            <input type="text" id="email" isNullable="false"
                                   name="<%=RegisterRequestField.EMAIL %>"
                                   maxlength="<%=TbAccountField.EMAIL_MAX_LENGTH %>">
                        </td>
                    </tr>
                </table>
                <br>
                <div>
                    <input type="radio" name="<%=RegisterRequestField.GENDER %>" value="<%=TbAccountField.GENDER.MALE %>"><span style="color: blue;">我是帅哥&nbsp;&nbsp;</span>
                    <input type="radio" name="<%=RegisterRequestField.GENDER %>" value="<%=TbAccountField.GENDER.FEMALE %>"><span
                        style="color: deeppink;">我是靓妹</span><br>
                    <input type="radio" name="<%=RegisterRequestField.GENDER %>" value="<%=TbAccountField.GENDER.DEFAULT %>" checked="checked"><span
                        style="color: hotpink;">我……我……我害羞，保密保密</span>
                </div>
                <br>
                <div>
                    <!--
                        <input id="submit1" type="submit" style="" value="注册" onmouseover="changeSubmitButtonFont('submit1',1);" onmouseout="changeSubmitButtonFont('submit1',2);" onclick="return finalcheck();">
                    -->
                    <%--                    <input id="submit1" type="submit" class="mySubmitBtn" value="注册" onclick="return finalcheck();">--%>
                </div>
            </form>
            <br>
            <div id="btn1" class="mySubmitBtn simpleMakeDivCenter">
                注册
            </div>
            <div id="jsInsertDiv" class="jsInsert">
            </div>
        </div>

        <br>
        <div id="bottomDiv" style="position: relative;text-align: center;color: yellow">已有账号？
            <a href="login" style="color:green;">立即登录！</a>
<%--            &nbsp;&nbsp;忘记密码？--%>
<%--            <a href="wjmm.php" style="color:red;">立即找回！</a>--%>
            <br><br>
            <a href="<%=request.getContextPath() %>/index" style="color:green;">返回首页</a>
        </div>
        <br>
    </div>
    <br>
</div>
<div id="jsDebug" style="position:absolute;"></div>

</body>
<script src="<%=request.getContextPath() %>/static/js/warn_user_input.js"></script>
<script src="<%=request.getContextPath() %>/static/js/hashes.js"></script>
<script src="<%=request.getContextPath() %>/static/js/ajax_util.js"></script>
<script type="text/javascript">
    let successPage = "home";
    let postLocation = "register";

    let btnEleId = "btn1";
    let jsInsertDivId = "jsInsertDiv";
    let formTableId = "formTable";
    let bottomDivId = "bottomDiv";
    let btnEle = document.getElementById(btnEleId);
    let jsInsertDiv = document.getElementById(jsInsertDivId);
    let formTableEle = document.getElementById(formTableId);
    let bottomDivEle = document.getElementById(bottomDivId);

    let postForm = function () {
        console.log("post button has been pressed.");
        // 检查用户输入是否完整且合法
        if (!finalcheck()) {
            return;
        }
        // let SHA512 = new Hashes.SHA512;

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
                let recvDataObj = JSON.parse(recvData);
                console.log(recvDataObj.success);
                if (recvDataObj.success) {//成功
                    formTableEle.style.display = "None";
                    bottomDivEle.style.display = "None";
                    btnEle.style.marginTop = "30px";
                    btnEle.innerText = "注册成功";
                    jsInsertDiv.innerHTML = "" + recvDataObj.message;

                    btnEle.onclick = function () {
                        window.location.href = successPage;
                    };

                    let t = 3;//设定倒计时的时间
                    let jumpFunction = setInterval(
                        function () {
                            if (t === 0) {
                                clearInterval(jumpFunction);
                                btnEle.innerText = "进入网盘";
                            }
                            t--;
                        }, 1000); //启动1秒定时
                } else { //失败
                   jsInsertDiv.innerHTML = "" + recvDataObj.message;
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
