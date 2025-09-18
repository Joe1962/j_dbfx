/*
 * Copyright Joe1962
 * https://github.com/Joe1962
 */
package cu.jsoft.j_dbfx.exception;

import cu.jsoft.j_dbfx.types.TYP_ConfigDBJSON;
import static cu.jsoft.j_utilsfx.global.CONSTS.NEW_LINE;
import static cu.jsoft.j_utilsfx.subs.SUB_PopupsFX.SimpleDialog;
import static cu.jsoft.j_utilsfx.subs.SUB_UtilsFXResources.getResourceImage;
import static cu.jsoft.j_utilsfx.subs.SUB_UtilsNotifications.echoln;
import java.sql.SQLException;
import java.util.HashMap;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

public class SQLx {
	private Font errHeaderFont;
	private Font errContentFont;
	private TYP_ConfigDBJSON theDBConfig;

	public SQLx(TYP_ConfigDBJSON theDBConfig, Font errHeaderFont, Font errContentFont) {
		this.theDBConfig = theDBConfig;
		this.errHeaderFont = errHeaderFont;
		this.errContentFont = errContentFont;
	}

	public void handleException(Exception e, int excType, String strDBTable, boolean DebugMode) {
		if (e.getClass() == SQLException.class) {
			switch (excType) {
				case 11:			// SELECT, goNext/goPrev or getCurrent errors...
//					GLOBAL.logLogger.log(Level.SEVERE, "Error cargando registros desde la tabla " + strDBTable + ". Detalles: {0}", new Object[]{e.getMessage()});
					echoln("Error cargando registros desde la tabla " + strDBTable + ".", DebugMode, false);
					// TODO: message dialog...???
					break;
				default:
					break;
			}
		}
	}

	public boolean handleExceptionPG(SQLException e, boolean DebugMode) {
		// return true for retry, false for not...
		TYP_jdbc_exception MyJDBCException = mapExceptionPG().get(e.getSQLState());

		StackTraceElement callerTrace = new Throwable().getStackTrace()[1];
		HashMap<String, String> callerInfo = new HashMap<>();
		callerInfo.put("file", callerTrace.getFileName().substring(0, callerTrace.getFileName().lastIndexOf('.')));
		callerInfo.put("class", callerTrace.getClassName());
		callerInfo.put("method", callerTrace.getMethodName());
		callerInfo.put("line", Integer.toString(callerTrace.getLineNumber()));

		if (MyJDBCException == null) {
//			GLOBAL.logLogger.log(Level.SEVERE, "Unknown PG SQL Exception" + ". Detalles: {0}", new Object[]{e.getMessage()});
			echoln("Unknown PG SQL Exception. Detalles: " + e.getMessage(), DebugMode, false);
			String strExtraInfo = setupExtraInfoText(callerInfo, true);
			// TODO: old webview stuff to convert...
			//doDLGSQLCritErrUnMapped("ERROR SQL: " + e.getSQLState(), e.getLocalizedMessage(), strExtraInfo + Arrays.toString(e.getStackTrace()));
			doDLGSQLCritErrText("ERROR SQL: " + e.getSQLState(), e.getLocalizedMessage(), strExtraInfo);
		} else {
			if (MyJDBCException.isRetry()) {
				String strExtraInfo = setupExtraInfoText(callerInfo, false);
				// TODO: old webview stuff to convert...
				if (doDLGSQLModConfig("ERROR SQL: " + MyJDBCException.getPg_sql_state(), MyJDBCException.getMesg(), strExtraInfo)) {
					return true;
				}
			} else {
				String strExtraInfo = setupExtraInfoText(callerInfo, false);
				doDLGSQLCritErrText("ERROR SQL: " + MyJDBCException.getPg_sql_state() + ", " + MyJDBCException.getMesg(), e.getLocalizedMessage(), strExtraInfo);
			}
		}
		return false;
	}

	public TYP_jdbc_exception retExceptionPG(SQLException e) {
		return mapExceptionPG().get(e.getSQLState());
	}

