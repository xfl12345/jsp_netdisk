package com.github.xfl12345.jsp_netdisk.model.utility.jdbc;


import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.executor.ErrorContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import com.github.xfl12345.jsp_netdisk.appconst.MyConst;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

@Deprecated
public class MybatisUtils {
    private static SqlConnectionUrlParameter sql_url_param;
    private static String db_name = "jsp_netdisk";
    private final static String db_init_sql_file_relative_path = "db_init.sql";
    private final static String db_url_base_parameter_file_relative_path = "db_url_base_parameter.properties";
    private final static String db_url_additional_parameter_file_relative_path = "db_url_additional_parameter.properties";
    private final static String mybatis_mappers_xml_dir_relative_path = "mybatis/mappings";
    private final static String mybatis_configuration_file_relative_path = "mybatis/mybatis_configuration.xml";
    private static boolean is_init_succeed = false;

    private static SqlSessionFactory sqlSessionFactory;
    private static PooledDataSource dataSource;
    private static TransactionFactory transactionFactory;
    private static Environment environment;
    private static Configuration configuration;


    static {
        System.out.println("Classpath root="+Thread.currentThread().getContextClassLoader().getResource("").getPath());
//        initWithXml();
        initWithJava();
    }

    public static SqlSessionFactory getSqlSessionFactory(){
        return sqlSessionFactory;
    }

    public static SqlSession getSqlSession() throws Exception{
        return sqlSessionFactory.openSession();
    }

    public static DataSource getDataSource() {
        return dataSource;
    }

    public static TransactionFactory getTransactionFactory() {
        return transactionFactory;
    }

    public static Environment getEnvironment() {
        return environment;
    }

    public static Configuration getConfiguration() {
        return configuration;
    }

    /**
     * ??????properties?????????Properties??????
     * @param resPath ????????????
     * @return ????????????Properties??????
     */
    public static Properties loadPropFromFile(String resPath){
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
     * @param resPath properties?????????????????????
     * @param obj ?????????????????????????????????????????????
     * @return ????????????Properties??????
     */
    public static Properties loadProp2obj(String resPath, Object obj){
        Properties properties = null;
        try{
            properties = loadPropFromFile(resPath);
            Class cls = obj.getClass();
            Field[] fields = cls.getDeclaredFields();
            for(Field f : fields){
                if (Modifier.isFinal(f.getModifiers()))
                    continue;
                f.setAccessible(true);// ??????????????? ??????????????????????????????
                f.set(obj, properties.getProperty(f.getName()));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return properties;
    }

    /**
     * ??????sql???????????????????????????????????????
     * @param conn ????????????Connection??????
     */
    private static void dbInit(Connection conn)
    {
        try {
            ScriptRunner runner = new ScriptRunner(conn);
            Resources.setCharset(StandardCharsets.UTF_8); //???????????????,??????????????????????????????
            runner.setLogWriter(null);//????????????????????????
            // ??????????????????
            //Reader read = new FileReader(new FileOperation("f:\\test.sql"));
            // ???class?????????????????????
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

    private static void initWithJava() {
        try {
            // ?????? sql URL??????????????????
            BaseSqlConnectionUrlParameter base = new BaseSqlConnectionUrlParameter();
            loadProp2obj(db_url_base_parameter_file_relative_path, base);
            sql_url_param = new SqlConnectionUrlParameter(base);
            db_name = sql_url_param.getSpecificDbName();
//            System.out.println(must_be_exist_db_name + " " + db_name);

            // ?????? sql URL????????????
            Properties confProp = loadPropFromFile(db_url_additional_parameter_file_relative_path);

            // ???????????????????????????????????????????????????????????????
            String tmpJdbcConnectionURL = sql_url_param.getSqlConnUrlWithConfigProp(MyConst.INFORMATION_SCHEMA_TABLE_NAME, confProp);
//            System.out.println(sql_url_param.getSqlConnUrlWithConfigProp(MyConst.information_schema, confProp));
            // ??????PooledDataSource????????????Properties????????????JDBC MYSQL URL???????????????
            DataSource tmpDataSource = new PooledDataSource(sql_url_param.getSqlServerDriverName(), tmpJdbcConnectionURL, confProp);
            // ???????????????????????????????????????MySQL?????????
            Connection conn2 = tmpDataSource.getConnection();

            System.out.println("Database server connected.Checking database.");
            // ?????????????????????????????????
            String testDB_sql = "select * from information_schema.SCHEMATA where SCHEMA_NAME = '"+ db_name +"'";
            System.out.println(testDB_sql);
            try {
                ResultSet rs = conn2.createStatement().executeQuery(testDB_sql);
                if(rs.next()){
                    System.out.print("Database is exist! ");
                }
                else{
                    System.out.print("Database is not exist!Initiation will process.");
                    dbInit(conn2);
                    System.out.print("Database initiated! ");
                }
                conn2.close();
            } catch (SQLException ex) {
                Logger.getLogger(MybatisUtils.class.getName()).log(Level.SEVERE, null, ex);
            }

            //???????????????
            dataSource = new PooledDataSource(sql_url_param.getSqlServerDriverName(),
                    sql_url_param.getSqlConnectionUrl(db_name), confProp);
            dataSource.setPoolMaximumActiveConnections(100);
            dataSource.setPoolMaximumIdleConnections(20);
            //??????
            transactionFactory = new JdbcTransactionFactory();
            //????????????
            environment = new Environment("development", transactionFactory, dataSource);
            //????????????
            configuration = new Configuration(environment);
            //??????????????????
            configuration.setMapUnderscoreToCamelCase(true);


            //???????????????Mapper????????? ???????????????????????????????????????mapper xml?????????????????????
//            configuration.addMapper(Interface_tb_account.class);
//            InputStream tmp_input_stream = Resources.getResourceAsStream("mybatis/mappings/tb_account.xml");
//            XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(
//                    tmp_input_stream ,
//                    configuration,
//                    tmp_input_stream.toString(),
//                    null);
//            xmlMapperBuilder.parse();
            String XML_RESOURCE_PATTERN = ResourcePatternResolver.CLASSPATH_URL_PREFIX + mybatis_mappers_xml_dir_relative_path +"/*.xml";
            System.out.println("Mybatis Mappers XML_RESOURCE_PATTERN="+XML_RESOURCE_PATTERN);
            ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
            Resource[]  mybatis_mappers_resources= resourcePatternResolver.getResources(XML_RESOURCE_PATTERN);
            for (Resource resource : mybatis_mappers_resources) {
                try {
                    System.out.println("Mybatis Mappers Resource="+resource.getURI()+" matched,loading...");
                    XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(resource.getInputStream(), configuration, resource.toString(), configuration.getSqlFragments());
                    xmlMapperBuilder.parse();
                    System.out.println("Mybatis Mappers Resource="+resource.getURI()+" loaded.");
                } finally {
                    ErrorContext.instance().reset();
                }
            }
            // ?????????sqlSessionFactory
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);

            is_init_succeed = true;
            System.out.println("Mybatis ready!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void initWithXml() {
        try {
            InputStream inputStream = Resources.getResourceAsStream(mybatis_configuration_file_relative_path);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

            is_init_succeed = true;
            System.out.println("Mybatis ready!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}