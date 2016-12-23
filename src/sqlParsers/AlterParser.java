package sqlParsers;

import dBManagement.DBManager;
import query.Query;

public class AlterParser extends Parser {

	public AlterParser(DBManager dbManager) {
		super(dbManager);
	}

	public Query alterQuery(String command) {
		command = command.toLowerCase();
		int addIdx = command.indexOf(" add ");
		int dropIdx = command.indexOf(" drop column ");
		if (addIdx == -1 && dropIdx != -1) {
			String tableName = command.substring(0, dropIdx);
			String input = command.substring(dropIdx + 13);
			return parseDropAlter(tableName, input);
		} else if (addIdx != -1 && dropIdx == -1) {
			String tableName = command.substring(0, addIdx);
			String input = command.substring(addIdx + 4);
			return parseAddAlter(tableName, input);
		}
		return new Query(false, "Wrong format: " + command);
	}

	private Query parseAddAlter(String tableName, String command) {
		if (invalidName(tableName)) {
			return new Query(false, "Invalid table name: " + tableName);
		}
		command = "(" + command + ")";
		String[][] tableData = getTableData(command.substring(command.indexOf("(")));
		if (tableData == null) {
			return new Query(false, "Invalid table schema");
		}
		return dbManager.alterQuery(tableName, reverse(tableData));
	}

	private Query parseDropAlter(String tableName, String command) {
		String[] columnNames = command.split(",");
		String[][] data = new String[2][columnNames.length];
		if (invalidName(tableName)) {
			return new Query(false, "Invalid table name: " + tableName);
		}
		for (int i = 0; i < columnNames.length; i++) {
			columnNames[i] = columnNames[i].trim();
			if (invalidName(columnNames[i])) {
				return new Query(false, "Wrong table data: " + command);
			}
			data[0][i] = columnNames[i];
			data[1][i] = null;
		}
		return dbManager.alterQuery(tableName, data);
	}

	private String[][] reverse(String[][] tableData) {
		String[][] reverseArr = new String[2][tableData.length];
		for (int i = 0; i < tableData.length; i++) {
			reverseArr[0][i] = new String(tableData[i][0]);
			reverseArr[1][i] = new String(tableData[i][1]);
		}
		return reverseArr;
	}
}
