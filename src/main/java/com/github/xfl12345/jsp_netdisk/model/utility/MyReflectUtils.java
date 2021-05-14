package com.github.xfl12345.jsp_netdisk.model.utility;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * 这是一个通过Java反射机制获取变量的一个工具类
 * 方便获取类的字段名称，同时极大地减少字段以常量字符串形式写死在程序里
 */
public class MyReflectUtils {
    public final HashMap<String, String> typeDic = new HashMap<>();

    public MyReflectUtils() {
        typeDic.put(int.class.getName(), Integer.class.getName());
        typeDic.put(double.class.getName(), Double.class.getName());
        typeDic.put(float.class.getName(), Float.class.getName());
        typeDic.put(long.class.getName(), Long.class.getName());
        typeDic.put(short.class.getName(), Short.class.getName());
        typeDic.put(byte.class.getName(), Byte.class.getName());
        typeDic.put(boolean.class.getName(), Boolean.class.getName());
        typeDic.put(char.class.getName(), Character.class.getName());
    }

    /**
     * String 转 任意基本类型的包装类
     * @param cls 基本类型
     * @param value String字符串
     * @return 基本类型的包装类的对象
     * @throws Exception 请务必保证目标类型有把String转成目标类型的构造方法
     */
    public <T> T castToWrapperClassObject(Class<T> cls, String value) throws Exception {
        String wrapperClassName = typeDic.get(cls.getName());
        if(wrapperClassName != null){
            return (T) castToWrapperClassObject(Class.forName(wrapperClassName), value);
        }
        return cls.getConstructor(String.class).newInstance(value);
    }

    public Field getFieldByName(Object obj, String FieldName) {
        Field res = null;
        try {
            res = obj.getClass().getDeclaredField(FieldName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return res;
    }

    public Map<String, Object> obj2Map(Object obj) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            map.put(field.getName(), field.get(obj));
        }
        return map;
    }

    public Object map2Obj(Map<String, Object> map, Class<?> cls) throws Exception {
        Object obj = cls.newInstance();
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            int mod = field.getModifiers();
            if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                continue;
            }
            field.setAccessible(true);
            field.set(obj, map.get(field.getName()));
        }
        return obj;
    }

    public TreeSet<String> getFieldNamesAsTreeSet(Class cls) throws IllegalAccessException, InstantiationException {
        Object obj = cls.newInstance();
        TreeSet<String> treeSet = new TreeSet<String>();
        Field[] fields = cls.getDeclaredFields(); // 获取所有成员对象及函数
        for (Field f : fields) {
            f.setAccessible(true);// 暴力反射。 私有的也可以被访问。
            treeSet.add(f.getName());
        }
        return treeSet;
    }

    public TreeSet<String> getFieldNamesAsArrayList(Class cls) throws IllegalAccessException, InstantiationException {
        Object obj = cls.newInstance();
        TreeSet<String> treeSet = new TreeSet<String>();
        Field[] fields = cls.getDeclaredFields(); // 获取所有成员对象及函数
        for (Field f : fields) {
            f.setAccessible(true);// 暴力反射。 私有的也可以被访问。
            treeSet.add(f.getName());
        }
        return treeSet;
    }

    private int demoFetch(Class cls, Object obj) {
        //Field[] fields = cls.getFields(); // 获取所有公有的成员对象及函数
        Field[] fields = cls.getDeclaredFields(); // 获取所有成员对象及函数
        //  System.out.println(((Field)Arrays.stream(fields).toArray()[0]).getName());

        boolean accessFlag;
        int count = 0;
        for (Field f : fields) {
            accessFlag = f.isAccessible();
            if (!accessFlag)
                f.setAccessible(true);// 暴力反射。 私有的也可以被访问。
            // System.out.println(f);
            try {
                System.out.println("成员名称:" + f.getName() +
                        " 成员修饰符: " + Modifier.toString(f.getModifiers()) +
                        " 成员数据类型: " + f.getGenericType().getTypeName() +
                        " 成员数据：" + f.get(obj));
                count++;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return count;
    }

    public void demo(Class cls) {
        Object obj;
        try {
            obj = cls.newInstance();
            int count = demoFetch(cls, obj);
            System.out.println("共计 " + count + " 个成员");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void demo(Object obj) {
        try {
            Class cls = obj.getClass();
            int count = demoFetch(cls, obj);
            System.out.println("共计 " + count + " 个成员");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
