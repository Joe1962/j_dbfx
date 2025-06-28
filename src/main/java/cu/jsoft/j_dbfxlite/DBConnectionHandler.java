/*
 * Copyright Joe1962
 */
package cu.jsoft.j_dbfxlite;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import cu.jsoft.j_dbfxlite.types.TYP_DBStructCheck;
import static cu.jsoft.j_utilsfxlite.subs.SUB_UtilsNotifications.echoClassMethodComment;
import static cu.jsoft.j_utilsfxlite.subs.SUB_UtilsNotifications.echoln;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author joe1962
 */
public class DBConnectionHandler {

	private Connection MyConn;
	// final SimpleDateFormat MyDateFormatter = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 *
	 * @param MyDBConnType
	 * @param MyDBDriver
	 * @param MyDBPath
	 * @param MyDBServer
	 * @param MyDBPort
	 * @param MyDBName
	 * @param MyDBUser
	 * @param MyDBPass
	 * @throws SQLException
	 */
	public void dbConnectServer(String MyDBConnType, String MyDBDriver, String MyDBPath, String MyDBServer, int MyDBPort, String MyDBName, String MyDBUser, String MyDBPass) throws SQLException {
		if (null == MyDBDriver) {
			
		} else {
			
			// Connect to the database server:
			switch (MyDBDriver) {
				case "postgresql":
					String sMyConn = MyDBConnType + ":" + MyDBDriver + "://" + MyDBServer + ":" + String.valueOf(MyDBPort) + "/" + MyDBName;
					MyConn = DriverManager.getConnection(sMyConn, MyDBUser, MyDBPass);
					break;
				case "derby":
					break;
				default:
					break;
			}
		}
	}

	/**
	 *
	 * @param MyDBConnType
	 * @param MyDBDriver
	 * @param MyDBPath
	 * @param MyDBUser
	 * @param MyDBPass
	 * @throws SQLException
	 */
	public void dbConnectFileSystem(String MyDBConnType, String MyDBDriver, String MyDBPath, String MyDBUser, String MyDBPass) throws SQLException {
		// Connect to the database file:
		String sMyConn = MyDBConnType + ":" + MyDBDriver + ":directory:/" + MyDBPath + "/";
		//echoln(sMyConn, false, false);			// DEBUG...
		MyConn = DriverManager.getConnection(sMyConn, MyDBUser, MyDBPass);
	}

	/**
	 *
	 * @throws SQLException
	 */
	public void dbDisconnect() throws SQLException {
		// Disconnect from the database:
		// Close Connection:
		if (getMyConn() != null) {
			getMyConn().close();
		}
	}

	/**
	 *
	 * @param pstmt
	 * @return
	 * @throws SQLException
	 */
	public ResultSet doQuery(PreparedStatement pstmt) throws SQLException {
		// Execute SQL query by prepared statement...
		return pstmt.executeQuery();
	}

	/**
	 *
	 * @param pstmt
	 * @return
	 * @throws SQLException
	 */
	public int doUpdate(PreparedStatement pstmt) throws SQLException {
		// Execute SQL update query by prepared statement...
		return pstmt.executeUpdate();
	}

	/**
	 *
	 * @param DBName
	 * @throws SQLException
	 */
	public void doTruncate(String DBName) throws SQLException {
		// Truncate DB table, probably postgreSQL-specific...
		String QuerySQL = "TRUNCATE " + DBName + " RESTART IDENTITY CASCADE";
		PreparedStatement pstmt = getMyConn().prepareStatement(QuerySQL);
		doUpdate(pstmt);
	}

