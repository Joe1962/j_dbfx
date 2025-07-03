/*
 * Copyright Joe1962
 * https://github.com/Joe1962
 */
package cu.jsoft.j_dbfxlite.types;

/**
 *
 * @author joe1962
 */
public class TYPUpsertCounts {
	int i_count;
	int u_count;

	public int getI_count() {
		return i_count;
	}

	public void setI_count(int i_count) {
		this.i_count = i_count;
	}

	public int getU_count() {
		return u_count;
	}

	public void setU_count(int u_count) {
		this.u_count = u_count;
	}

	public TYPUpsertCounts(int i_count, int u_count) {
		this.i_count = i_count;
		this.u_count = u_count;
	}

	public void addToCounts(int new_i_count, int new_u_count) {
		i_count = i_count + new_i_count;
		u_count = u_count + new_u_count;
	}

}
