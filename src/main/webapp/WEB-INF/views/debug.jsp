<%--
  Created by IntelliJ IDEA.
  User: xfl666
  Date: 2021/5/7
  Time: 20:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Title</title>
    <link href="<%=request.getContextPath() %>/static/css/login_common.css" rel="stylesheet" type="text/css"/>
</head>
<body>
<%



%>
<div id="jsInsert" class="jsInsert">
</div>
<br>
<br>
<button id="btn1" class="mySubmitBtn" style="width: auto;" ></button>

</body>
<script src="<%=request.getContextPath() %>/static/js/ajax_util.js"></script>
<script type="text/javascript">
    let jsInsertEle = document.getElementById("jsInsert");

    let postForm = function (){
        let json_data = {};
        ajax({
            url: "debug",
            type: 'post',
            data: json_data,
            dataType: 'json',
            timeout: 50000,  //5秒超时
            contentType:"application/json;charset=utf-8",
            success: function (recvData) {
                console.log(recvData);
                //服务器返回响应，根据响应结果，分析是否登录成功
                let testJsonStr = "{\"flag\":false,\"StudentID\":\"1\",\"CourseID\":\"1\",\"score\":\"80\"}";
                let recvDataObj =JSON.parse(recvData);
                console.log(recvDataObj.flag);
                jsInsertEle.innerText = recvDataObj.msg;
            },
            //异常处理
            error: function (e) {
                console.log(e);
                jsInsertEle.innerText = "请求异常。";
            },
            ontimeout:function (event){
                console.log(event);
                jsInsertEle.innerText = "请求超时。";
            }
        });
    }

    window.onload = function () {
        let buttonElement = document.getElementById("btn1")
        buttonElement.innerText = "发送请求";
        buttonElement.onclick = postForm;
    }


</script>
</html>
