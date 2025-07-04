/*
 * Copyright Joe1962
 */
package cu.jsoft.j_dbfxlite.exception;

/**
 *
 * @author Joe1962
 */
public class TYP_jdbc_exception {

	public TYP_jdbc_exception(String pg_sql_state, String pg_condition_class, String pg_condition_name, String mesg, boolean retry) {
		this.pg_sql_state = pg_sql_state;
		this.pg_condition_class = pg_condition_class;
		this.pg_condition_name = pg_condition_name;
		this.mesg = mesg;
		this.retry = retry;
	}

	private String pg_sql_state;
	private String pg_condition_class;
	private String pg_condition_name;
	private String mesg;
	private boolean retry;

	/**
	 * @return the pg_sql_state
	 */
	public String getPg_sql_state() {
		return pg_sql_state;
	}

	/**
	 * @param pg_sql_state the pg_sql_state to set
	 */
	public void setPg_sql_state(String pg_sql_state) {
		this.pg_sql_state = pg_sql_state;
	}

	/**
	 * @return the pg_condition_class
	 */
	public String getPg_condition_class() {
		return pg_condition_class;
	}

	/**
	 * @param pg_condition_class the pg_condition_class to set
	 */
	public void setPg_condition_class(String pg_condition_class) {
		this.pg_condition_class = pg_condition_class;
	}

	/**
	 * @return the pg_condition_name
	 */
	public String getPg_condition_name() {
		return pg_condition_name;
	}

	/**
	 * @param pg_condition_name the pg_condition_name to set
	 */
	public void setPg_condition_name(String pg_condition_name) {
		this.pg_condition_name = pg_condition_name;
	}

	/**
	 * @return the mesg
	 */
	public String getMesg() {
		return mesg;
	}

	/**
	 * @param mesg the mesg to set
	 */
	public void setMesg(String mesg) {
		this.mesg = mesg;
	}

	/**
	 * @return the retry
	 */
	public boolean isRetry() {
		return retry;
	}

	/**
	 * @param retry the retry to set
	 */
	public void setRetry(boolean retry) {
		this.retry = retry;
	}
}
