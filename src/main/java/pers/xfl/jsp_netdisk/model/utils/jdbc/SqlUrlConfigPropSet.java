package pers.xfl.jsp_netdisk.model.utils.jdbc;

import com.mysql.jdbc.ConnectionPropertiesImpl;
import pers.xfl.jsp_netdisk.model.utils.MyReflect;

import java.util.TreeSet;

public class SqlUrlConfigPropSet {
    public static TreeSet confPropSet;
    static {
        MyReflect myReflect = new MyReflect();
        try {
            confPropSet = myReflect.getFieldNamesAsTreeSet( ConnectionPropertiesImpl.class );
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }
}
