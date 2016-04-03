package com.baayso.bms.common.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 用于辅助拼装生成SQL语句的工具类
 * <p />
 *
 * 用法：
 * 
 * <pre>
 * SQLHelper sqlHelper = new SQLHelper(&quot;t_user&quot;, &quot;u&quot;) //
 *         .addWhere(null != loginName &amp;&amp; 0 &lt; loginName.length(), &quot;u.loginName LIKE ?&quot;, &quot;%&quot; + loginName + &quot;%&quot;) //
 *         .addOrderBy(&quot;u.createTime&quot;, &quot;ASC&quot;);
 * 
 * sqlHelper.getOraclePaginationQueryListSql(pageNum, pageSize);
 * sqlHelper.getQueryCountSql();
 *
 * </pre>
 *
 * @since 1.0.0 (2014/5/10)
 * @author ChenFangJie
 * 
 */
public final class SQLHelper {

    private final String tableName; // 表名
    private final String alias; // 表的别名

    private String selectClause = ""; // SELECT子句，可选
    private final String fromClause; // FROM子句，必选
    private String whereClause = ""; // WHERE子句，可选
    private String orderByClause = ""; // ORDER BY子句，可选

    private final List<Object> parameters = new ArrayList<>(); // WHERE子句中的参数

    private boolean isThereAlias = false; // 表是否有别名

    /**
     * 生成 FROM 子句（不指定别名）。
     *
     * @param tableName
     *            表名
     */
    public SQLHelper(String tableName) {
        // this.tableName = tableName;
        // this.alias = null;
        // this.fromClause = String.format(" FROM %s ", tableName);

        this(tableName, "");
    }

    /**
     * 生成 FROM 子句，并指定别名。
     *
     * @param tableName
     *            表名
     * @param alias
     *            别名
     */
    public SQLHelper(String tableName, String alias) {
        this.tableName = tableName;
        this.alias = alias;
        this.fromClause = String.format(" FROM %s %s ", tableName, alias);

        this.isThereAlias = (null != alias) && (0 < alias.length());
    }

    /**
     * 拼接 SELECT 子句。
     * 
     * @param field
     *            字段名
     * @return
     */
    public SQLHelper addSelectField(String field) {
        if (0 == this.whereClause.length()) {
            this.selectClause = "SELECT " + field;
        }
        else {
            this.selectClause += ", " + field;
        }

        return this;
    }

    /**
     * 根据传递的第一个参数判断是否拼接 SELECT 子句。
     * 
     * @param append
     *            为true则拼接，否则不拼接。
     * @param field
     *            字段名
     * @return
     */
    public SQLHelper addSelectField(boolean append, String field) {
        if (append) {
            this.addSelectField(field);
        }

        return this;
    }

    /**
     * 拼接 WHERE 子句。
     *
     * @param condition
     *            查询条件。
     * @param params
     *            查询条件对应的参数。
     * @return
     */
    public SQLHelper addWhere(String condition, Object... params) {

        if (0 == this.whereClause.length()) {
            this.whereClause = " WHERE " + condition;
        }
        else {
            this.whereClause += " AND " + condition;
        }

        // 保存参数
        if (null != params && 0 < params.length) {
            // for (int i = 0; i < params.length; i++) {
            // this.parameters.add(params[i]);
            // }

            this.parameters.addAll(Arrays.asList(params));
        }

        return this;
    }

    /**
     * 根据传递的第一个参数判断是否拼接 WHERE 子句。
     *
     * @param append
     *            为true则拼接，否则不拼接。
     * @param condition
     *            查询条件。
     * @param params
     *            查询条件对应的参数。
     * @return
     */
    public SQLHelper addWhere(boolean append, String condition, Object... params) {

        if (append) {
            this.addWhere(condition, params);
        }

        return this;
    }

    /**
     * 拼接 ORDER BY 子句。
     *
     * @param propertyName
     *            需要排序的属性名。
     * @param sortRule
     *            排序规则（ASC和DESC），传null和零长度字符串则以ASC排序。
     * @return
     */
    public SQLHelper addOrderBy(String propertyName, String sortRule) {

        if (null == sortRule || 0 == sortRule.length()) {
            sortRule = "ASC";
        }

        if (0 == this.orderByClause.length()) {
            this.orderByClause = String.format(" ORDER BY %s %s", propertyName, sortRule);
        }
        else {
            this.orderByClause += String.format(", %s %s", propertyName, sortRule);
        }

        return this;
    }

    /**
     * 根据传递的第一个参数判断是否拼接 ORDER BY 子句。
     *
     * @param append
     *            为true则拼接，否则不拼接。
     * @param propertyName
     *            需要排序的属性名。
     * @param sortRule
     *            排序规则（ASC和DESC），传null和零长度字符串则以ASC排序。
     * @return
     */
    public SQLHelper addOrderBy(boolean append, String propertyName, String sortRule) {

        if (append) {
            this.addOrderBy(propertyName, sortRule);
        }

        return this;
    }

