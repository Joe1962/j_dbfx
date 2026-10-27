/*
 * Copyright Joe1962
 * https://github.com/Joe1962
 */
package cu.jsoft.j_dbfx.types;

/**
 *
 * @author joe1962
 */
public class TYP_FieldValuePair {
	private String fieldName;
	private Object fieldValue;

	public TYP_FieldValuePair() {
	}

	public TYP_FieldValuePair(String fieldName, Object fieldValue) {
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}

	public Object getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(Object fieldValue) {
		this.fieldValue = fieldValue;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

}
