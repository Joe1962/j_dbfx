/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cu.jsoft.j_dbfxlite;

/**
 *
 * @author joe1962
 */
public class LibInfo {
	// Lib constants:
	private static final String TITLE = "j_dbfx-lite";
	private static final String VERSION = "1.0.0";
	private static final String BUILD = "250619.01";


	/**
	 * @return the TITLE
	 */
	public static String getTITLE() {
		return TITLE;
	}

	/**
	 * @return the VERSION
	 */
	public static String getVERSION() {
		return VERSION;
	}

	/**
	 * @return the BUILD
	 */
	public static String getBUILD() {
		return BUILD;
	}



	public static LibInfo getInstance() {
		return infoHolder.INSTANCE;
	}

	private LibInfo() {
	}

	private static class infoHolder {

		private static final LibInfo INSTANCE = new LibInfo();
	}

}
