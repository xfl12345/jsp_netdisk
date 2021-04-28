import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Tag;

import java.util.List;

public class StudyJsoup {
    public static void main(String[] args) {
        System.out.println(Tag.isKnownTag("a"));
        Document document = Jsoup.parse("<!doctype html><html></html>");
        Element metaElement = new Element("meta");
        metaElement.attr("http-equiv","Content-Type");
        metaElement.attr("content","text/html; charset=UTF-8");
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
        aElement.attr("href","https://github.com/xfl12345");
        aElement.text("欢迎访问我的GitHub");
        aElement.appendTo(document.body());

        System.out.println(document);
    }
}
