package com.github.xfl12345.jsp_netdisk.model.utility.jdbc;

import com.github.xfl12345.jsp_netdisk.StaticSpringApp;
import com.github.xfl12345.jsp_netdisk.appconst.MyConst;
import com.github.xfl12345.jsp_netdisk.model.utility.MyPropertiesUtils;
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
        Resources.setCharset(StandardCharsets.UTF_8); //???????????????,??????????????????????????????
        runner.setLogWriter(null);//????????????????????????
        // ??????????????????
        //Reader read = new FileReader(new FileOperation("f:\\test.sql"));
        // ???class?????????????????????
        //System.setProperty("console.encoding", "UTF-8");
        //logger.debug(Thread.currentThread().getContextClassLoader().getResource("").getPath());
        Reader read = Resources.getResourceAsReader(sqlFileRelativePath);
        runner.runScript(read);
        read.close();
        connection.close();
    }

    private void initWithJava() throws SQLException, IOException {
        MyPropertiesUtils myPropertiesUtils = StaticSpringApp.getBean(MyPropertiesUtils.class);
        // ?????? sql URL??????????????????
        sqlUrlParam = new SqlConnectionUrlParameter(
                new BaseSqlConnectionUrlParameter(myPropertiesUtils.loadPropFromFile(db_url_base_parameter_file_relative_path)) );
        // ?????? sql URL????????????
        confProp = myPropertiesUtils.loadPropFromFile(db_url_additional_parameter_file_relative_path);
        // ???????????????Properties????????????????????????????????????????????????????????????JDBC URL
        Properties tmpConfProp = (Properties) confProp.clone();
        tmpConfProp.remove("user");
        tmpConfProp.remove("password");
        // ???????????????????????????????????????????????????????????????
        // ?????????????????????????????????JDBC URL
        String tmpJdbcConnectionURL = sqlUrlParam.getSqlConnUrlWithConfigProp(MyConst.INFORMATION_SCHEMA_TABLE_NAME, confProp);
        logger.info("Temporary JDBC URL=" + tmpJdbcConnectionURL);
        // ??????UnpooledDataSource????????????Properties????????????JDBC MYSQL URL???????????????
        DataSource tmpDataSource = new UnpooledDataSource(sqlUrlParam.getSqlServerDriverName(), tmpJdbcConnectionURL, confProp);
        // ???????????????????????????????????????MySQL?????????
        Connection conn2 = tmpDataSource.getConnection();
        logger.info("Database server connected.Checking database.");
        // ?????? MySQL??? ???????????????????????????????????????????????????????????????????????????MyDataSource??????????????????
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
        //???????????????
        super.setDriver(sqlUrlParam.getSqlServerDriverName());
        super.setUrl(sqlUrlParam.getSqlConnectionUrl());
        super.setDriverProperties(confProp);
        super.setPoolMaximumActiveConnections(100);
        super.setPoolMaximumIdleConnections(20);
        isInitSucceed = true;
    }
}