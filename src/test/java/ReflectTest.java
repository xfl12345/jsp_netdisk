import com.github.xfl12345.jsp_netdisk.model.utility.MyReflectUtils;

import java.lang.reflect.Field;

public class ReflectTest {

    public int k = 123;

    public static void main(String[] args) throws Exception {
        ReflectTest reflectTest = new ReflectTest();
        MyReflectUtils  myReflectUtils = new MyReflectUtils();
        Field field = reflectTest.getClass().getDeclaredField("k");

        System.out.println( field.getType() );
        System.out.println( ( myReflectUtils.castToWrapperClassObject( Integer.class, "12345"))  );
        System.out.println( ( myReflectUtils.castToWrapperClassObject(field.getType(), "12345"))  );
        System.out.println( ( myReflectUtils.castToWrapperClassObject(field.getType(), "12345")).getClass().getTypeName()  );
        field.set(  reflectTest, myReflectUtils.castToWrapperClassObject(field.getType(), "12345")  );
        System.out.println("k=" + reflectTest.k );
    }
}
