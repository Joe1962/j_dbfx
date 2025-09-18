/*
 * Copyright Joe1962
 */
package cu.jsoft.j_dbfx;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author joe1962
 */
public class DBPostgresFDW {
	
	public void CreateServer(Connection MyConn, String MyServerName, String MyHost, String MyDB, String MyUser) throws SQLException {
		String SQL = "CREATE SERVER IF NOT EXISTS " + MyServerName + " FOREIGN DATA WRAPPER postgres_fdw OPTIONS (host '" + MyHost + "', dbname '" + MyDB + "');";
		SQL = SQL + "\n";
		SQL = SQL + "GRANT USAGE ON FOREIGN SERVER " + MyServerName + " TO " + MyUser + ";";
		PreparedStatement pstmt = MyConn.prepareStatement(SQL);
		pstmt.execute();
	}

	public void CreateUserMapping(Connection MyConn, String MyServerName, String MyUser, String MyPass) throws SQLException {
		String SQL = "CREATE USER MAPPING IF NOT EXISTS FOR " + MyUser + " SERVER " + MyServerName + " OPTIONS (user '" + MyUser + "', password '" + MyPass + "');";
		PreparedStatement pstmt = MyConn.prepareStatement(SQL);
		pstmt.execute();
	}

	public void ImportForeignTables(Connection MyConn, String MyServerName, String MyUser, String MyForeignSchema, String MyLocalSchema, String LimitToTables) throws SQLException {
		String SQL = "CREATE SCHEMA IF NOT EXISTS " + MyLocalSchema + " AUTHORIZATION " + MyUser + ";";
		SQL = SQL + "\n";
		SQL = SQL + "IMPORT FOREIGN SCHEMA " + MyForeignSchema + " LIMIT TO (" + LimitToTables + ") FROM SERVER " + MyServerName + " INTO " + MyLocalSchema + ";";
		PreparedStatement pstmt = MyConn.prepareStatement(SQL);
		pstmt.execute();
	}

	public void DropServer(Connection MyConn, String MyServerName, String MyHost, String MyDB, String MyUser) throws SQLException {
		String SQL = "DROP SERVER IF EXISTS " + MyServerName + " CASCADE;";
		PreparedStatement pstmt = MyConn.prepareStatement(SQL);
		pstmt.execute();
	}

	public void DeleteUserMapping(Connection MyConn, String MyServerName, String MyUser, String MyPass) throws SQLException {
		String SQL = "DROP USER MAPPING IF EXISTS FOR " + MyUser + " SERVER " + MyServerName + ";";
		PreparedStatement pstmt = MyConn.prepareStatement(SQL);
		pstmt.execute();
	}

	public void DropForeignSchema(Connection MyConn, String MyServerName, String MyForeignSchema) throws SQLException {
		String SQL = "DROP SCHEMA IF EXISTS " + MyForeignSchema + " CASCADE;";
		PreparedStatement pstmt = MyConn.prepareStatement(SQL);
		pstmt.execute();
	}

}

