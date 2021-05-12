import java.util.concurrent.ConcurrentHashMap;

public class StudyMultiThread {
    public ConcurrentHashMap<String, String> emailAndCode;

    public static void main(String[] args) {
        //只有t1一分的时候,出现数据共享的问题
        StudyMultiThreadTestThread t1 = new StudyMultiThreadTestThread(0);

        //两个线程访问同一个对象
        new Thread(t1, "first").start();
        new Thread(t1, "second").start();
    }

}