    /**
     * 获取生成的查询总记录数的 SQL语句（没有ORDER BY 子句）。
     *
     * @return 查询总记录数的SQL语句（没有ORDER BY 子句）。
     */
    public String getQueryCountSql() {

        return String.format("SELECT COUNT(*) %s%s", this.fromClause, this.whereClause);
    }

    /**
     * 获取生成的查询数据的 SQL语句。
     *
     * @return 查询数据的SQL语句
     */
    public String getQuerySql() {

        return String.format("%s%s%s%s", //
                (0 == this.selectClause.length() ? (this.isThereAlias ? "SELECT " + this.alias + ".*" : "SELECT *") : this.selectClause), //
                this.fromClause, //
                this.whereClause, //
                this.orderByClause);
    }

    /**
     * 获取MySQL数据库分页查询SQL语句。
     *
     * @param pageNum
     *            当前页码
     * @param pageSize
     *            每页显示记录数
     * @return MySQL数据库分页查询SQL语句
     */
    public String getMySQLPaginationQueryListSql(int pageNum, int pageSize) {

        // LIMIT函数，LIMIT [offset,] rows 可以从Mysql数据库表中 第M条记录开始 检索N条记录

        // mysql> select * from news where id>=(select id from news limit 490000,1) limit 10;

        // SELECT u.* FROM t_user AS u WHERE u.loginName LIKE ? ORDER BY u.createTime ASC LIMIT 0,5

        String mysqlPaginationSql = String.format( //
                "%s LIMIT %d,%d", //
                this.getQuerySql(), //
                Integer.valueOf(pageSize * (pageNum - 1)), //
                Integer.valueOf(pageSize));

        return mysqlPaginationSql;
    }

    /**
     * 获取Oracle数据库分页查询SQL语句。
     *
     * @param pageNum
     *            当前页码
     * @param pageSize
     *            每页显示记录数
     * @return Oracle数据库分页查询SQL语句
     */
    public String getOraclePaginationQueryListSql(int pageNum, int pageSize) {

        // SELECT u.* FROM (SELECT rownum rn, u.* FROM t_user u WHERE rownum <= 5 AND u.loginName LIKE ?) u WHERE rn > 0 ORDER BY u.createTime ASC

        // Oracle分页SQL的里层SELECT语句已包含WHERE关键字（WHERE rownum <= %d），所以将where子句中的"WHERE"替换成"AND"
        String whereClause = this.whereClause.replace("WHERE", "AND");

        String oraclePaginationSql = String.format( //
                "%s FROM (SELECT rownum rn, %s* FROM %s %s WHERE rownum <= %d %s) %s WHERE rn > %d %s", //
                (0 == this.selectClause.length() ? (this.isThereAlias ? "SELECT " + this.alias + ".*" : "SELECT *") : this.selectClause), //
                (this.isThereAlias ? this.alias + "." : this.tableName + "."), //
                this.tableName, //
                (this.isThereAlias ? this.alias : ""), //
                Integer.valueOf(pageNum * pageSize), //
                whereClause, //
                (this.isThereAlias ? this.alias : ""), //
                Integer.valueOf(pageSize * (pageNum - 1)), //
                this.orderByClause);

        return oraclePaginationSql;
    }

    /**
     * 获取SQLServer数据库分页查询SQL语句，尚未实现。
     *
     * @param pageNum
     *            当前页码
     * @param pageSize
     *            每页显示记录数
     * @return SQLServer数据库分页查询SQL语句
     */
    public String getSQLServerPaginationQueryListSql(int pageNum, int pageSize) {
        throw new UnsupportedOperationException("not yet implemented.");
    }

    /**
     * 获取SQLite数据库分页查询SQL语句，尚未实现。
     *
     * @param pageNum
     *            当前页码
     * @param pageSize
     *            每页显示记录数
     * @return SQLite数据库分页查询SQL语句
     */
    public String getSQLitePaginationQueryListSql(int pageNum, int pageSize) {
        throw new UnsupportedOperationException("not yet implemented.");
    }

    /**
     * 获取PostgreSQL数据库分页查询SQL语句，尚未实现。
     *
     * @param pageNum
     *            当前页码
     * @param pageSize
     *            每页显示记录数
     * @return PostgreSQL数据库分页查询SQL语句
     */
    public String getPostgreSQLPaginationQueryListSql(int pageNum, int pageSize) {
        throw new UnsupportedOperationException("not yet implemented.");
    }

    /**
     * 获取Sysbase数据库分页查询SQL语句，尚未实现。
     *
     * @param pageNum
     *            当前页码
     * @param pageSize
     *            每页显示记录数
     * @return PostgreSQL数据库分页查询SQL语句
     */
    public String getSysbasePaginationQueryListSql(int pageNum, int pageSize) {
        throw new UnsupportedOperationException("not yet implemented.");
    }

    /**
     * 获取参数列表，与SQL语句过滤条件中的'?'对应。
     *
     * @return 参数列表
     */
    public List<Object> getParameters() {
        return this.parameters;
    }

}