	private String setupExtraInfoText(HashMap callerInfo, boolean boolUnmapped) {
		StringBuilder ExtraInfo = new StringBuilder();

		ExtraInfo.append("Clase: ").append(callerInfo.get("file")).append(NEW_LINE);
		ExtraInfo.append("Método: ").append(callerInfo.get("method")).append(NEW_LINE);
		ExtraInfo.append("Línea: ").append(callerInfo.get("line")).append(NEW_LINE);
		ExtraInfo.append(NEW_LINE);
		ExtraInfo.append("Driver: ").append(theDBConfig.getDBDRIVER()).append(NEW_LINE);
		ExtraInfo.append("Server: ").append(theDBConfig.getDBHOST()).append(NEW_LINE);
		ExtraInfo.append("Puerto: ").append(theDBConfig.getDBPORT()).append(NEW_LINE);
		ExtraInfo.append("BD: ").append(theDBConfig.getDBNAME()).append(NEW_LINE);

		return ExtraInfo.toString();
	}

	private void doDLGSQLCritErrText(String titleMessage, String headerMessage, String strExtraInfo) {
		Label LabelHeader = new Label();
		LabelHeader.setFont(errHeaderFont);
		LabelHeader.setText(headerMessage);
		LabelHeader.setPadding(new Insets(10, 15, 15, 15));

		// Prep ERROR icon:
		ImageView IconError = getResourceImage("icons/64/dialog-error.png");

			// Set up HBOX with Labelheader and IconError:
			HBox theHeader = new HBox();
			theHeader.setAlignment(Pos.CENTER);
			theHeader.getChildren().add(LabelHeader);
			theHeader.getChildren().add(IconError);
			HBox.setMargin(IconError, new Insets(25, 25, 25, 25));

			// Set up sql code in TextArea:
			TextArea TextAreaContent = new TextArea();
			TextAreaContent.setEditable(false);
			TextAreaContent.setFont(errContentFont);
			TextAreaContent.setText(strExtraInfo);

			// Encapsulate TextArea in TitledPane:
			TitledPane MyTitledPane = new TitledPane();
			MyTitledPane.setContent(TextAreaContent);
			MyTitledPane.setMaxHeight(1.7976931348623157E308);
			MyTitledPane.setText("Información técnica...");
			MyTitledPane.setExpanded(false);
			MyTitledPane.setAnimated(false);
			MyTitledPane.expandedProperty().addListener((obs, oldValue, newValue) -> {
				Platform.runLater(() -> {
					MyTitledPane.requestLayout();
					MyTitledPane.getScene().getWindow().sizeToScene();
				});
			});

			// Encapsulate header and content in a BorderPane to handle resizing:
			BorderPane MyBorderPane = new BorderPane();
//			MyBorderPane.setTop(LabelHeader);
			MyBorderPane.setTop(theHeader);
			MyBorderPane.setCenter(MyTitledPane);

			// Make TitledPane the SimpleDialog content:
			Node MyContent = MyBorderPane;
//				Node MyContent = LabelHeader;
			// Show SimpleDialog:
			SimpleDialog("ERROR...!!!", MyContent, "OK", 0, 0);

	}

