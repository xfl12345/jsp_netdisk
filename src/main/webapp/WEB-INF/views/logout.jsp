<%@ page import="java.util.ArrayList" %>
<%@ page import="com.alibaba.fastjson.JSONObject" %><%--
  Created by IntelliJ IDEA.
  User: xfl666
  Date: 2021/4/23
  Time: 11:03
  To change this template use FileOperation | Settings | FileOperation Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>注销</title>
<%--    <link rel="stylesheet" href="https://unpkg.com/element-ui/lib/theme-chalk/index.css">--%>
    <link href="<%=request.getContextPath() %>/static/css/login_common.css" rel="stylesheet" type="text/css"/>
    <link href="<%=request.getContextPath() %>/static/css/login_special_use.css" rel="stylesheet" type="text/css"/>
</head>
<body>
    <h1 id="h1"></h1>
    <div id="additionalContent">
        <button id="btn1" class="mySubmitBtn" style="width: auto;" ></button>
        <br><br>
        <button id="btn2" class="mySubmitBtn" style="width: auto;" ></button>
    </div>
</body>

<%--<script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>--%>
<%--<script src="https://unpkg.com/element-ui/lib/index.js"></script>--%>

<script src="<%=request.getContextPath() %>/static/js/ajax_util.js"></script>
<script type="text/javascript">
    let jsInsertEle = document.getElementById("h1");

    let postForm = function (){
        let logoutSuccessPage = "<%=request.getContextPath() %>/index";
        let otherElement = document.getElementById("additionalContent");
        otherElement.parentNode.removeChild(otherElement);

        let json_data = {};
        // json_data["isAcceptHtmlCode"] = true;
        ajax({
            url: "logout",
            type: 'post',
            data: json_data,
            dataType: 'json',
            timeout: 50000,  //5秒超时
            contentType:"application/json;charset=utf-8",
            success: function (recvData) {
                console.log(recvData);
                //服务器返回响应，根据响应结果，分析是否请求成功
                let testJsonStr = "{\"flag\":false,\"StudentID\":\"1\",\"CourseID\":\"1\",\"score\":\"80\"}";
                let recvDataObj =JSON.parse(recvData);
                console.log(recvDataObj.success);
                let msg;
                if (recvDataObj.success) {//成功
                    msg = "注销成功！";
                } else { //失败
                    msg = "注销失败！"+ recvDataObj.message +"。";
                }
                let t = 1;//设定跳转的时间
                let jumpFunction = setInterval(
                    function () {
                        jsInsertEle.innerText = msg + '\n' + t + "秒后转跳！";
                        if (t === 0) {
                            clearInterval(jumpFunction);
                            window.location.href = logoutSuccessPage; //设定跳转的链接地址
                        }
                        t--;
                    }, 1000); //启动1秒定时
            },
            //异常处理
            error: function (e) {
                console.log(e);
            },
            ontimeout:function (event){
                console.log(event);
                jsInsertEle.innerText = "请求超时。";
            }
        });
    }

    window.onload = function () {
        let h1Ele = document.getElementById("h1");

        h1Ele.innerText = "确认注销？";
        let buttonElement = document.getElementById("btn1")
        buttonElement.innerText = "确认注销";
        buttonElement.onclick = postForm;

        let buttonElement2 = document.getElementById("btn2");
        buttonElement2.innerText = "还是算了吧";
        buttonElement2.onclick = function () {
            window.location.href='<%=request.getContextPath() %>/index';
        }
        
    }
</script>
</html>

