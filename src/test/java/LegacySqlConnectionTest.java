import org.apache.ibatis.session.SqlSession;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import pers.xfl.jsp_netdisk.StaticSpringApp;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class LegacySqlConnectionTest  {

    public static void main(String[] args) throws SQLException {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("application_context.xml");
        String sql = "SELECT * from tb_account";
        SqlSession sqlSession = StaticSpringApp.getSqlSession();
        Connection connection = sqlSession.getConnection();
        ResultSet rs = connection.prepareStatement(sql).executeQuery();
        ResultSetMetaData rsmd = rs.getMetaData();
        // 获取返回结果矩阵的列数
        int colCount = rsmd.getColumnCount();
        System.out.println();
        while (rs.next()) {
            int i;
            for(i = 1; i <= colCount; i++){
                System.out.print( rs.getString(i) + ',');
            }
            System.out.print('\n');
        }
        try {
            connection.close();
            sqlSession.close();
        } catch (
                SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
