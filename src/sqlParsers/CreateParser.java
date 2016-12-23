package sqlParsers;

import dBManagement.DBManager;
import query.Query;

public class CreateParser extends Parser {

	public CreateParser(DBManager dbManager) {
		super(dbManager);
	}

	public Query useCommand(String dbName) {
		if (validateName(dbName)) {
			return dbManager.useDataBase(dbName.toLowerCase());
		}
		return new Query(false, "Invalid database name: " + dbName);
	}

	public Query addDataBase(String dbName) {
		if (validateName(dbName)) {
			return dbManager.addDataBase(dbName.toLowerCase());
		}
		return new Query(false, "Invalid database name: " + dbName);
	}

	public Query dropDataBase(String dbName) {
		if (validateName(dbName)) {
			return dbManager.dropDataBase(dbName.toLowerCase());
		}
		return new Query(false, "Invalid Database name: " + dbName);
	}

	public Query addTable(String command) {
		int firstIdx = command.indexOf("(");
		int secondIdx = command.lastIndexOf(")");
		if (firstIdx == -1 || secondIdx == -1 || secondIdx != command.length() - 1) {
			return new Query(false, "Unidentified format: " + command);
		}
		String tableName = command.substring(0, firstIdx).trim();
		String[][] tableData = getTableData(command.substring(command.indexOf("(")));
		if (tableData == null) {
			return new Query(false, "Wrong syntax declaring table schema.");
		} else if (checkDuplicate2D(tableData)) {
			return new Query(false, "Duplicate column name");
		} else if (validateName(tableName)) {
			return dbManager.addTable(tableName.toLowerCase(), tableData);
		}
		return new Query(false, "Invalid table name: " + tableName);
	}

	public Query dropTable(String tableName) {
		if (validateName(tableName)) {
			return dbManager.dropTable(tableName.toLowerCase());
		}
		return new Query(false, "Invalid table name: " + tableName);
	}

	private boolean validateName(String name) {
		if (invalidName(name)) {
			printError("Wrong or unidentified name.");
			return false;
		}
		return true;
	}
}