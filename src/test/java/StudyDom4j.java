import org.dom4j.*;
import com.github.xfl12345.jsp_netdisk.model.pojo.html.ATag;

import java.nio.charset.StandardCharsets;

public class StudyDom4j {
    public static void main(String[] args) {
        Document document = DocumentHelper.createDocument();
        document.setXMLEncoding(StandardCharsets.UTF_8.name());
        Element root = document.addElement("root");

        ATag tagA = new ATag();
        tagA.setHref("666");
        tagA.setTarget("888");
        tagA.setInnerHtmlValue("123456");

        Element node = null;
        try {
            Document kkk = DocumentHelper.parseText(tagA.getHtmlCode());

            System.out.println(kkk.asXML());
            node =  kkk.getRootElement();
//            node.setName("a");
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        root.add(node);

        System.out.println(document.asXML());
    }
}
