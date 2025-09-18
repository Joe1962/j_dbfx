/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cu.jsoft.j_dbfx.types;

/**
 *
 * @author Joe1962
 */
public class TYP_DBFFields {

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the numwidth
	 */
	public int getNumwidth() {
		return numwidth;
	}

	/**
	 * @param numwidth the numwidth to set
	 */
	public void setNumwidth(int numwidth) {
		this.numwidth = numwidth;
	}

	/**
	 * @return the numdecimals
	 */
	public int getNumdecimals() {
		return numdecimals;
	}

	/**
	 * @param numdecimals the numdecimals to set
	 */
	public void setNumdecimals(int numdecimals) {
		this.numdecimals = numdecimals;
	}
	private String name;
	private String type;
	private int numwidth;
	private int numdecimals;
}
