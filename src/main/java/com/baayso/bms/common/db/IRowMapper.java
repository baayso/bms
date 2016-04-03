package com.baayso.bms.common.db;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 结果集映射接口
 * 
 * @author ChenFangjie
 * 
 */
public interface IRowMapper<T> {

    public T mapRow(ResultSet rs) throws SQLException;

}
