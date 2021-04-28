<%--
  Created by IntelliJ IDEA.
  User: xfl666
  Date: 2021/4/20
  Time: 10:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" language="java" %>
<%@ page import="pers.xfl.jsp_netdisk.model.appconst.MyConst" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>注册</title>
    <link href="static/css/login_common.css" rel="stylesheet" type="text/css"/>
    <link href="static/css/login_special_use.css" rel="stylesheet" type="text/css"/>
</head>
<body background="static/pic/yourname_dusk.jpg">
<div id="div1" class="makeDivCenterParent">
    <div id="div2" style="background-color: rgba(0, 0, 0, 0.68);">
        <div id="div3" class="divForm">
            <form id="formTable" action="register" method="post">
                <br>
                <span style="color: yellow;">什么？</span>
                <span style="color: hotpink;">你要和我签订魔法协议？（快去注册啦！）</span><br><br>
                <table border="0" cellspacing="0" id="table1" style="width:100%;">
                    <tr>
                        <td class="inputTableLeft">用户名</td>
                        <td class="inputTableRight">
                            <input type="text" name="username" id="userName" style="height:100%;"
                                   minlength="<%=(int)MyConst.tbAccountUsernameMinLength %>"
                                   maxlength="<%=(int)MyConst.tbAccountUsernameMaxLength %>">
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
                    <tr>
                        <td>&nbsp;</td>
                        <td></td>
                    </tr>
                    <tr>
                        <td class="inputTableLeft">重复密码</td>
                        <td class="inputTableRight">
                            <input type="password" name="password2" id="password2" maxlength="27" style="height:100%;">
                        </td>
                    </tr>
                    <tr>
                        <td>&nbsp;</td>
                        <td></td>
                    </tr>
                    <tr>
                        <td colspan="2" style="text-align: center;font-family:'楷体';font-size: large;">
                            设置一下你的用于找回密码的电子邮箱：
                        </td>
                    </tr>

                    <tr>
                        <td class="inputTableLeft">电子邮箱</td>
                        <td class="inputTableRight">
                            <input type="text" name="email" id="email" maxlength="40" isNullable="true">
                        </td>
                    </tr>
                </table>
                <br>
                <div>
                    <input type="radio" name="gender" value="1"><span style="color: blue;">我是帅哥&nbsp;&nbsp;</span>
                    <input type="radio" name="gender" value="2"><span style="color: deeppink;">我是靓妹</span><br>
                    <input type="radio" name="gender" value="0"><span style="color: hotpink;">我……我……我害羞，保密保密</span>
                </div>
                <br>
                <div>
                    <!--
                        <input id="submit1" type="submit" style="" value="注册" onmouseover="changeSubmitButtonFont('submit1',1);" onmouseout="changeSubmitButtonFont('submit1',2);" onclick="return finalcheck();">
                    -->
<%--                    <input id="submit1" type="submit" class="mySubmitBtn" value="注册" onclick="return finalcheck();">--%>
                    <div id="btn1" class="mySubmitBtn simpleMakeDivCenter" >
                        注册
                    </div>
                </div>
            </form>
        </div>

        <br>
        <div style="position: relative;text-align: center;color: yellow">已有账号？
            <a href="login" style="color:green;">立即登录！</a>
            &nbsp;&nbsp;忘记密码？
            <a href="wjmm.php" style="color:red;">立即找回！</a>
            <br><br>
            <a href="index" style="color:green;">返回首页</a>
        </div>
        <br>
    </div>
    <br>
</div>
<div id="jsDebug" style="position:absolute;"></div>

</body>
<script src="static/js/warn_user_input.js"></script>
<script src="static/js/hashes.js"></script>
<script src="static/js/ajax_util.js"></script>
<script type="text/javascript">
    var registerBtnEleId = "btn1";
    var registerBtnEle = document.getElementById(registerBtnEleId);
    var registerSuccessPage = "home";
    var postForm = function (){
        console.log("Login button has been pressed.")
        // var SHA512 = new Hashes.SHA512;
        var jsInsertDivId = "jsInsertDiv";
        var formTableId = "formTable";
        var formTableEle = document.getElementById(formTableId);

        var allInputEle = formTableEle.getElementsByTagName("input");
        var json_data = {};
        for (var i = 0; i < allInputEle.length; i++) {
            let currEle = allInputEle[i];
            var inputEleName = currEle.getAttribute("name") ;
            console.log(inputEleName + " found!");
            if(currEle.type.toUpperCase() === "radio".toUpperCase()){
                if(currEle.checked){
                    json_data[inputEleName] = currEle.value;
                }
            }
            else {
                if(inputEleName.toUpperCase() === "password2".toUpperCase())
                    continue;
                json_data[inputEleName] = currEle.value;
            }
        }
        // json_data['operation'] = "update";

        ajax({
            url: "register",
            type: 'post',
            data: json_data,
            dataType: 'json',
            timeout: 50000,  //5秒超时
            contentType:"application/json;charset=utf-8",
            success: function (recvData) {
                console.log(recvData);
                //服务器返回响应，根据响应结果，分析是否登录成功
                var recvDataObj =JSON.parse(recvData);
                console.log(recvDataObj.flag);
                if (recvDataObj.flag) {//成功
                    registerBtnEle.innerText = recvDataObj.msg + "";
                    formTableEle.style.display = "None";
                    document.getElementById("bottomDiv").style.display = "None";
                    registerBtnEle.onclick=function (){
                        window.location.href = registerSuccessPage;
                    };

                    let t = 1;//设定跳转的时间
                    var jumpFunction = setInterval(
                        function () {
                            document.getElementById(jsInsertDivId).innerText = "登录成功！" + t + "秒后转跳！";
                            if (t === 0) {
                                clearInterval(jumpFunction);
                                window.location.href = registerSuccessPage; //设定跳转的链接地址
                            }
                            t--;
                        }, 1000); //启动1秒定时
                } else { //失败
                    document.getElementById(jsInsertDivId).innerText = "登录失败！"+ recvDataObj.msg +"。";
                }
            },
            //异常处理
            error: function (e) {
                console.log(e);
            },
            ontimeout:function (event){
                console.log(event);
                document.getElementById(jsInsertDivId).innerText = "请求超时。";
            }
        });
    }
    registerBtnEle.onclick = function (){postForm();};
</script>
</html>
