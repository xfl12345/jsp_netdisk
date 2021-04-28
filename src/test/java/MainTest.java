

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import pers.xfl.jsp_netdisk.StaticSpringApp;
import pers.xfl.jsp_netdisk.model.helloworld.impl.HelloWorld;


public class MainTest {
    public static void main(String[] args) throws Exception {
        System.out.println("Classpath root="+Thread.currentThread().getContextClassLoader().getResource("").getPath());
        HelloWorld helloWorld = new HelloWorld();
        helloWorld.sayHelloWorld();

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("application_context.xml");
        UuidTest.main(null);

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