	/**
	 *
	 * @param Type
	 * @param Description
	 * @param MyOperator
	 * @param isDebug
	 * @throws SQLException
	 */
	public void WriteSysLog(int Type, String Description, String MyOperator, boolean isDebug) throws SQLException {
		// Requires existence of syslog table in current DB connection...
		String SQLSelectSequential = "SELECT max(consec) AS SeqNext FROM syslog ";
		PreparedStatement pstmt;
		pstmt = getMyConn().prepareStatement(SQLSelectSequential, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		ResultSet RST;
		RST = doQuery(pstmt);
		RST.first();
		Object tmpObj = RST.getObject("SeqNext");
		long tmpLong;
		if (tmpObj == null) {
			tmpLong = 0;
		} else {
			tmpLong = (long) RST.getObject("SeqNext");
		}

		String QuerySQL = "INSERT INTO syslog (consec, fecha_hora, tipo, descrip, operator) VALUES (?, ?, ?, ?, ?)";
		//PreparedStatement pstmt;
		pstmt = getMyConn().prepareStatement(QuerySQL, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		int n = 1;
		Date tmpDate = new java.util.Date();
		pstmt.setLong(n++, ++tmpLong);
		pstmt.setTimestamp(n++, new java.sql.Timestamp(tmpDate.getTime()));
		pstmt.setInt(n++, Type);
		pstmt.setString(n++, Description);
		pstmt.setString(n++, MyOperator);
		int retBool = doUpdate(pstmt);
		echoln(retBool, isDebug, false);
	}

	/**
	 *
	 * @param TimeoutSecs
	 * @return
	 * @throws SQLException
	 */
	public boolean IsValid(int TimeoutSecs) throws SQLException {
		return MyConn.isValid(5);
	}

	public void doCopyBackup(String DBTable, String FileDest) throws SQLException, IOException {
		String QuerySQL = "SELECT * FROM " + DBTable;
		PreparedStatement pstmt = getMyConn().prepareStatement(QuerySQL);
		ResultSet MyRS = doQuery(pstmt);

		try (FileWriter MyWriter = new FileWriter(FileDest, false); CSVWriter MyCSVWriter = new CSVWriter(MyWriter)) {
			MyCSVWriter.writeAll(MyRS, true);
		}
	}

	public long doCopyRestore(String DBTable, String FileSource) throws FileNotFoundException, IOException, SQLException, CsvException {
		List<String[]> MyList;
		try (FileReader MyReader = new FileReader(FileSource); CSVReader MyCSVReader = new CSVReader(MyReader)) {
			MyList = MyCSVReader.readAll();
		}

		// TODO: truncate table and import CSV list...
		return 0;			// Return number of records...
	}

	/**
	 *
	 * @return
	 */
	public Connection getMyConn() {
		return MyConn;
	}

	/**
	 *
	 * @param aMyConn
	 */
	public void setMyConn(Connection aMyConn) {
		MyConn = aMyConn;
	}

	public boolean getDB() throws SQLException {
		String QuerySQL = "SELECT datname FROM pg_database AS db;";
		PreparedStatement pstmt = MyConn.prepareStatement(QuerySQL);
		ResultSet rst = pstmt.executeQuery();
		ArrayList<String> lstBD = new ArrayList<>();

		int n = 1;
		while (rst.next()) {
			String db = rst.getString("db");
			if (db.isEmpty() || db.equals("postgres") || db.equals("template0") || db.equals("template1")) {
			} else {
				lstBD.add(db);
				if (rst.isLast() && n == 1) {
				} else {
				}
				n++;
			}
		}

		return !lstBD.isEmpty();
	}

	public boolean isTable(String DBName, String SchemaName, String TableName) throws SQLException {
		ArrayList<String> FailList = new ArrayList<>();
		PreparedStatement pstmt;
		ResultSet RST;

		// First get all columns to compare counts:
		String QuerySQLAllColumns = "SELECT "
			+ "table_name "
			+ "FROM information_schema.columns "
			+ "WHERE table_catalog = ? "
			+ "AND table_schema = ? "
			+ "AND table_name = ? ";
		pstmt = MyConn.prepareStatement(QuerySQLAllColumns, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		int n = 1;
		pstmt.setString(n++, DBName);
		pstmt.setString(n++, SchemaName);
		pstmt.setString(n++, TableName);
		RST = doQuery(pstmt);
		boolean retBool = RST.last();

		return retBool;
	}

	/**
	 *
	 * @param DBName - The name of the database containing the table to be
	 * checked.
	 * @param SchemaName - The name of the schema containing the table to be
	 * checked.
	 * @param TableName - The name of the table whose structure is to be checked.
	 * @param DBStructCheck - Contains all the table fields and their properties.
	 * @param isDebug - Indicates debug mode or not.
	 * @return True if table is valid, false if not.
	 * @throws SQLException
	 */
	public ArrayList<String> DBStructCheck(String DBName, String SchemaName, String TableName, ArrayList<TYP_DBStructCheck> DBStructCheck, boolean isDebug) throws SQLException {
		ArrayList<String> FailList = new ArrayList<>();
		PreparedStatement pstmt;
		ResultSet RST;

		// First get all columns to compare counts:
		String QuerySQLAllColumns = "SELECT "
			+ "table_name, "
			+ "column_name, "
			+ "column_default, "
			+ "is_nullable, "
			+ "data_type, "
			+ "character_maximum_length "
			+ "FROM information_schema.columns "
			+ "WHERE table_catalog = ? "
			+ "AND table_schema = ? "
			+ "AND table_name = ? ";
		pstmt = MyConn.prepareStatement(QuerySQLAllColumns, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		int n = 1;
		pstmt.setString(n++, DBName);
		pstmt.setString(n++, SchemaName);
		pstmt.setString(n++, TableName);
		RST = doQuery(pstmt);
		boolean retBool = RST.last();
		if (retBool) {
			// Missing fields:
			if (RST.getRow() < DBStructCheck.size()) {
				// Missing columns in table:
				int tmpInt = DBStructCheck.size() - RST.getRow();
				if (tmpInt == 1) {
					FailList.add(TableName + ": falta " + tmpInt + " columna en la tabla.");
				} else {
					FailList.add(TableName + ": faltan " + tmpInt + " columnas en la tabla.");
				}
			}
			// TODO: Should we really care if there are extra fields...???
			if (RST.getRow() > DBStructCheck.size()) {
				// Extra columns in table:
				int tmpInt = RST.getRow() - DBStructCheck.size();
				if (tmpInt == 1) {
					FailList.add(TableName + ": hay " + tmpInt + " columna de más en la tabla.");
				} else {
					FailList.add(TableName + ": hay " + tmpInt + " columnas de más en la tabla.");
				}
			}
		} else {
			// no rows:

		}

		// Next compare column by column:
		String QuerySQL = QuerySQLAllColumns + "AND column_name = ?";
		pstmt = MyConn.prepareStatement(QuerySQL, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		n = 1;
		pstmt.setString(n++, DBName);
		pstmt.setString(n++, SchemaName);
		pstmt.setString(n++, TableName);

		for (TYP_DBStructCheck DBStructCheckRow : DBStructCheck) {
			echoln(TableName + "." + DBStructCheckRow.getColumn_name(), isDebug, false);

			pstmt.setString(4, DBStructCheckRow.getColumn_name());
			RST = doQuery(pstmt);
			RST.first();
			echoClassMethodComment(pstmt.toString(), isDebug, false);			// DEBUG...

			if (RST.getRow() == 0) {
				FailList.add(TableName + "." + DBStructCheckRow.getColumn_name() + ": no existe.");
				return FailList;
			}
			if (DBStructCheckRow.getColumn_default() == null ? RST.getString("column_default") == null : DBStructCheckRow.getColumn_default().equals(RST.getString("column_default"))) {
			} else {
				FailList.add(TableName + "." + DBStructCheckRow.getColumn_name() + ": valor por defecto incorrecto.");
			}
			if (DBStructCheckRow.getIs_nullable() == null ? RST.getString("is_nullable") == null : DBStructCheckRow.getIs_nullable().equals(RST.getString("is_nullable"))) {
			} else {
				FailList.add(TableName + "." + DBStructCheckRow.getColumn_name() + ": autorización de nulo incorrecta.");
			}
			if (DBStructCheckRow.getData_type() == null ? RST.getString("data_type") == null : DBStructCheckRow.getData_type().equals(RST.getString("data_type"))) {
			} else {
				FailList.add(TableName + "." + DBStructCheckRow.getColumn_name() + ": tipo de datos incorrecto.");
			}
			if (DBStructCheckRow.getCharacter_maximum_length() > 0) {
				if (DBStructCheckRow.getCharacter_maximum_length() == RST.getInt("character_maximum_length")) {
				} else {
					FailList.add(TableName + "." + DBStructCheckRow.getColumn_name() + ": longitud de caracteres incorrecta.");
				}
			}
			//RST.next();
		}

		return FailList;
	}

}
