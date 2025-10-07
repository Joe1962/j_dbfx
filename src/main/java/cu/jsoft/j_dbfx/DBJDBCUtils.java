/*
 * Copyright Joe1962
 * https://github.com/Joe1962
 */
package cu.jsoft.j_dbfx;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author joe1962
 */
public class DBJDBCUtils {
	
	public Boolean[] getJDBCBooleanArraySafe(ResultSet RST, String columnName) throws SQLException {
		Object arrayObj = RST.getObject(columnName);
		return (arrayObj instanceof java.sql.Array) ? (Boolean[]) ((java.sql.Array) arrayObj).getArray() : null;
	}

}
