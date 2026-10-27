/*
 * Copyright Joe1962
 * https://github.com/Joe1962
 */
package cu.jsoft.j_dbfx;

import cu.jsoft.j_dbfx.global.FLAGS;
import cu.jsoft.j_dbfx.types.TYP_FieldValuePair;
import static cu.jsoft.j_utilsfx.utils.SUB_UtilsNotifications.echoClassMethodComment;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.UUID;

/**
 * Result set abstracto: envuelve un {@link ResultSet} JDBC y expone
 * conversiones seguras (nulos, dinero, fechas ISO) para construir las formas
 * canónicas del contrato. Cada recurso extiende esta clase e implementa
 * {@link #getCurrent()}.
 *
 * @author joe1962
 * @param <T>
 */
public abstract class RS_new<T> {
	protected Connection MyConn;
	protected String dbSchema;
	protected String dbTableFull;
	protected String dbTableName;
	protected String dbCountField;
	protected ResultSet RST;
	protected static final DateTimeFormatter ISO_UTC = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").withZone(ZoneOffset.UTC);
	protected String sqlAllFieldsSelect;
	protected String sqlAppendTail;



	public abstract T getCurrent() throws SQLException;
	public abstract void appendRowHelper(T MyRow, PreparedStatement pstmt) throws SQLException;
	public abstract int updateRow(T MyRow, TYP_FieldValuePair WhereParam) throws SQLException;



	protected String prepSQL(String SQL, String OrderByString) {
		if (!OrderByString.isBlank()) {
			return SQL + " " + OrderByString + ";";
		} else {
			return SQL;
		}
	}

	protected void runSQLQuery(PreparedStatement pstmt) throws SQLException {
		RST = pstmt.executeQuery();
		RST.first();
		if (FLAGS.isDEBUG()) {
			String QuerySQL = RST.getStatement().toString();
			echoClassMethodComment(QuerySQL, FLAGS.isDEBUG(), false);			// DEBUG...
		}
	}

	public void selectAll() throws SQLException {
		StringBuilder SQL = new StringBuilder(getSqlAllFieldsSelect());
		SQL.append(getDbTableFull());
		SQL.append(";");

		selectSQL(SQL.toString());
	}

	public void selectKeysetPaginated(String start, int limit, String orderField, boolean orderASC) throws SQLException {
		StringBuilder SQL = new StringBuilder(getSqlAllFieldsSelect());
		SQL.append(getDbTableFull());
		if (!start.isBlank()) {
			SQL.append(" WHERE ");
			if (orderField.equals("uuid")) {
				SQL.append("uuid > ");
			} else if (orderField.equals("nick")) {
				SQL.append("nick = ");
			}
			SQL.append("'");
			SQL.append(start);
			SQL.append("'");
		}
		SQL.append(" ORDER BY ");
		SQL.append(orderField);
		if (orderASC) {
			SQL.append(" ASC ");
		} else {
			SQL.append(" DESC ");
		}
		SQL.append("LIMIT ");
		SQL.append(String.valueOf(limit));
		SQL.append(";");

		selectSQL(SQL.toString());
	}

	public boolean SelectByUUID(UUID uuid) throws SQLException {
		StringBuilder SQL = new StringBuilder(getSqlAllFieldsSelect());
		SQL.append(getDbTableFull());
		SQL.append(" WHERE uuid = ?;");

		return selectSQL(SQL.toString(), uuid);
	}

	public boolean SelectByNick(String nick) throws SQLException {
		StringBuilder SQL = new StringBuilder(getSqlAllFieldsSelect());
		SQL.append(getDbTableFull());
		SQL.append(" WHERE nick = ?;");

		return selectSQL(SQL.toString(), nick);
	}

	public boolean SelectByOtherField(String OtherFieldName, Object otherFieldValue) throws SQLException {
		StringBuilder SQL = new StringBuilder(getSqlAllFieldsSelect());
		SQL.append(getDbTableFull());
		SQL.append(" WHERE ");
		SQL.append(OtherFieldName);
		SQL.append(" = ? ;");

		return selectSQL(SQL.toString(), otherFieldValue);
	}

	public int appendRow(T MyRow) throws SQLException {
		PreparedStatement pstmt = appendRowCommon(MyRow, false);
		return pstmt.executeUpdate();
	}

