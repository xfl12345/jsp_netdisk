package com.github.xfl12345.jsp_netdisk.model.utils.jdbc;

import java.util.Properties;

public class SqlConnectionUrlParameter extends BaseSqlConnectionUrlParameter {

    public SqlConnectionUrlParameter() {
        super();
    }

    public SqlConnectionUrlParameter(BaseSqlConnectionUrlParameter base) {
        super(base);
    }

    /**
     * 是否更新了部分属性的值？
      */
    public boolean is_updated = false;

    /**
     * JDBC URL
     */
    private String sqlConnectionUrl = null;

    /**
     * 构造并返回JDBC URL的基础URL（不包含数据库名称、不带附加参数）
     * @return JDBC URL的基础URL
     */
    public String getSqlConnectionBaseUrl() {
        if (is_updated || sqlConnectionUrl == null) {
            sqlConnectionUrl = (sqlProtocal + ":" +
                    sqlSubProtocal + "://" +
                    sqlServerAddress + ":" +
                    sqlServerPort + "/");
            is_updated = false;
        }
        return sqlConnectionUrl;
    }

    /**
     * 构造并返回默认的不带附加参数JDBC URL
     * @return JDBC URL
     */
    public String getSqlConnectionUrl() {
        return getSqlConnectionUrl(null);
    }

    /**
     * 指定数据库名称，构造并返回完整的不带附加参数JDBC URL
     * @param dbName 数据库名称
     * @return JDBC URL
     */
    public String getSqlConnectionUrl(String dbName) {
        if(dbName == null)
            return getSqlConnectionBaseUrl() + specificDbName;
        return getSqlConnectionBaseUrl() + dbName;
    }

    /**
     * 构造并返回JDBC URL里的附加参数
     * @param confProp JDBC URL的附加参数
     * @return 以字符串的形式返回JDBC URL里的附加参数
     */
    private String getAdditionalParameter(Properties confProp){
        String sqlUrlParam = "";
        if( !confProp.stringPropertyNames().isEmpty() ){
            for (String propName : confProp.stringPropertyNames() ) {
                sqlUrlParam += propName + "=" + confProp.getProperty(propName) + "&";
            }
            sqlUrlParam = sqlUrlParam.substring(0, sqlUrlParam.length() - 1);
        }
        return sqlUrlParam;
    }

    /**
     * 构造并返回完整的带附加参数的JDBC URL
     * @param confProp JDBC URL的附加参数
     * @return JDBC URL
     */
    public String getSqlConnUrlWithConfigProp(Properties confProp) {
        return getSqlConnUrlWithConfigProp(null, confProp);
    }

    /**
     * 指定数据库名称，构造并返回完整的带附加参数的JDBC URL
     * @param confProp JDBC URL的附加参数
     * @return JDBC URL
     */
    public String getSqlConnUrlWithConfigProp(String dbName, Properties confProp) {
        return getSqlConnectionUrl(dbName) + "?" + getAdditionalParameter(confProp);
    }

    @Override
    public void setSqlServerDriverName(String sqlServerDriverName) {
        super.setSqlServerDriverName(sqlServerDriverName);
        is_updated = true;
    }

    @Override
    public void setSqlProtocal(String sqlProtocal) {
        super.setSqlProtocal(sqlProtocal);
        is_updated = true;
    }

    @Override
    public void setSqlSubProtocal(String sqlSubProtocal) {
        super.setSqlSubProtocal(sqlSubProtocal);
        is_updated = true;
    }

    @Override
    public void setSqlServerAddress(String sqlServerAddress) {
        super.setSqlServerAddress(sqlServerAddress);
        is_updated = true;
    }

    @Override
    public void setSqlServerPort(String sqlServerPort) {
        super.setSqlServerPort(sqlServerPort);
        is_updated = true;
    }

    @Override
    public void setSpecificDbName(String specificDbName){
        super.setSpecificDbName(specificDbName);
        is_updated = true;
    }

}
