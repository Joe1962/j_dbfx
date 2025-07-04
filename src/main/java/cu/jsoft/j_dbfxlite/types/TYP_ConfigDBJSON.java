/*
 * Copyright Joe1962
 * https://github.com/Joe1962
 */
package cu.jsoft.j_dbfxlite.types;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

/**
 *
 * @author joe1962
 */
public class TYP_ConfigDBJSON {
	@JsonProperty("DBNICK")
	private String DBNICK;				// Common name of this specific DB config.

	@JsonProperty("DBCONNTYPE")
	private String DBCONNTYPE;		// "jdbc".

	@JsonProperty("DBDRIVER")
	private String DBDRIVER;			// "postgresql" or "derby".

	@JsonProperty("DBPATH")
	private String DBPATH;				// For derby embedded...

	@JsonProperty("DBHOST")
	private String DBHOST;			// default = "localhost".

	@JsonProperty("DBPORT")
	private int DBPORT;					// default = "5432" if DBDriver = "postgresql".

	@JsonProperty("DBNAME")
	private String DBNAME;				// default = "rms".

	@JsonProperty("DBUSER")
	private String DBUSER;				// default = "herbiesoft".

	@JsonProperty("DBPASS")
	private String DBPASS;				// default = "123".

//	private String DBManager;			// "Server (PostgreSQL)" or "Local (JavaDB)".

	public TYP_ConfigDBJSON() {
	}

	public TYP_ConfigDBJSON(String DBNICK, String DBDRIVER, String DBTYPE, String DBPATH, String DBHOST, int DBPORT, String DBNAME, String DBUSER, String DBPASS) {
		this.DBNICK = DBNICK;
		this.DBCONNTYPE = DBDRIVER;
		this.DBDRIVER = DBTYPE;
		this.DBHOST = DBHOST;
		this.DBPORT = DBPORT;
		this.DBPATH = DBPATH;
		this.DBNAME = DBNAME;
		this.DBUSER = DBUSER;
		this.DBPASS = DBPASS;
	}

	public String getDBNICK() {
		return DBNICK;
	}

	public void setDBNICK(String DBNICK) {
		this.DBNICK = DBNICK;
	}

	/**
	 * @return the DBCONNTYPE
	 */
	public String getDBCONNTYPE() {
		return DBCONNTYPE;
	}

	/**
	 * @param DBCONNTYPE the DBCONNTYPE to set
	 */
	public void setDBCONNTYPE(String DBCONNTYPE) {
		this.DBCONNTYPE = DBCONNTYPE;
	}

	public String getDBDRIVER() {
		return DBDRIVER;
	}

	public void setDBDRIVER(String DBDRIVER) {
		this.DBDRIVER = DBDRIVER;
	}

	public String getDBPATH() {
		return DBPATH;
	}

	public void setDBPATH(String DBPATH) {
		this.DBPATH = DBPATH;
	}

	public String getDBHOST() {
		return DBHOST;
	}

	public void setDBHOST(String DBHOST) {
		this.DBHOST = DBHOST;
	}

	public int getDBPORT() {
		return DBPORT;
	}

	public void setDBPORT(int DBPORT) {
		this.DBPORT = DBPORT;
	}

	public String getDBNAME() {
		return DBNAME;
	}

	public void setDBNAME(String DBNAME) {
		this.DBNAME = DBNAME;
	}

	public String getDBUSER() {
		return DBUSER;
	}

	public void setDBUSER(String DBUSER) {
		this.DBUSER = DBUSER;
	}

	public String getDBPASS() {
		return DBPASS;
	}

	public void setDBPASS(String DBPASS) {
		this.DBPASS = DBPASS;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 19 * hash + Objects.hashCode(this.DBNICK);
		hash = 19 * hash + Objects.hashCode(this.DBCONNTYPE);
		hash = 19 * hash + Objects.hashCode(this.DBDRIVER);
		hash = 19 * hash + Objects.hashCode(this.DBHOST);
		hash = 19 * hash + this.DBPORT;
		hash = 19 * hash + Objects.hashCode(this.DBPATH);
		hash = 19 * hash + Objects.hashCode(this.DBNAME);
		hash = 19 * hash + Objects.hashCode(this.DBUSER);
		hash = 19 * hash + Objects.hashCode(this.DBPASS);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final TYP_ConfigDBJSON other = (TYP_ConfigDBJSON) obj;
		if (this.DBPORT != other.DBPORT) {
			return false;
		}
		if (!Objects.equals(this.DBNICK, other.DBNICK)) {
			return false;
		}
		if (!Objects.equals(this.DBCONNTYPE, other.DBCONNTYPE)) {
			return false;
		}
		if (!Objects.equals(this.DBDRIVER, other.DBDRIVER)) {
			return false;
		}
		if (!Objects.equals(this.DBHOST, other.DBHOST)) {
			return false;
		}
		if (!Objects.equals(this.DBPATH, other.DBPATH)) {
			return false;
		}
		if (!Objects.equals(this.DBNAME, other.DBNAME)) {
			return false;
		}
		if (!Objects.equals(this.DBUSER, other.DBUSER)) {
			return false;
		}
		return Objects.equals(this.DBPASS, other.DBPASS);
	}

}