	public T appendRowReturning(T MyRow) throws SQLException {
		PreparedStatement pstmt = appendRowCommon(MyRow, true);
		setRST(pstmt.executeQuery());

		if (getRST() == null) {
			return null;
		}

		// Append with RETURNING:
		if (getRST().first()) {
			return getCurrent();
		} else {
			return null;
		}
	}

	public PreparedStatement appendRowCommon(T MyRow, boolean Returning) throws SQLException {
		StringBuilder SQL = new StringBuilder("INSERT INTO ");
		SQL.append(getDbTableFull());
		SQL.append(getSqlAppendTail());

		if (Returning) {
			// Append with RETURNING:
			SQL.append("RETURNING *;");
		} else {
			// Append (simple):
			SQL.append(";");
		}

		PreparedStatement pstmt = MyConn.prepareStatement(SQL.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		appendRowHelper(MyRow, pstmt);

		return pstmt;
	}

	/**
	 * NOTES: In the calling method, insert consecutive Integer keys from 1 to last in the SortedMap.
	 * NOTES: The fieldname/fieldvalue pairs will be added to the SQL 'WHERE' in the key order.
	 *
	 * @param WhereParam
	 * @return count of updated rows.
	 * @throws java.sql.SQLException
	 */
	public int delRows(SortedMap<Integer, TYP_FieldValuePair> WhereParam) throws SQLException {
		// NOTES: In the calling method, insert consecutive Integer keys from 1 to last in the SortedMap.
		// NOTES: The fieldname/fieldvalue pairs will be added to the SQL 'WHERE' in the key order.

		StringBuilder SQL = new StringBuilder("DELETE FROM  ");
		SQL.append(getDbTableFull());
		SQL.append(" WHERE ");

		for (Map.Entry<Integer, TYP_FieldValuePair> entry : WhereParam.entrySet()) {
			Integer key = entry.getKey();
			TYP_FieldValuePair value = entry.getValue();
			
			SQL.append(value.getFieldName());
			SQL.append(" = ");
			Object temp = value.getFieldValue();
			if (temp instanceof UUID || temp instanceof String || temp instanceof java.sql.Date) {
				SQL.append("'");
				SQL.append(value.getFieldValue());
				SQL.append("'");
			} else {
				SQL.append(value.getFieldValue());
			}

			if (key < WhereParam.lastKey()) {
				SQL.append(" AND ");
			}
		}

		SQL.append(";");

		int count = modSQL(SQL.toString());

		if (FLAGS.isDEBUG()) {System.out.println("Deleted " + count + " records...");}

		return count;
	}

	public int Count() throws SQLException {
		String SQL = "SELECT COUNT(" + dbCountField + ") FROM " + dbTableFull + ";";
		PreparedStatement pstmt = MyConn.prepareStatement(SQL);
		ResultSet rs = pstmt.executeQuery();

		if (FLAGS.isDEBUG()) {System.out.println(pstmt.toString());}

		while (rs.next()){
			return rs.getInt(1);
		}
		return -1;
	}

	public Boolean isBoolFieldByOtherField(String boolFieldName, String OtherFieldName, Object otherFieldValue) throws SQLException {
		StringBuilder SQL = new StringBuilder("SELECT ");
		SQL.append(boolFieldName);
		SQL.append(" FROM ");
		SQL.append(getDbTableFull());
		SQL.append(" WHERE ");
		SQL.append(OtherFieldName);
		SQL.append(" = ? ;");

		if (selectSQL(SQL.toString(), otherFieldValue)) {
			return getRST().getBoolean(boolFieldName);
		} else {
			return null;
		}
	}

	public boolean selectSQL(String SQL) throws SQLException {
		PreparedStatement pstmt = MyConn.prepareStatement(SQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		RST = pstmt.executeQuery();

		if (FLAGS.isDEBUG()) {System.out.println(pstmt.toString());}

		return RST.first();
	}

	public boolean selectSQL(String SQL, Object WhereParam) throws SQLException {
		PreparedStatement pstmt = MyConn.prepareStatement(SQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

		pstmt.setObject(1, WhereParam);

		RST = pstmt.executeQuery();

		if (FLAGS.isDEBUG()) {System.out.println(pstmt.toString());}

		return RST.first();
	}

	public int modSQL(String SQL) throws SQLException {
		PreparedStatement pstmt = MyConn.prepareStatement(SQL);

		int retInt = pstmt.executeUpdate();

		if (FLAGS.isDEBUG()) {System.out.println(pstmt.toString());}

		return retInt;
	}

	public int modSQL(String SQL, HashMap<Integer, Object> fv) throws SQLException {
		PreparedStatement pstmt = MyConn.prepareStatement(SQL);

		for (Map.Entry<Integer, Object> entry : fv.entrySet()) {
			pstmt.setObject(entry.getKey(), entry.getValue());
		}

		int retInt = pstmt.executeUpdate();

		if (FLAGS.isDEBUG()) {System.out.println(pstmt.toString());}

		return retInt;
	}

	/**
	 * NOTE: Requires safetyNet parameter == "Danger Will Robinson!".
	 * 
	 * @param safetyNet
	 * @throws SQLException
	 */
	public void doTruncateTable(String safetyNet) throws SQLException {
		// Truncate DB table, probably postgreSQL-specific...
		if (!safetyNet.equals("Danger Will Robinson!")) {return;}

		StringBuilder SQL = new StringBuilder("TRUNCATE ");
		SQL.append(dbTableFull);
		SQL.append(" RESTART IDENTITY CASCADE");
		PreparedStatement pstmt = MyConn.prepareStatement(SQL.toString());
		pstmt.executeUpdate();
	}



	/**
	 * Número de fila actual (1-based) o 0 si no hay cursor posicionado.
	 *
	 * @return fila actual.
	 * @throws SQLException si falla la consulta.
	 */
	public int getRow() throws SQLException {
		return RST.getRow();
	}

	/**
	 * Mueve el cursor a la primera fila.
	 *
	 * @return true si hay fila.
	 * @throws SQLException si falla la navegación.
	 */
	public boolean goFirst() throws SQLException {
		if (RST == null) {
			return false;
		}

		return RST.first();
	}

	/**
	 * Mueve el cursor a la última fila.
	 *
	 * @return true si hay fila.
	 * @throws SQLException si falla la navegación.
	 */
	public boolean goLast() throws SQLException {
		if (RST == null) {
			return false;
		}

		return RST.last();
	}

	/**
	 * Avanza a la fila anterior.
	 *
	 * @return true si hay fila disponible.
	 * @throws SQLException si falla la navegación.
	 */
	public boolean goPrev() throws SQLException {
		if (RST == null) {
			return false;
		}

		return RST.previous();
	}

	/**
	 * Avanza a la siguiente fila.
	 *
	 * @return true si hay fila disponible.
	 * @throws SQLException si falla la navegación.
	 */
	public boolean goNext() throws SQLException {
		if (RST == null) {
			return false;
		}
		return RST.next();
	}

	public Boolean[] getJDBCBooleanArraySafe(ResultSet RST, String columnName) throws SQLException {
		if (RST == null || columnName == null) {
			return null;
		}

		Object arrayObj = RST.getObject(columnName);
		return (arrayObj instanceof java.sql.Array) ? (Boolean[]) ((java.sql.Array) arrayObj).getArray() : null;
	}



	/**
	 * Ejecuta un script SQL completo (múltiples sentencias separadas por ';').
	 * El driver PostgreSQL envía el script por el protocolo de consulta simple.
	 *
	 * @param conn conexión JDBC.
	 * @param sql  script a ejecutar.
	 * @throws SQLException si falla la ejecución.
	 */
	private static void exec(Connection conn, String sql) throws SQLException {
		try (Statement st = conn.createStatement()) {
			st.execute(sql);
		}
		if (FLAGS.isDEBUG()) {System.out.println("RS: Script ejecutado (" + sql.length() + " bytes).");}
	}

	/**
	 * Construye el mapa canónico de la fila actual.
	 *
	 * @return mapa clave-valor serializable a msgpack.
	 * @throws SQLException si falla la lectura.
	 */
	//public abstract Map<String, Object> getCurrent() throws SQLException;

	/** Lee un string o null. */
	protected String str(String column) throws SQLException {
		String value = RST.getString(column);
		return RST.wasNull() ? null : value;
	}

	/** Lee un Long o null. */
	protected Long lng(String column) throws SQLException {
		long value = RST.getLong(column);
		return RST.wasNull() ? null : value;
	}

	/** Lee un Integer o null. */
	protected Integer integer(String column) throws SQLException {
		int value = RST.getInt(column);
		return RST.wasNull() ? null : value;
	}

	/** Lee un Double o null. */
	protected Double dbl(String column) throws SQLException {
		double value = RST.getDouble(column);
		return RST.wasNull() ? null : value;
	}

	/** Lee un BigDecimal o null. */
	protected BigDecimal big(String column) throws SQLException {
		BigDecimal value = RST.getBigDecimal(column);
		return RST.wasNull() ? null : value;
	}

	/** Lee un BigDecimal y lo formatea como dinero/cantidad con 2 decimales. */
	protected String money(String column) throws SQLException {
		return toMoney(big(column));
	}

	/** Lee un Timestamp y lo devuelve en ISO-8601 con zona UTC. */
	protected String ts(String column) throws SQLException {
		Timestamp value = RST.getTimestamp(column);
		if (value == null) {
			return null;
		}
		return toIso(value);
	}

	/** Lee un Boolean o null. */
	protected Boolean bool(String column) throws SQLException {
		boolean value = RST.getBoolean(column);
		return RST.wasNull() ? null : value;
	}

	/**
	 * Convierte una fecha a ISO-8601 con zona UTC ({@code 2026-08-03T12:00:00.000+00:00}).
	 *
	 * @param date fecha a convertir.
	 * @return cadena ISO-8601.
	 */
	public static String toIso(Date date) {
		if (date == null) {
			return null;
		}
		return ISO_UTC.format(date.toInstant());
	}

	/**
	 * Convierte un {@link OffsetDateTime} a la misma forma ISO-8601.
	 *
	 * @param odt fecha a convertir.
	 * @return cadena ISO-8601 o null.
	 */
	public static String toIso(OffsetDateTime odt) {
		if (odt == null) {
			return null;
		}
		return ISO_UTC.format(odt);
	}

	/**
	 * Formatea un {@link BigDecimal} como cadena con 2 decimales fijos
	 * (convención del contrato para importes, superficies y cantidades).
	 *
	 * @param value valor a formatear.
	 * @return cadena {@code "%.2f"} o null.
	 */
	public static String toMoney(BigDecimal value) {
		if (value == null) {
			return null;
		}
		return String.format(java.util.Locale.ROOT, "%.2f", value);
	}



	/** Mapa ordenado vacío para el recurso en construcción.
	 * @return  */
	protected LinkedHashMap<String, Object> row() {
		return new LinkedHashMap<>();
	}

	public Connection getMyConn() {
		return MyConn;
	}

	public void setMyConn(Connection MyConn) {
		this.MyConn = MyConn;
	}

	public String getDbSchema() {
		return dbSchema;
	}

	public void setDbSchema(String dbSchema) {
		this.dbSchema = dbSchema;
	}

	public String getDbTableFull() {
		return dbTableFull;
	}

	public void setDbTableFull(String dbTableFull) {
		this.dbTableFull = dbTableFull;
	}

	public String getDbTableName() {
		return dbTableName;
	}

	public void setDbTableName(String dbTableName) {
		this.dbTableName = dbTableName;
	}

	public String getDbCountField() {
		return dbCountField;
	}

	public void setDbCountField(String dbCountField) {
		this.dbCountField = dbCountField;
	}

	public ResultSet getRST() {
		return RST;
	}

	public void setRST(ResultSet RST) {
		this.RST = RST;
	}

	/**
	 * @return the sqlAllFieldsSelect
	 */
	public String getSqlAllFieldsSelect() {
		return sqlAllFieldsSelect;
	}

	/**
	 * @param sqlAllFieldsSelect the sqlAllFieldsSelect to set
	 */
	public void setSqlAllFieldsSelect(String sqlAllFieldsSelect) {
		this.sqlAllFieldsSelect = sqlAllFieldsSelect;
	}

	/**
	 * @return the sqlAppendTail
	 */
	public String getSqlAppendTail() {
		return sqlAppendTail;
	}

	/**
	 * @param sqlAppendTail the sqlAppendTail to set
	 */
	public void setSqlAppendTail(String sqlAppendTail) {
		this.sqlAppendTail = sqlAppendTail;
	}

}
