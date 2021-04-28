import pers.xfl.jsp_netdisk.model.pojo.html.TagA;

import java.lang.reflect.Field;

public class ReflectTest {
    public static void main(String[] args) {
        //Field[] fields = cls.getFields(); // 获取所有公有的成员对象及函数
        Field[] fields = TagA.class.getDeclaredFields(); // 获取所有成员对象及函数
        //  System.out.println(((Field)Arrays.stream(fields).toArray()[0]).getName());

        boolean accessFlag;
        int count = 0;
        for (Field f : fields) {
            accessFlag = f.isAccessible();
//            if (!accessFlag)
//                f.setAccessible(true);// 暴力反射。 私有的也可以被访问。
            // System.out.println(f);
            System.out.println("成员名称:" + f.getName());
        }


        TagA tagA = new TagA();
        tagA.setHref("666");
        tagA.setTarget("888");
        tagA.setInnerHtmlValue("123456");

        System.out.println(tagA.getHtmlCode());
    }
}