	private HashMap<String, TYP_jdbc_exception> mapExceptionPG() {
		HashMap<String, TYP_jdbc_exception> mapJDBCException = new HashMap<>();

		// Placeholder for adding exceptions:
		//mapJDBCException.put("", new TYP_jdbc_exception("", "Class  - ", "", "", false));
		// Class 08 — Connection Exception:
		mapJDBCException.put("08000", new TYP_jdbc_exception("08000", "Class 08 - Connection Exception", "connection_exception", "Excepción en la conexión SQL", false));
		mapJDBCException.put("08001", new TYP_jdbc_exception("08001", "Class 08 - Connection Exception", "sqlclient_unable_to_establish_sqlconnection", "Imposible establecer conexión SQL", false));
		mapJDBCException.put("08003", new TYP_jdbc_exception("08003", "Class 08 - Connection Exception", "connection_does_not_exist", "No existe la conexión SQL", false));
		mapJDBCException.put("08004", new TYP_jdbc_exception("08004", "Class 08 - Connection Exception", "sqlserver_rejected_establishment_of_sqlconnection", "El servidor rechazó la conexión SQL", false));
		mapJDBCException.put("08006", new TYP_jdbc_exception("08006", "Class 08 - Connection Exception", "connection_failure", "Falla en la conexión SQL", false));
		mapJDBCException.put("08007", new TYP_jdbc_exception("08007", "Class 08 - Connection Exception", "transaction_resolution_unknown", "Resolución desconocida de la transacción en la conexión SQL", false));
		mapJDBCException.put("08P01", new TYP_jdbc_exception("08P01", "Class 08 - Connection Exception", "protocol_violation", "Violación de protocolo en la conexión SQL", false));

		// Class 00 — Successful Completion:
		// Class 01 — Warning:
		// Class 02 — No Data (this is also a warning class per the SQL standard):
		// Class 03 — SQL Statement Not Yet Complete:
		// Class 08 — Connection Exception:
		// Class 09 — Triggered Action Exception:
		// Class 0A — Feature Not Supported:
		// Class 0B — Invalid Transaction Initiation:
		// Class 0F — Locator Exception:
		// Class 0L — Invalid Grantor:
		// Class 0P — Invalid Role Specification:
		// Class 0Z — Diagnostics Exception:
		// Class 20 — Case Not Found:
		// Class 21 — Cardinality Violation:
		// Class 22 — Data Exception:
		// Class 23 — Integrity Constraint Violation:
		mapJDBCException.put("23000", new TYP_jdbc_exception("23000", "Class  - Integrity Constraint Violation", "integrity_constraint_violation", "Violación de integridad", false));
		mapJDBCException.put("23001", new TYP_jdbc_exception("23001", "Class  - Integrity Constraint Violation", "restrict_violation", "Violación de restricción", false));
		mapJDBCException.put("23502", new TYP_jdbc_exception("23502", "Class  - Integrity Constraint Violation", "not_null_violation", "Violación de no nulo", false));
		mapJDBCException.put("23503", new TYP_jdbc_exception("23503", "Class  - Integrity Constraint Violation", "foreign_key_violation", "Violación de llave foránea", false));
		mapJDBCException.put("23505", new TYP_jdbc_exception("23505", "Class  - Integrity Constraint Violation", "unique_violation", "Violación de unicidad", false));
		mapJDBCException.put("23514", new TYP_jdbc_exception("23514", "Class  - Integrity Constraint Violation", "check_violation", "Violación de chequeo", false));
		mapJDBCException.put("23P01", new TYP_jdbc_exception("23P01", "Class  - Integrity Constraint Violation", "exclusion_violation", "Violación de exclusión", false));

		// Class 24 — Invalid Cursor State:
		// Class 25 — Invalid Transaction State:
		// Class 26 — Invalid SQL Statement Name:
		// Class 27 — Triggered Data Change Violation:
		// Class 28 — Invalid Authorization Specification:
		mapJDBCException.put("28000", new TYP_jdbc_exception("28000", "Class 28 - Invalid Authorization Specification", "invalid_authorization_specification", "Especificación de autorización inválida en la conexión SQL", false));
		mapJDBCException.put("28P01", new TYP_jdbc_exception("28P01", "Class 28 - Invalid Authorization Specification", "invalid_password", "Password inválido en la conexión SQL", false));

		// Class 2B — Dependent Privilege Descriptors Still Exist:
		// Class 2D — Invalid Transaction Termination:
		// Class 2F — SQL Routine Exception:
		// Class 34 — Invalid Cursor Name:
		// Class 38 — External Routine Exception:
		// Class 39 — External Routine Invocation Exception:
		// Class 3B — Savepoint Exception:
		// Class 3D — Invalid Catalog Name:
		mapJDBCException.put("3D000", new TYP_jdbc_exception("3D000", "Class 3D - Invalid Catalog Name", "invalid_catalog_name", "No existe la base de datos", false));

		// Class 3F — Invalid Schema Name:
		mapJDBCException.put("3F000", new TYP_jdbc_exception("3F000", "Class 3F - Invalid Schema Name", "invalid_schema_name", "No existe el schema", false));

		// Class 40 — Transaction Rollback:
		// Class 42 — Syntax Error or Access Rule Violation:
		mapJDBCException.put("42000", new TYP_jdbc_exception("42000", "Class 42 - Syntax Error or Access Rule Violation", "syntax_error_or_access_rule_violation", "", false));
		mapJDBCException.put("42601", new TYP_jdbc_exception("42601", "Class 42 - Syntax Error or Access Rule Violation Error", "syntax_error", "", false));
		mapJDBCException.put("42501", new TYP_jdbc_exception("42501", "Class 42 - Syntax Error or Access Rule Violation privilege", "insufficient_privilege", "", false));
		mapJDBCException.put("42846", new TYP_jdbc_exception("42846", "Class 42 - Syntax Error or Access Rule Violation", "cannot_coerce", "", false));
		mapJDBCException.put("42803", new TYP_jdbc_exception("42803", "Class 42 - Syntax Error or Access Rule Violation", "grouping_error", "", false));
		mapJDBCException.put("42P20", new TYP_jdbc_exception("42P20", "Class 42 - Syntax Error or Access Rule Violation", "windowing_error", "", false));
		mapJDBCException.put("42P19", new TYP_jdbc_exception("42P19", "Class 42 - Syntax Error or Access Rule Violation", "invalid_recursion", "", false));
		mapJDBCException.put("42830", new TYP_jdbc_exception("42830", "Class 42 - Syntax Error or Access Rule Violation", "invalid_foreign_key", "", false));
		mapJDBCException.put("42602", new TYP_jdbc_exception("42602", "Class 42 - Syntax Error or Access Rule Violation", "invalid_name", "", false));
		mapJDBCException.put("42622", new TYP_jdbc_exception("42622", "Class 42 - Syntax Error or Access Rule Violation", "name_too_long", "", false));
		mapJDBCException.put("42939", new TYP_jdbc_exception("42939", "Class 42 - Syntax Error or Access Rule Violation", "reserved_name", "", false));
		mapJDBCException.put("42804", new TYP_jdbc_exception("42804", "Class 42 - Syntax Error or Access Rule Violation", "datatype_mismatch", "", false));
		mapJDBCException.put("42P18", new TYP_jdbc_exception("42P18", "Class 42 - Syntax Error or Access Rule Violation", "indeterminate_datatype", "", false));
		mapJDBCException.put("42P21", new TYP_jdbc_exception("42P21", "Class 42 - Syntax Error or Access Rule Violation", "collation_mismatch", "", false));
		mapJDBCException.put("42P22", new TYP_jdbc_exception("42P22", "Class 42 - Syntax Error or Access Rule Violation", "indeterminate_collation", "", false));
		mapJDBCException.put("42809", new TYP_jdbc_exception("42809", "Class 42 - Syntax Error or Access Rule Violation", "wrong_object_type", "", false));
		mapJDBCException.put("42703", new TYP_jdbc_exception("42703", "Class 42 - Undefined column", "undefined_column", "Campo inexistente", false));
		mapJDBCException.put("42883", new TYP_jdbc_exception("42883", "Class 42 - Syntax Error or Access Rule Violation", "undefined_function", "", false));
		mapJDBCException.put("42P01", new TYP_jdbc_exception("42P01", "Class 42 - Syntax Error or Access Rule Violation", "undefined_table", "", false));
		mapJDBCException.put("42P02", new TYP_jdbc_exception("42P02", "Class 42 - Syntax Error or Access Rule Violation", "undefined_parameter", "", false));
		mapJDBCException.put("42704", new TYP_jdbc_exception("42704", "Class 42 - Syntax Error or Access Rule Violation", "undefined_object", "", false));
		mapJDBCException.put("42701", new TYP_jdbc_exception("42701", "Class 42 - Syntax Error or Access Rule Violation", "duplicate_column", "", false));
		mapJDBCException.put("42P03", new TYP_jdbc_exception("42P03", "Class 42 - Syntax Error or Access Rule Violation", "duplicate_cursor", "", false));
		mapJDBCException.put("42P04", new TYP_jdbc_exception("42P04", "Class 42 - Syntax Error or Access Rule Violation", "duplicate_database", "", false));
		mapJDBCException.put("42723", new TYP_jdbc_exception("42723", "Class 42 - Syntax Error or Access Rule Violation", "duplicate_function", "", false));
		mapJDBCException.put("42P05", new TYP_jdbc_exception("42P05", "Class 42 - Syntax Error or Access Rule Violation", "duplicate_prepared_statement", "", false));
		mapJDBCException.put("42P06", new TYP_jdbc_exception("42P06", "Class 42 - Syntax Error or Access Rule Violation", "duplicate_schema", "", false));
		mapJDBCException.put("42P07", new TYP_jdbc_exception("42P07", "Class 42 - Syntax Error or Access Rule Violation", "duplicate_table", "", false));
		mapJDBCException.put("42712", new TYP_jdbc_exception("42712", "Class 42 - Syntax Error or Access Rule Violation", "duplicate_alias", "", false));
		mapJDBCException.put("42710", new TYP_jdbc_exception("42710", "Class 42 - Syntax Error or Access Rule Violation", "duplicate_object", "", false));
		mapJDBCException.put("42702", new TYP_jdbc_exception("42702", "Class 42 - Syntax Error or Access Rule Violation", "ambiguous_column", "", false));
		mapJDBCException.put("42725", new TYP_jdbc_exception("42725", "Class 42 - Syntax Error or Access Rule Violation", "ambiguous_function", "", false));
		mapJDBCException.put("42P08", new TYP_jdbc_exception("42P08", "Class 42 - Syntax Error or Access Rule Violation", "ambiguous_parameter", "", false));
		mapJDBCException.put("42P09", new TYP_jdbc_exception("42P09", "Class 42 - Syntax Error or Access Rule Violation", "ambiguous_alias", "", false));
		mapJDBCException.put("42P10", new TYP_jdbc_exception("42P10", "Class 42 - Syntax Error or Access Rule Violation", "invalid_column_reference", "", false));
		mapJDBCException.put("42611", new TYP_jdbc_exception("42611", "Class 42 - Syntax Error or Access Rule Violation", "invalid_column_definition", "", false));
		mapJDBCException.put("42P11", new TYP_jdbc_exception("42P11", "Class 42 - Syntax Error or Access Rule Violation", "invalid_cursor_definition", "", false));
		mapJDBCException.put("42P12", new TYP_jdbc_exception("42P12", "Class 42 - Syntax Error or Access Rule Violation", "invalid_database_definition", "", false));
		mapJDBCException.put("42P13", new TYP_jdbc_exception("42P13", "Class 42 - Syntax Error or Access Rule Violation", "invalid_function_definition", "", false));
		mapJDBCException.put("42P14", new TYP_jdbc_exception("42P14", "Class 42 - Syntax Error or Access Rule Violation", "invalid_prepared_statement_definition", "", false));
		mapJDBCException.put("42P15", new TYP_jdbc_exception("42P15", "Class 42 - Syntax Error or Access Rule Violation", "invalid_schema_definition", "", false));
		mapJDBCException.put("42P16", new TYP_jdbc_exception("42P16", "Class 42 - Syntax Error or Access Rule Violation", "invalid_table_definition", "", false));
		mapJDBCException.put("42P17", new TYP_jdbc_exception("42P17", "Class 42 - Syntax Error or Access Rule Violation", "invalid_object_definition", "", false));

		// Class 44 — WITH CHECK OPTION Violation:
		// Class 53 — Insufficient Resources:
		mapJDBCException.put("53000", new TYP_jdbc_exception("53000", "Class 53 - Insufficient Resources", "insufficient_resources", "Recursos insuficientes", false));
		mapJDBCException.put("53100", new TYP_jdbc_exception("53100", "Class 53 - Insufficient Resources", "disk_full", "Disco lleno", false));
		mapJDBCException.put("53200", new TYP_jdbc_exception("53200", "Class 53 - Insufficient Resources", "out_of_memory", "Memoria insuficiente", false));
		mapJDBCException.put("53300", new TYP_jdbc_exception("53300", "Class 53 - Insufficient Resources", "too_many_connections", "Conexiones excesivas", true));
		mapJDBCException.put("53400", new TYP_jdbc_exception("53400", "Class 53 - Insufficient Resources", "configuration_limit_exceeded", "Límites configurados excedidos", false));

		// Class 54 — Program Limit Exceeded:
		// Class 55 — Object Not In Prerequisite State:
		// Class 57 — Operator Intervention:
		// Class 58 — System Error (errors external to PostgreSQL itself):
		mapJDBCException.put("58000", new TYP_jdbc_exception("58000", "Class 58 - System Error (errors external to PostgreSQL itself)", "system_error", "Error del Sistema Operativo", false));
		mapJDBCException.put("58030", new TYP_jdbc_exception("58030", "Class 58 - System Error (errors external to PostgreSQL itself)", "io_error", "Error de Lectura/Escritura", false));
		mapJDBCException.put("58P01", new TYP_jdbc_exception("58P01", "Class 58 - System Error (errors external to PostgreSQL itself)", "undefined_file", "Fichero indefinido", false));
		mapJDBCException.put("58P02", new TYP_jdbc_exception("58P02", "Class 58 - System Error (errors external to PostgreSQL itself)", "duplicate_file", "Fichero duplicado", false));

		// Class 72 — Snapshot Failure:
		// Class F0 — Configuration File Error:
		mapJDBCException.put("F0000", new TYP_jdbc_exception("F0000", "Class F0 - Configuration File Error", "config_file_error", "Error en fichero de configuración", false));
		mapJDBCException.put("F0001", new TYP_jdbc_exception("F0001", "Class F0 - Configuration File Error", "lock_file_exists", "Fichero de bloqueo existe", false));

		// Class HV — Foreign Data Wrapper Error (SQL/MED):
		// Class P0 — PL/pgSQL Error:
		// Class XX — Internal Error:
		mapJDBCException.put("XX000", new TYP_jdbc_exception("XX000", "Class XX - Internal Error", "internal_error", "Error interno", false));
		mapJDBCException.put("XX001", new TYP_jdbc_exception("XX001", "Class XX - Internal Error", "data_corrupted", "Datos corruptos", false));
		mapJDBCException.put("XX002", new TYP_jdbc_exception("XX002", "Class XX - Internal Error", " 	index_corrupted", "Indice corrupto", false));

		return mapJDBCException;
	}



