import java.io.File;
import java.net.URL;

/**
 * java相对路径、绝对路径及类路径的测试
 * source code URL=https://www.cnblogs.com/zj0208/p/8953303.html
 */
public class StudyFile {
    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getContextClassLoader().getResource(""));
        System.out.println(StudyFile.class.getClassLoader().getResource(""));
        System.out.println(ClassLoader.getSystemResource(""));
        System.out.println(StudyFile.class.getResource(""));
        System.out.println(StudyFile.class.getResource("/"));

        System.out.println(new File("/").getAbsolutePath());
        System.out.println(System.getProperty("user.dir"));
        System.out.println(System.getProperty("file.encoding"));

        StudyFile studyFile = new StudyFile();
        try{
            studyFile.testRelativePath();
            studyFile.testAbsolutePath();
            studyFile.testClassPath();
            studyFile.testSprit();
            studyFile.testName();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 测试相对路径是相对谁
     * -- 相对于部署项目的文件夹（AppServer）
     */
    // @org.junit.Test
    public void testRelativePath() throws Exception {

        String filePath = "test//t.txt";
        File file = new File(filePath);
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
        }

        System.out.println(file.getAbsolutePath());
        // E:\workspace\AppServer\test\t.txt
    }

    /**
     * 测试绝对路径
     */
    // @org.junit.Test
    public void testAbsolutePath() throws Exception {
        String filePath = "D:\\path\\test.txt";

        File file = new File(filePath);
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
        }

        System.out.println(file.getName()); // test.txt
        System.out.println(file.getAbsolutePath()); // D:\path\test.txt
    }

    /**
     * 获取ClassPath(类路径)
     */
    // @org.junit.Test
    public void testClassPath() throws Exception {
        /*
             来个对比（各种情况下ClassPath的值）：
             1) 直接junit运行方法时打印：(给这个类单独创建了一个ClassPath)
             /E:/workspace/AppServer/target/test-classes/

             2) Eclipse启动tomcat时打印(tomcat插件中的ClassPath)：
             /E:/workspace/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/AppServer/WEB-INF/classes/

             3) 单独启动tomcat时打印(tomcat中的类路径)：
             /E:/apache-tomcat-7.0.62/webapps/AppServer/WEB-INF/classes
         */
        // 获取类路径
        URL url = this.getClass().getResource("/");
        // file:/E:/workspace/AppServer/target/test-classes/

        String path = url.getPath();

        // 看看类路径下都有啥
        File file = new File(path);

        // 直接junit运行方法
        for (File f : file.listFiles()) {
            System.out.println(f.getName()); // 还没有文件被编译，啥也没有
        }
    }

    /**
     * 测试路径中的正反斜杠
     */
    // @org.junit.Test
    public void testSprit() throws Exception {
        // 文件已经存在
        String filePath = null;

        /*
         * 正斜杠'/'
         */
        filePath = "D:/path/test.txt"; // D:\path\test.txt
        filePath = "D://path//test.txt"; // D:\path\test.txt
        filePath = "D:/path//test.txt"; // D:\path\test.txt
        filePath = "D:////path////test.txt"; // D:\path\test.txt

        /*
         * 反斜杠'\'
         */
        filePath = "D:\\path\\test.txt"; // D:\path\test.txt
        // filePath = "D:\path\test.txt"; // 编译都通过不了啊，\t是一个制表符
        // filePath = "D:\\\path\\test.txt"; // 编译都通过不了啊

        // 正反斜杠混合使用
        filePath = "D:\\path/test.txt"; // D:\path\test.txt
        filePath = "D:/path\\test.txt"; // D:\path\test.txt

        File file = new File(filePath);
        System.out.println(file.getAbsolutePath());
    }

    @org.junit.Test
    public void testName() throws Exception {

        String filePath = null;

        filePath = "D:/path/test.txt"; // D:/path/test.txt
        System.out.println(filePath);

        filePath = "D://path//test.txt"; // D://path//test.txt
        System.out.println(filePath);

        filePath = "D:/path//test.txt"; // D:/path//test.txt
        System.out.println(filePath);

        filePath = "D:////path////test.txt"; // D:////path////test.txt
        System.out.println(filePath);

        /*
         * 反斜杠'\'
         */
        filePath = "D:\\path\\test.txt"; // D:\path\test.txt
        System.out.println(filePath);

        // 正反斜杠混合使用
        filePath = "D:\\path/test.txt"; // D:\path/test.txt
        System.out.println(filePath);

        filePath = "D:/path\\test.txt"; // D:/path\test.txt
        System.out.println(filePath);

    }

    /**
     * 总结：
     * 1) 相对路径
     *
     *         相对路径：是相对于application（服务）目录所在的路径。
     *
     *         比如：
     *             相对路径为"test/t.txt", 服务目录为："D:/App"
     *             则t.txt的绝对路径为："D:/App/test/t.txt"
     *
     * 2) 绝对路径
     *
     *         没什么好说的。
     *
     * 3) 类路径
     *
     *         a. Eclipse中右键运行(为当前类单独创建了一个类路径)：
     *             /E:/workspace/AppServer/target/test-classes/
     *
     *         b. Eclipse中启动tomcat(tomcat插件中的类路径)：：
     *             /E:/workspace/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/AppServer/WEB-INF/classes/
     *
     *         c. tomcat中启动start.bat(tomcat服务中的类路径):
     *             /E:/apache-tomcat-7.0.62/webapps/AppServer/WEB-INF/classes
     *
     * 4) 路径中的正反斜杠（/ \）
     *
     *         a. '/' 正斜杠
     *             怎么用都是对的，无论是单斜杠，双斜杠，多斜杠 或 混合使用，都能正确的解析文件路径。
     *
     *         b. '\' 反斜杠
     *             只能使用双斜杠'\\'.
     *             单斜杠，多斜杠 或 混合使用都会报错。编译都不能通过。
     *
     *         c. 正反斜杠混合使用
     *             反斜杠只能使用双斜杠'\\', 正斜杠随意。 都能正确解析出路径。  "D:/aaa\\/bb.txt"，这种写法也能解析。
     *
     *         d. 反双斜杠'\\',运行时打印字符串时会变成'\'。
     *            正斜杠,运行时打印字符串,打印结果和编译前一致。
     */

}