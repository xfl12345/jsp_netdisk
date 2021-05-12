package com.github.xfl12345.jsp_netdisk.model.utils.jdbc;

import com.github.xfl12345.jsp_netdisk.StaticSpringApp;
import com.github.xfl12345.jsp_netdisk.appconst.MyConst;
import com.github.xfl12345.jsp_netdisk.model.utils.MyPropertiesUtils;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.Properties;

public class MyDataSource extends PooledDataSource {

    private final Logger logger = LoggerFactory.getLogger(MyDataSource.class);

    private SqlConnectionUrlParameter sqlUrlParam;
    private final String db_init_sql_file_relative_path;
    private final String db_restart_init_sql_file_relative_path;
    private final String db_url_base_parameter_file_relative_path;
    private final String db_url_additional_parameter_file_relative_path;
    public boolean isInitSucceed = false;
    private Properties confProp;

    static {
//        logger.debug("Resource root="+Thread.currentThread().getContextClassLoader().getResource("").getPath());
    }

    private void debugSomething() {
        logger.debug(this.toString());
        logger.debug(this.getClass().getClassLoader().toString());
    }

    public MyDataSource() throws SQLException, IOException {
        this("db_init.sql",
                "db_restart_init.sql",
                "db_url_base_parameter.properties",
                "db_url_additional_parameter.properties");
    }

    public MyDataSource(String db_init_sql_file_relative_path,
                        String db_restart_init_sql_file_relative_path,
                        String db_url_base_parameter_file_relative_path,
                        String db_url_additional_parameter_file_relative_path) throws SQLException, IOException {
        this.db_init_sql_file_relative_path = db_init_sql_file_relative_path;
        this.db_restart_init_sql_file_relative_path = db_restart_init_sql_file_relative_path;
        this.db_url_base_parameter_file_relative_path = db_url_base_parameter_file_relative_path;
        this.db_url_additional_parameter_file_relative_path = db_url_additional_parameter_file_relative_path;
        debugSomething();
        initWithJava();
        if (isInitSucceed)
            logger.info("Mybatis datasource ready!");
        else
            logger.info("Mybatis datasource initialization failed!");
    }

    private void executeSqlFile(Connection connection, String sqlFileRelativePath) throws IOException, SQLException {
        ScriptRunner runner = new ScriptRunner(connection);
        Resources.setCharset(StandardCharsets.UTF_8); //设置字符集,不然中文乱码插入错误
        runner.setLogWriter(null);//设置是否输出日志
        // 绝对路径读取
        //Reader read = new FileReader(new File("f:\\test.sql"));
        // 从class目录下直接读取
        //System.setProperty("console.encoding", "UTF-8");
        //logger.debug(Thread.currentThread().getContextClassLoader().getResource("").getPath());
        Reader read = Resources.getResourceAsReader(sqlFileRelativePath);
        runner.runScript(read);
        read.close();
        connection.close();
    }

    private void initWithJava() throws SQLException, IOException {
        MyPropertiesUtils myPropertiesUtils = StaticSpringApp.getBean(MyPropertiesUtils.class);
        // 加载 sql URL链接基本参数
        sqlUrlParam = new SqlConnectionUrlParameter(
                new BaseSqlConnectionUrlParameter(myPropertiesUtils.loadPropFromFile(db_url_base_parameter_file_relative_path)) );
        // 加载 sql URL附加属性
        confProp = myPropertiesUtils.loadPropFromFile(db_url_additional_parameter_file_relative_path);
        // 浅拷贝一个Properties对象，删除其中的敏感数据，用以展示完整的JDBC URL
        Properties tmpConfProp = (Properties) confProp.clone();
        tmpConfProp.remove("user");
        tmpConfProp.remove("password");
        // 创建一个临时的数据库连接，完成数据库初始化
        // 构建一个不带附加参数的JDBC URL
        String tmpJdbcConnectionURL = sqlUrlParam.getSqlConnUrlWithConfigProp(MyConst.INFORMATION_SCHEMA_TABLE_NAME, confProp);
        logger.info("Temporary JDBC URL=" + tmpJdbcConnectionURL);
        // 利用UnpooledDataSource构造器从Properties对象加载JDBC MYSQL URL的附加参数
        DataSource tmpDataSource = new UnpooledDataSource(sqlUrlParam.getSqlServerDriverName(), tmpJdbcConnectionURL, confProp);
        // 创建一个临时连接，用于试探MySQL数据库
        Connection conn2 = tmpDataSource.getConnection();
        logger.info("Database server connected.Checking database.");
        // 检查 MySQL中 某个数据库是否存在（其它数据库暂未适配，所以这个“MyDataSource”并非万能）
        String testDB_sql = "select * from information_schema.SCHEMATA where SCHEMA_NAME = '"
                + sqlUrlParam.getSpecificDbName() + "'";
        logger.info(testDB_sql);
        ResultSet rs = conn2.createStatement().executeQuery(testDB_sql);
        if (rs.next()) {
            logger.info("Database is exist!Initiation will process.");
            executeSqlFile(conn2, db_restart_init_sql_file_relative_path);
        } else {
            logger.info("Database is not exist!Database setup will process.");
            executeSqlFile(conn2, db_init_sql_file_relative_path);
        }
        logger.info("Database initiated! ");
        conn2.close();
        logger.info("Final JDBC URL=" + sqlUrlParam.getSqlConnUrlWithConfigProp(tmpConfProp));
        //配置连接池
        super.setDriver(sqlUrlParam.getSqlServerDriverName());
        super.setUrl(sqlUrlParam.getSqlConnectionUrl());
        super.setDriverProperties(confProp);
        super.setPoolMaximumActiveConnections(100);
        super.setPoolMaximumIdleConnections(20);
        isInitSucceed = true;
    }
}