	// TODO: old webview stuff to convert...

	private String setupExtraInfoHTML(HashMap callerInfo, boolean boolUnmapped) {
//		// TODO: Prep this for derby also...
//		String strExtraInfo
//			= "<table border='0' cellspacing='0' cellpadding='0'>"
//			+ "<col width='125'>"
//			+ "<tr><td align='right'>Clase:&nbsp;</td><td align='left'>" + callerInfo.get("file") + "</td></tr>"
//			+ "<tr><td align='right'>Método:&nbsp;</td><td align='left'>" + callerInfo.get("method") + "</td></tr>"
//			+ "<tr><td align='right'>Línea:&nbsp;</td><td align='left'>" + callerInfo.get("line") + "</td></tr>"
//			+ "</table>"
//			+ "<hr width=85%>"
//			+ "<table border='0' cellspacing='0' cellpadding='0'>"
//			//+ "<tr><td align='right'>Tipo de driver:&nbsp;</td><td align='left'>" + GLOBAL.DBDriverType + "</td></tr>"
//			+ "<col width='125'>"
//			+ "<tr><td align='right'>Driver:&nbsp;</td><td align='left'>" + GLOBAL.DBCONFIG.get(GLOBAL.DBDefaultDB).getDBDRIVER() + "</td></tr>"
//			+ "<tr><td align='right'>Server:&nbsp;</td><td align='left'>" + GLOBAL.DBCONFIG.get(GLOBAL.DBDefaultDB).getDBHOST() + "</td></tr>"
//			+ "<tr><td align='right'>Puerto:&nbsp;</td><td align='left'>" + GLOBAL.DBCONFIG.get(GLOBAL.DBDefaultDB).getDBPORT() + "</td></tr>"
//			+ "<tr><td align='right'>BD:&nbsp;</td><td align='left'>" + GLOBAL.DBCONFIG.get(GLOBAL.DBDefaultDB).getDBNAME() + "</td></tr>"
//			+ "</table></center>";
//		if (boolUnmapped) {
//			strExtraInfo = strExtraInfo + "<hr width=85%>";
//		}
//		return strExtraInfo;

		// temp return just to avoid code error::
		return null;
	}

