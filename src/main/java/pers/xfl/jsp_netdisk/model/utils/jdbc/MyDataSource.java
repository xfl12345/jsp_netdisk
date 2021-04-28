package pers.xfl.jsp_netdisk.model.utils.jdbc;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import pers.xfl.jsp_netdisk.model.appconst.MyConst;

import javax.sql.DataSource;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyDataSource extends PooledDataSource {
    private SqlConnectionUrlParameter sqlUrlParam;
    private String db_init_sql_file_relative_path = "db_init.sql";
    private String db_restart_init_sql_file_relative_path = "db_restart_init.sql";
    private String db_url_base_parameter_file_relative_path = "db_url_base_parameter.properties";
    private String db_url_additional_parameter_file_relative_path = "db_url_additional_parameter.properties";
    public boolean isInitSucceed = false;
    private Properties confProp;

    static {
//        System.out.println("Resource root="+Thread.currentThread().getContextClassLoader().getResource("").getPath());
    }

    private void debugSomething(){
        System.out.println(this);
        System.out.println(this.getClass().getClassLoader().toString());
    }

    private void setupDataSource(){
//        debugSomething();
        initWithJava();
        //配置连接池
        this.setDriver(sqlUrlParam.getSqlServerDriverName());
        this.setUrl(sqlUrlParam.getSqlConnectionUrl());
        this.setDriverProperties(confProp);
        super.setPoolMaximumActiveConnections(100);
        super.setPoolMaximumIdleConnections(20);
        if(isInitSucceed)
            System.out.println("Mybatis datasource ready!");
        else
            System.out.println("Mybatis datasource initialization failed!");
    }

    public MyDataSource(String db_init_sql_file_relative_path,
                        String db_restart_init_sql_file_relative_path,
                        String db_url_base_parameter_file_relative_path,
                        String db_url_additional_parameter_file_relative_path) {
        this.db_init_sql_file_relative_path = db_init_sql_file_relative_path;
        this.db_restart_init_sql_file_relative_path = db_restart_init_sql_file_relative_path;
        this.db_url_base_parameter_file_relative_path = db_url_base_parameter_file_relative_path;
        this.db_url_additional_parameter_file_relative_path = db_url_additional_parameter_file_relative_path;
        setupDataSource();
    }

    public MyDataSource(){
        setupDataSource();
    }

    /**
     * 加载properties文件为Properties对象
     * @param resPath 文件路径
     * @return 返回一个Properties对象
     */
    public Properties loadPropFromFile(String resPath){
        Properties properties = null;
        try {
            InputStream inputStream = Resources.getResourceAsStream(resPath);
            properties = new Properties();
            properties.load(inputStream);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return properties;
    }

    /**
     * @param resPath properties配置文件的路径
     * @param obj 要被通过暴力反射填装数据的对象
     * @return 返回一个Properties对象
     */
    public Properties loadProp2obj(String resPath, Object obj){
        Properties properties = null;
        try{
            properties = loadPropFromFile(resPath);
            Class cls = obj.getClass();
            Field[] fields = cls.getDeclaredFields();
            for(Field f : fields){
                f.setAccessible(true);// 暴力反射。 私有的也可以被访问。
                f.set(obj, properties.getProperty(f.getName()));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return properties;
    }

    /**
     * 执行sql脚本文件，完成数据库完全初始化
     * @param conn 传入一个Connection对象
     */
    private void dbInit(Connection conn)
    {
        try {
            ScriptRunner runner = new ScriptRunner(conn);
            Resources.setCharset(StandardCharsets.UTF_8); //设置字符集,不然中文乱码插入错误
            runner.setLogWriter(null);//设置是否输出日志
            // 绝对路径读取
            //Reader read = new FileReader(new File("f:\\test.sql"));
            // 从class目录下直接读取
            //System.setProperty("console.encoding", "UTF-8");
            //System.out.println(Thread.currentThread().getContextClassLoader().getResource("").getPath());
            Reader read = Resources.getResourceAsReader(db_init_sql_file_relative_path);
            runner.runScript(read);
            conn.close();
            System.out.print("Running sql script from file finished.");
        } catch (Exception e) {
            System.out.print("Failed to run sql script from file.");
            e.printStackTrace();
        }
    }

    /**
     * 执行sql脚本文件，系统重启后，完成对数据库失效的数据进行清除
     * @param conn 传入一个Connection对象
     */
    private void dbRestartInit(Connection conn)
    {
        try {
            ScriptRunner runner = new ScriptRunner(conn);
            Resources.setCharset(StandardCharsets.UTF_8); //设置字符集,不然中文乱码插入错误
            runner.setLogWriter(null);//设置是否输出日志
            // 绝对路径读取
            //Reader read = new FileReader(new File("f:\\test.sql"));
            // 从class目录下直接读取
            //System.setProperty("console.encoding", "UTF-8");
            //System.out.println(Thread.currentThread().getContextClassLoader().getResource("").getPath());
            Reader read = Resources.getResourceAsReader(db_restart_init_sql_file_relative_path);
            runner.runScript(read);
            conn.close();
            System.out.print("Running sql script from file finished.");
        } catch (Exception e) {
            System.out.print("Failed to run sql script from file.");
            e.printStackTrace();
        }
    }

    private void initWithJava() {
        try {
            // 加载 sql URL链接基本参数
            SqlConnectionUrlParameterBase base = new SqlConnectionUrlParameterBase();
            loadProp2obj(db_url_base_parameter_file_relative_path, base);
            sqlUrlParam = new SqlConnectionUrlParameter(base);
//            System.out.println(must_be_exist_db_name + " " + db_name);
            // 加载 sql URL附加属性
            confProp = loadPropFromFile(db_url_additional_parameter_file_relative_path);
            // 创建一个临时的数据库连接，完成数据库初始化
            // 构建一个不带附加参数的JDBC URL
            String tmpJdbcConnectionURL = sqlUrlParam.getSqlConnectionUrl(MyConst.information_schema);
            System.out.println("Temporary JDBC URL=" + tmpJdbcConnectionURL);
            // 利用PooledDataSource构造器从Properties对象加载JDBC MYSQL URL的附加参数
            DataSource tmpDataSource = new PooledDataSource(sqlUrlParam.getSqlServerDriverName(), tmpJdbcConnectionURL, confProp);
            // 创建一个临时连接，用于试探MySQL数据库
            Connection conn2 = tmpDataSource.getConnection();
            System.out.println("Database server connected.Checking database.");
            // 检查某个数据库是否存在
            String testDB_sql = "select * from information_schema.SCHEMATA where SCHEMA_NAME = '"
                    + sqlUrlParam.getSpecificDbName() +"'";
            System.out.println(testDB_sql);
            try {
                ResultSet rs = conn2.createStatement().executeQuery(testDB_sql);
                if(rs.next()){
                    System.out.print("Database is exist!Initiation will process.");
                    dbRestartInit(conn2);
                    System.out.print("Database initiated! ");
                }
                else{
                    System.out.print("Database is not exist!Database setup will process.");
                    dbInit(conn2);
                    System.out.print("Database initiated! ");
                }
                System.out.println();
                conn2.close();
            } catch (SQLException ex) {
                Logger.getLogger(MyDataSource.class.getName()).log(Level.SEVERE, null, ex);
            }
//            System.out.println("Final JDBC URL=" + sqlUrlParam.getSqlConnectionUrl());
            Properties tmpConfProp = (Properties) confProp.clone();
            tmpConfProp.remove("user");
            tmpConfProp.remove("password");
            System.out.println("Final JDBC URL=" + sqlUrlParam.getSqlConnUrlWithConfigProp(tmpConfProp));
            isInitSucceed = true;
        } catch (Exception e) {
            e.printStackTrace();
            isInitSucceed = false;
        }
    }
}