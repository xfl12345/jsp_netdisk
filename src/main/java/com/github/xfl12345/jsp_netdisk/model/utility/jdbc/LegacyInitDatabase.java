package com.github.xfl12345.jsp_netdisk.model.utility.jdbc;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Deprecated
public class LegacyInitDatabase {
    private static Connection conn;
    private static final String sqlServerName ="mysql";
    private static final String sqlServerAddress ="localhost";
    private static final String sqlServerPort ="3306";
    private static final String dbName ="jsp_netdisk";
    private static final String sqlUseUnicode ="true";
    private static final String sqlCharacterEncoding ="utf8";
    private static final String sqlLoginUsername="jsp_netdisk";
    private static final String sqlLoginPassword="change_me_pw";
    private static String sqlURL;
    private final static String mustBeExistDBname = "information_schema";
    private final static String db_init_sql_file_relative_path = "db_init.sql";

    public static Connection getConnection()
    {
        Connection conn2 = null;
        conn2 = justGetConnection(mustBeExistDBname);
        System.out.println("Database connected.Checking database.");
        String testDB_sql = "select * from information_schema.SCHEMATA where SCHEMA_NAME = '"+ dbName +"'";
        System.out.println(testDB_sql);
        try {
            ResultSet rs = conn2.createStatement().executeQuery(testDB_sql);
            if(rs.next()){
                conn2.close();
                System.out.print("Database is exist! ");
            }
            else{
                System.out.print("Database is not exist!Initiation will process. ");
                db_init(conn2);
                System.out.print("Database initiated! ");
            }
            conn2 = justGetConnection(dbName);
            System.out.println("Database connected.");
        } catch (SQLException ex) {
            Logger.getLogger(LegacyInitDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }

        conn = conn2;
        return conn;
    }
    private static Connection justGetConnection(String DBname)
    {
        Connection conn2 = null;
        try {
            sqlURL = "jdbc:"+
                    sqlServerName + "://" + sqlServerAddress + ":" +
                    sqlServerPort + "/" + DBname + "?" +
                    "useUnicode=" + sqlUseUnicode + "&" +
                    "characterEncoding=" + sqlCharacterEncoding;
            Class.forName("com.mysql.jdbc.Driver");
            conn2 =   DriverManager.getConnection(sqlURL,sqlLoginUsername,sqlLoginPassword);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(LegacyInitDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conn2;
    }
//    private static Connection justGetConnection(String DBname)
//    {
//        SqlSession sqlSession = test_mybatis.getSqlSession();
//        Connection connection = sqlSession.getConnection();
//        return connection;
//    }

    private static void db_init(Connection conn2)
    {
        //TODO 还没有魔改完
        try {
            ScriptRunner runner = new ScriptRunner(conn2);
            Resources.setCharset(StandardCharsets.UTF_8); //设置字符集,不然中文乱码插入错误
            runner.setLogWriter(null);//设置是否输出日志
            // 绝对路径读取
            //Reader read = new FileReader(new FileOperation("f:\\test.sql"));
            // 从class目录下直接读取
            //System.setProperty("console.encoding", "UTF-8");
            //System.out.println(Thread.currentThread().getContextClassLoader().getResource("").getPath());
            Reader read = Resources.getResourceAsReader(db_init_sql_file_relative_path);
            runner.runScript(read);
            runner.closeConnection();
            conn2.close();
            System.out.print("sql init finished.");
        } catch (Exception e) {
            System.out.print("sql init went wrong.");
            e.printStackTrace();
        }
    }

}
