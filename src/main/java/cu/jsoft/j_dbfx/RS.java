/*
 * Copyright Joe1962
 * https://github.com/Joe1962
 */
package cu.jsoft.j_dbfx;

import static cu.jsoft.j_utilsfxlite.global.CONSTS.SPACE;
import cu.jsoft.j_utilsfxlite.subs.SUB_UtilsNotifications;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

/**
 *
 * @author joe1962
 */
public abstract class RS {
//<editor-fold defaultstate="collapsed" desc=" My class-level variables declaration ">
	protected DBConnectionHandler DBConnHandler;
	protected Connection MyConn;
	protected int my_ID;
	protected ResultSet RST;
	protected String SQLSelectAll;
	protected String SQLDeleteAll;
	protected String SQLSelectByDate;
	protected String SQLSelectByPK;
	protected String SQLSelectByMaster;
	protected String SQLSelectByName;
	protected String SQLSelectPrevByPK;
	protected String SQLSelectNextByPK;
	protected String SQLSelectFirstByPK;
	protected String SQLSelectLastByPK;
	protected String SQLSelectSequential;
	protected String SQLTable;
	protected String SQLAppendRow;
	protected String SQLUpdateRow;
	protected SimpleDateFormat MyDateFormatter = new SimpleDateFormat("yyyy-MM-dd");
	private Boolean isInserting = false;
//</editor-fold>

	/**
	 * @return the DBConnHandler
	 */
	protected DBConnectionHandler getDBConnHandler() {
		return DBConnHandler;
	}

	/**
	 * @param DBConnHandler the DBConnHandler to set
	 */
	public void setDBConnHandler(DBConnectionHandler DBConnHandler) {
		this.DBConnHandler = DBConnHandler;
		setMyConn(getDBConnHandler().getMyConn());
	}

	/**
	 * @return the MyConn
	 */
	protected Connection getMyConn() {
		return MyConn;
	}

	/**
	 * @param MyConn the MyConn to set
	 */
	public void setMyConn(Connection MyConn) {
		this.MyConn = MyConn;
	}