	private void doDLGSQLCritErrHTML(String titleMessage, String headerMessage, String strExtraInfo) {
//		Alert alert = new Alert(AlertType.ERROR);
//		alert.initModality(Modality.APPLICATION_MODAL);
//		alert.setTitle(titleMessage);
//		alert.setHeaderText(headerMessage);
//		WebView webView = new WebView();
//		webView.getEngine().loadContent("<html>" + strExtraInfo + "</html>");
//		webView.setPrefSize(500, 175);
//		alert.getDialogPane().setContent(webView);
//		alert.showAndWait();
	}

	private void doDLGSQLCritErrUnMapped(String titleMessage, String headerMessage, String strExtraInfo) {
//		Alert alert = new Alert(AlertType.ERROR);
//		alert.initModality(Modality.APPLICATION_MODAL);
//		alert.setTitle(titleMessage);
//		alert.setHeaderText(headerMessage);
//		WebView webViewContent = new WebView();
//		webViewContent.getEngine().loadContent("<html>" + strExtraInfo + "</html>");
//		webViewContent.setPrefSize(785, 250);
//		alert.getDialogPane().setContent(webViewContent);
//		alert.showAndWait();
	}

	private boolean doDLGSQLModConfig(String titleMessage, String headerMessage, String strExtraInfo) {
//		Alert alert = new Alert(AlertType.CONFIRMATION);
//		alert.setTitle(titleMessage);
//		alert.setHeaderText(headerMessage + ". " + "Modificar configuración?");
//		alert.initModality(Modality.APPLICATION_MODAL);
//		WebView webView = new WebView();
//		webView.getEngine().loadContent("<html>" + strExtraInfo + "</html>");
//		webView.setPrefSize(500, 175);
//		alert.getDialogPane().setContent(webView);
//		Optional<ButtonType> response = alert.showAndWait();
//		return response.get() == ButtonType.OK;

		// temp return just to avoid code error::
		return false;
	}

}
