<%--
    Document   : login.jsp
    Created on : 2020-8-27, 8:33:32
    Author     : xfl666
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" language="java" %>
<%@ page import="com.github.xfl12345.jsp_netdisk.model.utility.MyStrIsOK" %>
<%@ page import="com.github.xfl12345.jsp_netdisk.appconst.MyConst" %>
<%@ page import="static com.github.xfl12345.jsp_netdisk.StaticSpringApp.myConst" %>
<%@ page import="com.github.xfl12345.jsp_netdisk.appconst.field.*" %>
<%@ page import="com.github.xfl12345.jsp_netdisk.model.pojo.database.TbAccount" %>
<%@ page import="com.github.xfl12345.jsp_netdisk.appconst.api.request.RegisterRequestField" %>
<%@ page import="com.github.xfl12345.jsp_netdisk.appconst.api.request.EmailVerificationRequestField" %>
<!doctype html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${title}</title>
    <link href="<%=request.getContextPath() %>/static/css/login_common.css" rel="stylesheet" type="text/css"/>
    <link href="<%=request.getContextPath() %>/static/css/login_special_use.css" rel="stylesheet" type="text/css"/>
    <style>
        .emailVerificationCodeSentEmailBtnDisable {
            background-color: transparent;
        }
    </style>

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
            <br>
            <table border="0" cellspacing="0" id="formTable" style="width:100%;">
                <tr>
                    <td id="divFormSubTitle" colspan="2"
                        style="text-align: center;font-family:'楷体';color:blue;font-size:10pt;">${msg}</td>
                </tr>
                <tr>
                    <td class="inputTableLeft">电子邮箱</td>
                    <td class="inputTableRight" id="<%=RegisterRequestField.EMAIL %>">
                        <% TbAccount tbAccount = (TbAccount) request.getSession().getAttribute(MySessionAttributes.TB_ACCOUNT); %>
                        <%=tbAccount.getEmail() %>
                    </td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td></td>
                </tr>
                <tr>
                    <td class="inputTableLeft">验证码</td>
                    <td class="inputTableRight">
                        <input type="text" name="<%=EmailVerificationRequestField.EMAIL_VERIFICATION_CODE %>"
                               id="<%=EmailVerificationRequestField.EMAIL_VERIFICATION_CODE %>"
                               minlength="<%=myConst.getEmailVerificationCodeLength() %>"
                               maxlength="<%=myConst.getEmailVerificationCodeLength() %>"
                               style="height:100%;">
                    </td>
                </tr>

            </table>
            <br>

            <div id="btn2" class="mySubmitBtn simpleMakeDivCenter">
                重新获取验证码
            </div>
            <div id="jsInsertDiv2" class="jsInsert simpleMakeDivCenter"></div>
            <br><br>
            <div id="btn1" class="mySubmitBtn simpleMakeDivCenter">
                激活
            </div>
            <div id="jsInsertDiv" class="jsInsert simpleMakeDivCenter"></div>
        </div>

        <br>
        <div id="bottomDiv" style="position: relative;text-align: center;">
            <a href="<%=request.getContextPath() %>/index" style="color:green;">返回首页</a>
        </div>
        <br>
    </div>
    <br>
</div>
</body>
<script src="<%=request.getContextPath() %>/static/js/warn_user_input.js"></script>
<script src="<%=request.getContextPath() %>/static/js/ajax_util.js"></script>
<script type="text/javascript">
    var successPage = "home";
    var postApiLocation = "emailVerificationCode";
    var sentEmailApiLocation = "emailVerificationCodeSentEmail";

    var bottomDivId = "bottomDiv";
    var bottomDivEle = document.getElementById(bottomDivId);

    var divFormTitleId = "divFormTitle";
    var divFormTitleEle = document.getElementById(divFormTitleId);
    var divFormSubTitleId = "divFormSubTitle";
    var divFormSubTitleEle = document.getElementById(divFormSubTitleId);

    // 请求再发一封邮件验证码的按钮
    var btn2EleId = "btn2";
    var btn2Ele = document.getElementById(btn2EleId);
    var jsInsertDiv2Id = "jsInsertDiv2";
    var jsInsertDiv2Ele = document.getElementById(jsInsertDiv2Id);

    // 提交验证码的按钮
    var formTableId = "formTable";
    var formTableEle = document.getElementById(formTableId);
    var btnEleId = "btn1";
    var btnEle = document.getElementById(btnEleId);
    var jsInsertDivId = "jsInsertDiv";
    var jsInsertDivEle = document.getElementById(jsInsertDivId);

    var postForm = function () {
        console.log(btnEleId + " has been pressed.")

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
            url: postApiLocation,
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
                    formTableEle.style.display = "none";
                    btn2Ele.style.display = "none";
                    jsInsertDiv2Ele.style.display = "none";
                    bottomDivEle.style.display = "none";
                    divFormTitleEle.innerText = "封印解除！";
                    btnEle.onclick = function () {
                        window.location.href = successPage;
                    };
                    let t = 1;//设定跳转的时间
                    let jumpFunction = setInterval(
                        function () {
                            jsInsertDivEle.innerText = "验证通过！" + t + "秒后转跳！";
                            if (t === 0) {
                                clearInterval(jumpFunction);
                                window.location.href = successPage; //设定跳转的链接地址
                            }
                            t--;
                        }, 1000); //启动1秒定时
                } else { //失败
                    jsInsertDivEle.innerText = "验证失败！" + recvDataObj.message + "。";
                }
            },
            //异常处理
            error: function (e) {
                console.log(e);
            },
            ontimeout: function (event) {
                console.log(event);
                jsInsertDivEle.innerText = "请求超时。";
            }
        });
    }
    btnEle.onclick = function () {
        postForm();
    };

    var emailVerificationCodeGet = function () {
        console.log(btn2EleId + " has been pressed.")
        let json_data = {};
        // json_data['operation'] = "update";
        ajax({
            url: sentEmailApiLocation,
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
                    let btnOldOnClickEvent = btn2Ele.onclick;
                    btn2Ele.onclick = null;
                    let btnOldText = btn2Ele.innerText;
                    btn2Ele.classList.add("emailVerificationCodeSentEmailBtnDisable");
                    let t = <%=myConst.getSentEmailMaxFrequencyPeerSeconds() %>;//设定倒计时总时间
                    let jumpFunction = setInterval(
                        function () {
                            btn2Ele.innerText = t + "秒后可重新获取";
                            if (t === 0) {
                                clearInterval(jumpFunction);
                                btn2Ele.onclick = btnOldOnClickEvent;
                                btn2Ele.innerText = btnOldText;
                                btn2Ele.classList.remove("emailVerificationCodeSentEmailBtnDisable");
                            }
                            t--;
                        }, 1000); //启动1秒定时
                } else { //失败
                    jsInsertDiv2Ele.innerText = "请求失败！" + recvDataObj.message + "。";
                }
            },
            //异常处理
            error: function (e) {
                console.log(e);
            },
            ontimeout: function (event) {
                console.log(event);
                jsInsertDiv2Ele.innerText = "请求超时。";
            }
        });
    }
    btn2Ele.onclick = function () {
        emailVerificationCodeGet();
    };


</script>
</html>