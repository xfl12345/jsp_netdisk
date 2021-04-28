package pers.xfl.jsp_netdisk.model.utils.jdbc;

public class SqlConnectionUrlParameterBase {
    protected String sqlServerDriverName;

    protected String sqlProtocal = "jdbc";
    protected String sqlSubProtocal = "mysql";

    protected String sqlServerAddress;
    protected String sqlServerPort;

    protected String specificDbName = "jsp_netdisk";

    public SqlConnectionUrlParameterBase() {}

    /**
     * 通过克隆来构造对象
     * @param base 同一个类的对象
     */
    public SqlConnectionUrlParameterBase(SqlConnectionUrlParameterBase base) {
        this.sqlServerDriverName = base.getSqlServerDriverName();
        this.sqlProtocal = base.getSqlProtocal();
        this.sqlSubProtocal = base.getSqlSubProtocal();
        this.sqlServerAddress = base.getSqlServerAddress();
        this.sqlServerPort = base.getSqlServerPort();
        this.specificDbName = base.getSpecificDbName();
    }

    public String getSqlServerDriverName() {
        return sqlServerDriverName;
    }

    public void setSqlServerDriverName(String sqlServerDriverName) {
        this.sqlServerDriverName = sqlServerDriverName;
    }

    public String getSqlProtocal() {
        return sqlProtocal;
    }

    public void setSqlProtocal(String sqlProtocal) {
        this.sqlProtocal = sqlProtocal;
    }

    public String getSqlSubProtocal() {
        return sqlSubProtocal;
    }

    public void setSqlSubProtocal(String sqlSubProtocal) {
        this.sqlSubProtocal = sqlSubProtocal;
    }

    public String getSqlServerAddress() {
        return sqlServerAddress;
    }

    public void setSqlServerAddress(String sqlServerAddress) {
        this.sqlServerAddress = sqlServerAddress;
    }

    public String getSqlServerPort() {
        return sqlServerPort;
    }

    public void setSqlServerPort(String sqlServerPort) {
        this.sqlServerPort = sqlServerPort;
    }

    public String getSpecificDbName() {
        return specificDbName;
    }

    public void setSpecificDbName(String specificDbName) {
        this.specificDbName = specificDbName;
    }

}
