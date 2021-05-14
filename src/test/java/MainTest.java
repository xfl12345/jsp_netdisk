

import com.github.xfl12345.jsp_netdisk.appconst.api.result.SentEmailApiResult;
import com.github.xfl12345.jsp_netdisk.appconst.field.TbAccountField;
import com.github.xfl12345.jsp_netdisk.model.pojo.database.TbAccount;
import org.apache.ibatis.io.Resources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.github.xfl12345.jsp_netdisk.StaticSpringApp;
import com.github.xfl12345.jsp_netdisk.model.helloworld.impl.HelloWorld;
import com.github.xfl12345.jsp_netdisk.model.service.EmailVerificationService;
import com.github.xfl12345.jsp_netdisk.model.utility.jdbc.ContextFinalizer;

import java.io.File;
import java.util.Date;


public class MainTest {
    public static AbstractApplicationContext applicationContext;
    public static void main(String[] args) throws Exception {
        System.out.println("Classpath root="+Thread.currentThread().getContextClassLoader().getResource("").getPath());
        HelloWorld helloWorld = new HelloWorld();
        helloWorld.sayHelloWorld();

        LoggerContext logContext = (LoggerContext) LogManager.getContext(false);
        File conFile = Resources.getResourceAsFile("log4j2.xml");
        logContext.setConfigLocation(conFile.toURI());
        logContext.reconfigure();

        applicationContext = new ClassPathXmlApplicationContext("application_context.xml");

        EmailVerificationService emailVerificationService = applicationContext.getBean(EmailVerificationService.class);
//        emailVerificationService.generateEmailObj("1046539849@qq.com", "xfl666" );
//        Thread.sleep(1000 * 60 );
        TbAccount tbAccount = new TbAccount();
        tbAccount.setAccountId(666666L);
        tbAccount.setAccountStatus(TbAccountField.ACCOUNT_STATUS.EMAIL_NOT_ACTIVATED);
        tbAccount.setRegisterTimeInMs(0);
        tbAccount.setRegisterTime((new Date()).toString());
        tbAccount.setGender("0");
        tbAccount.setEmail("1046539849@qq.com");
        tbAccount.setUsername("xfl666");
        SentEmailApiResult sentEmailApiResult = emailVerificationService.sendEmail(tbAccount);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("begin to shutdown ... ");
            ContextFinalizer contextFinalizer = StaticSpringApp.getBean(ContextFinalizer.class);
            contextFinalizer.contextDestroyed(null);
            MainTest.applicationContext.close();
            /*try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            System.out.println("bye bye, app stopped ...");
        }));

        Thread.sleep(1000 * 30);
        System.exit(0);

//        // 获取返回结果矩阵的列数
//        int colCount = rsmd.getColumnCount();
//        System.out.println();
//        while (rs.next()) {
//            int i;
//            for(i = 1; i <= colCount; i++){
//                System.out.print( rs.getString(i) + ',');
//            }
//            System.out.print('\n');
//        }

//        String sql = "SELECT * from tb_account";
//
//        SqlSession sqlSession = MybatisUtils.getSqlSession();
//        Connection connection = sqlSession.getConnection();
//        ResultSet rs = connection.prepareStatement(sql).executeQuery();
//        ResultSetMetaData rsmd = rs.getMetaData();
//        // 获取返回结果矩阵的列数
//        int colCount = rsmd.getColumnCount();
//        System.out.println();
//        while (rs.next()) {
//            int i;
//            for(i = 1; i <= colCount; i++){
//                System.out.print( rs.getString(i) + ',');
//            }
//            System.out.print('\n');
//        }


//        String sql = "SELECT ordinal_position as `序号`, " +
//                "column_key as `键`, " +
//                "column_name as `字段名`, " +
//                "column_type as `数据类型`, " +
//                "is_nullable as `是否为空`, " +
//                "column_default as `默认值`, " +
//                "column_comment as `注释` " +
//                "FROM information_schema.columns " +
//                "WHERE table_schema ='jsp_netdisk' and table_name = 'tb_account'";
//
//        SqlSession sqlSession = MybatisUtils.getSqlSession();
//        Connection connection = sqlSession.getConnection();
//        ResultSet rs = connection.prepareStatement(sql).executeQuery();
//        ResultSetMetaData rsmd = rs.getMetaData();
//        // 获取返回结果矩阵的列数
//        int colCount = rsmd.getColumnCount();
//        System.out.println();
//        while (rs.next()) {
//            int i;
//            for(i = 1; i <= colCount; i++){
//                System.out.print( rs.getString(i) + ',');
//            }
//            System.out.print('\n');
//        }



    }



}
