/*
 * Copyright Joe1962
 * https://github.com/Joe1962
 */
package cu.jsoft.j_dbfx;

import cu.jsoft.j_dbfx.types.TYPUpsertCounts;
import java.sql.SQLException;

/**
 *
 * @author joe1962
 */
public abstract class RSwithUpsert extends RS {
	
	public abstract int CountByPOS(int SelectedPOS) throws SQLException;

	public abstract TYPUpsertCounts CountUpserted() throws SQLException;

	public abstract boolean ClearUpserted() throws SQLException;

	public abstract TYPUpsertCounts Upsert(int SelectedPOS, String DBVersionParam) throws SQLException;



	// Additional methods that should be in RS, but would cause too much trouble right now:

	public abstract void selectByPOS(String OrderByString, Object MyMaster) throws SQLException;

	public abstract void selectByPOSDate(String OrderByString, Object MyMaster) throws SQLException;

	public abstract void selectByPOSDateOrphans(String OrderByString, Object MyMaster) throws SQLException;

}
