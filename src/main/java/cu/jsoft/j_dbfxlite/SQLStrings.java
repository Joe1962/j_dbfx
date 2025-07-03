/*
 * Copyright Joe1962
 * https://github.com/Joe1962
 */
package cu.jsoft.j_dbfxlite;

/**
 *
 * @author joe1962
 */
public abstract class SQLStrings {

	public abstract String getSQLSelectAll();

	public abstract String getSQLSelectByPK();

	public abstract String getSQLSelectByMaster();

	public abstract String getSQLSelectByname();

	public abstract String getSQLDeleteAll();

	public abstract String getSQLAppendRow();

	public abstract String getSQLUpdateRow();

	public String getSQLCountUpserted() {
		return "SELECT \n" +
			"	COUNT(inserted) FILTER (WHERE inserted) AS num_inserted, \n" +
			"	COUNT(updated) FILTER (WHERE updated) AS num_updated \n" +
			"FROM DBTABLE;";
	}

	public String getSQLClearUpserted() {
		return "UPDATE DBTABLE SET inserted = false, updated = false;";
	}

	public abstract String getSQLUpsert(String DBVersionParam);

	public abstract String getSQLSelectByUpserted_PKAndIntField();

}
