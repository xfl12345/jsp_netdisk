import org.apache.ibatis.io.Resources;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Tag;
import com.github.xfl12345.jsp_netdisk.appconst.AppInfo;

import java.io.*;

public class StudyJsoup {
    public static void main(String[] args) throws IOException {
        System.out.println(Tag.isKnownTag("a"));
        Document document = Jsoup.parse("<!doctype html><html></html>");
        Element metaElement = new Element("meta");
        metaElement.attr("http-equiv","Content-Type");
        metaElement.attr("content","text/html;charset=UTF-8");
        metaElement.appendTo(document.head());
        Node htmlNode = document.body().parentNode();
        Node documentRoot = htmlNode.parentNode();
        System.out.println(htmlNode.nodeName());
        System.out.println(documentRoot.nodeName());
        System.out.println(documentRoot.childNodeSize());
        for (Node node : documentRoot.childNodes()) {
            System.out.println(node.nodeName());
        }

        for (Node node : htmlNode.childNodes()) {
            System.out.println(node.nodeName());
        }

//        doc.head().appendChild(metaEle);
        document.body().addClass("body-styles-cls");
        Element aElement = new Element("a");
        aElement.attr("id","myGithubLink");
        aElement.attr("href","https://github.com/xfl12345");
        aElement.text("欢迎访问我的GitHub");
        aElement.appendTo(document.body());

        System.out.println("***********************");
        System.out.println(document);
        System.out.println("***********************");
        Document documentCopy = document.clone();
        Element targetElement = documentCopy.getElementById("myGithubLink");
        targetElement.attr("href", "https://github.com/xfl12345/MyBigData_help");

        System.out.println("***********************");
        System.out.println(document);
        System.out.println("***********************");
        System.out.println("***********************");
        System.out.println(documentCopy);
        System.out.println("***********************");

        File file = Resources.getResourceAsFile("staticVerificationEmail.html");
        InputStream inputStream = new FileInputStream(file);

//        FileReader fileReader = new FileReader(file);//定义一个fileReader对象，用来初始化BufferedReader
//        BufferedReader bufferedReader = new BufferedReader(fileReader);//new一个BufferedReader对象，将文件内容读取到缓存
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, (1 << 20));//1MiB 内存空间做缓冲
        byte[] tmpReadBuffer = new byte[(1 << 20)];//再浪费个1MiB内存空间用于从BufferInputStream读取数据
        /**
         * 由于 AbstractStringBuilder 里的 new char[x]  限制 x 为 int 型
         * AbstractStringBuilder 里的 count 成员是 int 型
         * StringBuilder 支持的字符串最大长度 理论上限是 2的32次方
         * 即 StringBuilder 理论上最大读入 4GiB 大小的字符串
          */
        StringBuilder stringBuilder = new StringBuilder((int)file.length());//定义一个字符串缓存，将字符串存放缓存中
        int currReadByteCount = 0;
        while ( (currReadByteCount = bufferedInputStream.read(tmpReadBuffer, 0, tmpReadBuffer.length)) != -1 ){
            stringBuilder.append(new String(tmpReadBuffer, 0, currReadByteCount));
        }
        bufferedInputStream.close();
        inputStream.close();
//        String stringInFile = "";
//        while ((stringInFile = bufferedReader.readLine()) != null) {//逐行读取文件内容，不读取换行符和末尾的空格
//            stringBuilder.append(stringInFile + "\n");//将读取的字符串添加换行符后累加存放在缓存中
////            System.out.println(emailHtmlOriginString);
//        }

//        char[] testCharBuffer = new char[(int) file.length()];
//        System.out.println("The number of characters read:" + bufferedReader.read(testCharBuffer));
//        String emailHtmlOriginString = new String(testCharBuffer);


        String emailHtmlOriginString = stringBuilder.toString();
        System.out.println(emailHtmlOriginString);
        System.out.println("file.length()="+file.length());
        System.out.println("stringBuilder.capacity()="+stringBuilder.capacity());
        System.out.println("emailHtmlOriginString.length()="+emailHtmlOriginString.length());

        System.out.println("***********************");

//        String emailHtmlOriginString;
//        Document emailHtmlDocument = Jsoup.parse(input, StandardCharsets.UTF_8.name());
        Document emailHtmlDocument = Jsoup.parse(emailHtmlOriginString);
        // 设置邮件里显示的网站名称
        Element websiteNameElement = emailHtmlDocument.getElementById("websiteName");
        websiteNameElement.text("jsp_netdisk");
        // 设置邮件正文内容的标题（非邮件自身属性里的标题，这个是写在邮件正文里的）
        Element contentTitleElement = emailHtmlDocument.getElementById("contentTitle");
        contentTitleElement.text("绑定邮箱");
        // 先 清空 邮件模板里的正文内容
        Element contentMainBodyElement = emailHtmlDocument.getElementById("contentMainBody");
        int vaildMinute = 10;
        contentMainBodyElement.html("<p>您的验证码： <b>12345678</b> （"+vaildMinute+"分钟内有效）</p>");

        System.out.println(emailHtmlDocument);
        System.out.println("***********************");



        {
            Element p = new Element("p");
            p.text("您的邮箱验证码是：");
            Element b = new Element("b");
            b.attr("id","code");
            b.text("666xfl");
            b.appendTo(p);
            System.out.println(p);
        }


    }
}
