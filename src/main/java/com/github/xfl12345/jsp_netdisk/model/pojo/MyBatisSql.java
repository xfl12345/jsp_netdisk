package com.github.xfl12345.jsp_netdisk.model.pojo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * source code URL=https://blog.csdn.net/BESSINO/article/details/53519468
 */
public class MyBatisSql {

    /**
     * 运行期 sql
     */
    private String sql;

    /**
     * 参数 数组
     */
    private Object[] parameters;

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getSql() {
        return sql;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    public Object[] getParameters() {
        return parameters;
    }

    /**
     * 获取可以直接在数据库中执行的sql语句
     * @return 最终成品级sql语句
     */
    @Override
    public String toString() {
        if (parameters == null || sql == null) {
            return "";
        }
        List<Object> parametersArray = Arrays.asList(parameters);
        List<Object> list = new ArrayList<Object>(parametersArray);
        while (sql.indexOf("?") != -1 && list.size() > 0 && parameters.length > 0) {
            sql = sql.replaceFirst("\\?", list.get(0).toString());
            list.remove(0);
        }
        return sql.replaceAll("(\r?\n(\\s*\r?\n)+)", "\r\n");
    }
}