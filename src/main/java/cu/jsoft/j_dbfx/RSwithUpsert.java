/*
 * Copyright Joe1962
 * https://github.com/Joe1962
 */
package cu.jsoft.j_dbfx;

import cu.jsoft.j_dbfx.types.TYPUpsertCounts;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author joe1962
 */
public abstract class RSwithUpsert extends RS {
	protected String SQL_CountUpserted = "SELECT \n" +
			"	COUNT(inserted) FILTER (WHERE inserted) AS num_inserted, \n" +
			"	COUNT(updated) FILTER (WHERE updated) AS num_updated \n" +
			"FROM " + dbTable + ";";
	protected String SQL_ClearUpserted = "UPDATE public." + dbTable + " SET \n" +
			"	inserted = false,\n" +
			"	updated = false;";
	protected String SQL_Upsert;

	public RSwithUpsert() {
	}
	
	public abstract int CountByPOS(int SelectedPOS) throws SQLException;

	public TYPUpsertCounts CountUpserted() throws SQLException {
		SQL_CountUpserted = SQL_CountUpserted.replaceFirst("PLACEHOLDER_dbTable", dbTable);

		TYPUpsertCounts MyCounts = new TYPUpsertCounts(0, 0);

		PreparedStatement pstmt = getMyConn().prepareStatement(SQL_CountUpserted);
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()){
			MyCounts.setI_count(rs.getInt("num_inserted"));
			MyCounts.setU_count(rs.getInt("num_updated"));
		}
		return MyCounts;
	}

	public boolean ClearUpserted() throws SQLException {
		SQL_ClearUpserted = SQL_ClearUpserted.replaceFirst("PLACEHOLDER_dbTable", dbTable);

		PreparedStatement pstmt = getMyConn().prepareStatement(SQL_ClearUpserted);
		return pstmt.executeUpdate() == 1;
	}

	public TYPUpsertCounts Upsert(int SelectedPOS) throws SQLException {
		//SQL_Upsert = SQL_Upsert.replaceFirst("PLACEHOLDER_dbTable", dbTable);
		SQL_Upsert = SQL_Upsert.replaceFirst("PLACEHOLDER_SelectedPOS", String.valueOf(SelectedPOS));

		ClearUpserted();

		PreparedStatement pstmt = getMyConn().prepareStatement(SQL_Upsert);
		pstmt.executeUpdate();

		return CountUpserted();
	}

}
