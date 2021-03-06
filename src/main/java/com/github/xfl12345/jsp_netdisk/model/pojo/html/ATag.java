package com.github.xfl12345.jsp_netdisk.model.pojo.html;

import java.lang.reflect.Field;

@Deprecated
public class ATag extends BaseTag implements ITag {
    private String href;
    private String target;

    public ATag() {
        super();
        this.tagName = "a";
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getInnerHtmlValue() {
        return innerHtmlValue;
    }

    public void setInnerHtmlValue(String innerHtmlValue) {
        this.innerHtmlValue = innerHtmlValue;
    }

    @Override
    public String getHtmlCode() {
        StringBuilder tagAttribute = new StringBuilder();
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field f : fields) {
            try {
                if (f.get(this) != null) {
                    tagAttribute.append(f.getName()).append("='").append(f.get(this)).append("' ");
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        String htmlCode = "<" + this.tagName + " " + tagAttribute
                + ">" + innerHtmlValue + "</" + this.tagName + ">";
        return htmlCode;
    }

}