	public int Count(String SQL, String dbTable) throws SQLException {
		SQL = SQL.replaceFirst("DBTABLE", dbTable);
		PreparedStatement pstmt = getMyConn().prepareStatement(SQL);
		//pstmt.setString(1, dbTable);
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()){
			return rs.getInt(1);
		}
		return -1;
	}

	public void selectFreeStyle(String SQL) throws SQLException {
		PreparedStatement pstmt = getMyConn().prepareStatement(SQL, 
		ResultSet.TYPE_SCROLL_INSENSITIVE, 
		ResultSet.CONCUR_UPDATABLE);
		RST = pstmt.executeQuery();
		RST.first();
	}

	public void selectAll(String OrderByString) throws SQLException {
		String QuerySQL = SQLSelectAll + SPACE + OrderByString;
		PreparedStatement pstmt;
		pstmt = getMyConn().prepareStatement(QuerySQL, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		RST = getDBConnHandler().doQuery(pstmt);
		RST.first();
//		echoClassMethodComment(pstmt.toString(), isDebug, false);			// DEBUG...
	}

	public abstract void selectByDate(String OrderByString, Object MyMaster) throws SQLException;

	public int deleteAll() throws SQLException {
		String QuerySQL = SQLDeleteAll;
		PreparedStatement pstmt;
		pstmt = getMyConn().prepareStatement(QuerySQL, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		return pstmt.executeUpdate();
	}

	public abstract void selectByPK(Object MyPK) throws SQLException;

	public abstract void selectByMaster(String OrderByString, Object MyMaster) throws SQLException;

	public void selectyStringField(String MyName, String MyNameField, boolean FuzzySearch, boolean CaseSensitive, String OrderByString) throws SQLException {
		if (MyName == null || MyNameField == null) {
			return;
		}

		String WhereStatement;
		if (CaseSensitive) {
			WhereStatement = "WHERE " + MyNameField + " LIKE ? ";
		} else {
			WhereStatement = "WHERE UPPER(" + MyNameField + ") LIKE ? ";
			MyName = MyName.toUpperCase();
		}
		String QuerySQL = SQLSelectAll + SPACE + WhereStatement + SPACE + OrderByString;
		PreparedStatement pstmt;
		pstmt = getMyConn().prepareStatement(QuerySQL, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		int n = 1;
		if (FuzzySearch) {
			pstmt.setString(n++, "%" + MyName + "%");
		} else {
			pstmt.setString(n++, MyName);
		}
		RST = getDBConnHandler().doQuery(pstmt);
		RST.first();
		//echoln("RS.selectyStringField: " + pstmt.toString(), isDebug, false);
	}

	public int deleteByStringField(String MyName, String MyNameField, boolean CaseSensitive, String MyTable) throws SQLException {
		if (MyName == null || MyNameField == null) {
			return -1;
		}

		String WhereStatement;
		if (CaseSensitive) {
			WhereStatement = "WHERE " + MyNameField + " = ? ";
		} else {
			WhereStatement = "WHERE UPPER(" + MyNameField + ") = ? ";
			MyName = MyName.toUpperCase();
		}

		String QuerySQL = "DELETE FROM" + SPACE + MyTable + SPACE + WhereStatement;

		PreparedStatement pstmt;
		pstmt = getMyConn().prepareStatement(QuerySQL, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		pstmt.setString(1, MyName);
		return getDBConnHandler().doUpdate(pstmt);
	}

	public abstract void selectByNameByActiveState(String MyName, String OrderByString, Object ActiveState) throws SQLException;

	public abstract Object getCurrent() throws SQLException;

	public boolean goFirst() throws SQLException {
		if (RST == null) {
			return false;
		}

		return RST.first();
	}

	public boolean goLast() throws SQLException {
		if (RST == null) {
			return false;
		}

		return RST.last();
	}

	public boolean goPrev() throws SQLException {
		if (RST == null) {
			return false;
		}

		return RST.previous();
	}

	public boolean goNext() throws SQLException {
		if (RST == null) {
			return false;
		}
		return RST.next();
	}

	/**
	 * Get last int value + 1 for selected DB table column.
	 * Set up the proper SQL query in the subclasses.
	 *
	 * @param ParamsMap Map key is from 1 to number of parameters in SQL query,
	 * map values are the values of the corresponding parameters.
	 * @return 
	 * @throws java.sql.SQLException
	 */
	public int getSequential(HashMap<Integer, Object> ParamsMap) throws SQLException {
		PreparedStatement pstmt;
		pstmt = getMyConn().prepareStatement(SQLSelectSequential, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

		if (ParamsMap != null) {
			for (int MapKey = 1; MapKey < ParamsMap.size() + 1.; MapKey++) {
				Object MapValue = ParamsMap.get(MapKey);
				pstmt.setObject(MapKey, MapValue);
			}
		}

		setRST(getDBConnHandler().doQuery(pstmt));
		getRST().first();
		Object tmpObj = getRST().getObject("SeqNext");
		int tmpInt;
		if (tmpObj == null) {
			tmpInt = 0;
		} else {
			tmpInt = (int) getRST().getObject("SeqNext");
		}
		tmpInt++;
		return tmpInt;
	}

	public abstract boolean appendRow(Object MyRow) throws SQLException;

	public abstract boolean updateRow(Object MyRow, Object WhereParam) throws SQLException;

	private PreparedStatement getUpdateFieldStatement(String myField) throws SQLException {
		String QuerySQL = "UPDATE " + SQLTable + " SET " + myField + " = ? WHERE cod = ?";
		PreparedStatement pstmt;
		pstmt = getMyConn().prepareStatement(QuerySQL, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		return pstmt;
	}

	public int updateField(String myField, Date myValue) throws SQLException {
		PreparedStatement pstmt = getUpdateFieldStatement(myField);
		pstmt.setDate(1, myValue);
		pstmt.setInt(2, my_ID);
		if (getRST() != null) {
			String QuerySQL = getRST().getStatement().toString();
		}
		return pstmt.executeUpdate();
	}

	public int updateField(String myField, int myValue) throws SQLException {
		PreparedStatement pstmt = getUpdateFieldStatement(myField);
		pstmt.setInt(1, myValue);
		pstmt.setInt(2, my_ID);
		if (getRST() != null) {
			String QuerySQL = getRST().getStatement().toString();
		}
		return pstmt.executeUpdate();
	}

	public int updateField(String myField, BigDecimal myValue) throws SQLException {
		PreparedStatement pstmt = getUpdateFieldStatement(myField);
		pstmt.setBigDecimal(1, myValue);
		pstmt.setInt(2, my_ID);
		if (getRST() != null) {
			String QuerySQL = getRST().getStatement().toString();
		}
		return pstmt.executeUpdate();
	}

	public int getIntValue(String fieldname) throws SQLException {
		return RST.getInt(fieldname);
	}

	public Date getDateValue(String fieldname) throws SQLException {
		return RST.getDate(fieldname);
	}

	public BigDecimal getCurrencyValue(String fieldname) throws SQLException {
		return RST.getBigDecimal(fieldname);
	}

	public abstract int deleteRow() throws SQLException;

	public abstract boolean deleteRowByID(int MyID) throws SQLException;

	public abstract boolean deleteRowByID(String MyID) throws SQLException;

	public abstract int deleteRowByID(BigInteger MyID) throws SQLException;

	public abstract int deleteRowsByZeroQuant() throws SQLException;

	public abstract int deleteByMaster() throws SQLException;

	public int rsCount() {
		// Remember to use a select method first to set up the recordset...
		int NumRows;

		try {
			boolean retBool = RST.last();
			if (retBool) {
				NumRows = RST.getRow();
				RST.first();
				return NumRows;
			} else {
				return 0;
			}
		} catch (SQLException | NullPointerException ex) {
			SUB_UtilsNotifications.echoln(ex.getMessage(), false, true);
			return 0;
		}
	}

	public void setLogState(boolean isLoggedTable, String dbTable) throws SQLException {
		String SQL = "";

		if (isLoggedTable) {
			SQL = "ALTER TABLE " + dbTable + " SET LOGGED;";
		} else {
			SQL = "ALTER TABLE " + dbTable + " SET UNLOGGED;";
		}

		PreparedStatement pstmt = getMyConn().prepareStatement(SQL);
		pstmt.execute();
	}

	public ResultSet getRST() {
		return RST;
	}

	public void setRST(ResultSet RST) {
		this.RST = RST;
	}

	public Boolean getInserting() {
		return isInserting;
	}

	public void setInserting(Boolean isInserting) {
		this.isInserting = isInserting;
	}

	public final class TYP_RetInsertOrUpdate {

		private int RowsAffected;
		private boolean InsertSuccessful;
		private boolean UpdateSuccessful;

		/**
		 * @return the RowsAffected
		 */
		public int getRowsAffected() {
			return RowsAffected;
		}

		/**
		 * @param aRowsAffected the RowsAffected to set
		 */
		public void setRowsAffected(int aRowsAffected) {
			RowsAffected = aRowsAffected;
		}

		/**
		 * @return the InsertSuccessful
		 */
		public boolean isInsertSuccessful() {
			return InsertSuccessful;
		}

		/**
		 * @param aInsertSuccessful the InsertSuccessful to set
		 */
		public void setInsertSuccessful(boolean aInsertSuccessful) {
			InsertSuccessful = aInsertSuccessful;
		}

		/**
		 * @return the UpdateSuccessful
		 */
		public boolean isUpdateSuccessful() {
			return UpdateSuccessful;
		}

		/**
		 * @param aUpdateSuccessful the UpdateSuccessful to set
		 */
		public void setUpdateSuccessful(boolean aUpdateSuccessful) {
			UpdateSuccessful = aUpdateSuccessful;
		}
	}

	public void setMy_ID(int id) {
		this.my_ID = id;
	}

}
