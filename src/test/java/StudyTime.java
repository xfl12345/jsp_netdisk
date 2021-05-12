import java.text.SimpleDateFormat;
import java.util.Date;

public class StudyTime {
    public static void main(String[] args) {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
//        simpleDateFormat
        long time = date.getTime();//这就是距离1970年1月1日0点0分0秒的毫秒数
        int millisecond = (int) (( time & 0x0FFFF )) % 1000;
        System.out.println(simpleDateFormat.format(date));
        System.out.println(millisecond);

    }
}
