import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

public class StudyString {

    public static void main(String[] args) {
        String str = "/xfl666/12345/home/path/to/file";
        String str2 = "/";

        String[] strArr = StringUtils.split(str, "/");

        System.out.println(strArr.length);
        for(String dirName : strArr){
            System.out.println(dirName);
        }

    }
}
