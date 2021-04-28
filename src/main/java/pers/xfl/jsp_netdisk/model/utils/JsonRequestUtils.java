/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pers.xfl.jsp_netdisk.model.utils;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * 这是一个从http请求里获取 com.alibaba.fastjson.JSONObject 的工具类
 * source code URL=https://blog.csdn.net/weixin_34226182/article/details/89551738
 */
public class JsonRequestUtils {

    /**
     * 这是一个从http请求里获取 com.alibaba.fastjson.JSONObject 的main方法
     */
    public static JSONObject getJsonObject(HttpServletRequest request) throws IOException {
        String json = getRequestString(request);
        return JSONObject.parseObject(json);
    }

    /**
     * *
     * 以字符串的形式，获取 request 中 请求体 的内容，不管是post还是get请求，万能
     *
     * @param request
     * @return : String
     * @throws IOException
     */
    public static String getRequestString(HttpServletRequest request)
            throws IOException {
        String submitMehtod = request.getMethod();
        // GET
        if (submitMehtod.equals("GET")) {//URL https://blog.csdn.net/qq_28863045/article/details/79503945
            return getRequestStrFromGET(request);
        } else {// POST
            return getRequestStrFromPOST(request);
        }
    }

    /**
     * 获取请求数据的编码格式
     * @param request 一个http请求
     * @return 以字符串形式返回http请求的编码格式
     */
    public static String getRequestCharEncoding(HttpServletRequest request){
        String charEncoding = request.getCharacterEncoding();
        //如果请求数据包没有定义（注明）编码，默认使用 iso-8859-1
        if (charEncoding == null) {
//            charEncoding = "UTF-8";
            charEncoding = "iso-8859-1";
        }
        return charEncoding;
    }

    /**
     * 以字符串的形式，获取 post 请求内容
     *
     * @param request
     * @return : String
     * @throws IOException
     */
    public static String getRequestStrFromPOST(HttpServletRequest request) throws IOException {
        byte[] buffer = getRequestPostBytes(request);
        String charEncoding = getRequestCharEncoding(request);
        return new String(buffer, charEncoding);
    }

    /**
     * *
     * 以字符串的形式，获取 get 请求内容
     * source code URL=https://blog.csdn.net/qq_28863045/article/details/79503945
     * @param request
     * @return : String
     * @throws IOException
     */
    public static String getRequestStrFromGET(HttpServletRequest request) throws IOException {
        String rawData = request.getQueryString();
        String charEncoding = getRequestCharEncoding(request);
        if (rawData != null) {
            return new String(rawData.getBytes(charEncoding), StandardCharsets.UTF_8).replaceAll("%22", "\"");
        }
        return null;
    }

    /**
     * 描述:获取 post 请求的 byte[] 数组
     *
     * @param request
     * @return : byte[]
     * @throws IOException
     */
    public static byte[] getRequestPostBytes(HttpServletRequest request)
            throws IOException {
        int contentLength = request.getContentLength();
        if (contentLength < 0) {
            return null;
        }
        byte[] buffer = new byte[contentLength];
        //尝试读取数据，尽量读完整个InputStream
        for (int i = 0; i < contentLength; ) {
            int readlen = request.getInputStream().read(buffer, i, contentLength - i);
            if (readlen == -1) {
                break;
            }
            i += readlen;
        }
        return buffer;
    }

    /**
     * 将JSON对象以字符串的形式响应给客户端
     * @param response http响应对象
     * @param jsonObject alibaba fastjson的JSONObject对象
     */
    public static void responseJsonStr(HttpServletResponse response, JSONObject jsonObject){
        if(response != null){
            response.setContentType("text/html;charset=UTF-8");
            try (PrintWriter out = response.getWriter()){
                out.append(jsonObject.toJSONString());
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
