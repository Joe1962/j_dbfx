/*
 * Copyright Joe1962
 * https://github.com/Joe1962
 */
package cu.jsoft.j_dbfx.types;

/**
 *
 * @author joe1962
 */
public class TYP_RetInsertOrUpdate {
		private int RowsAffected;
		private boolean InsertSuccessful;
		private boolean UpdateSuccessful;

		/**
		 * @return the RowsAffected
		 */
		public int getRowsAffected() {
			return RowsAffected;
		}

		/**
		 * @param aRowsAffected the RowsAffected to set
		 */
		public void setRowsAffected(int aRowsAffected) {
			RowsAffected = aRowsAffected;
		}

		/**
		 * @return the InsertSuccessful
		 */
		public boolean isInsertSuccessful() {
			return InsertSuccessful;
		}

		/**
		 * @param aInsertSuccessful the InsertSuccessful to set
		 */
		public void setInsertSuccessful(boolean aInsertSuccessful) {
			InsertSuccessful = aInsertSuccessful;
		}

		/**
		 * @return the UpdateSuccessful
		 */
		public boolean isUpdateSuccessful() {
			return UpdateSuccessful;
		}

		/**
		 * @param aUpdateSuccessful the UpdateSuccessful to set
		 */
		public void setUpdateSuccessful(boolean aUpdateSuccessful) {
			UpdateSuccessful = aUpdateSuccessful;
		}
	
}
