package sqlParsers;

import dBManagement.DBManager;
import query.Query;

public class SelectParser extends Parser {

	public SelectParser(DBManager dbManager) {
		super(dbManager);
	}

	public Query selectQuery(String command) {
		String tableName;
		String[] colName;
		int distinctValue = getDistinct(command);
		if (wrongeFormat(command)) {
			return new Query(false, "Wrong command format: " + command);
		}
		if (distinctValue != -1) {
			command = command.substring(8);
		}
		tableName = getSelectTableName(command).toLowerCase();
		colName = getColumnNames(command, tableName);
		if (colName == null || tableName == null) {
			return new Query(false, "Invalid command: " + command);
		} else if (checkDuplicate(colName)) {
			return new Query(false, "Duplicate column name");
		}
		String condition = getCondition(command);
		return dbManager.selectQuery(tableName, colName, condition, distinctValue != -1);
	}

	public String getSelectTableName(String command) {
		int fromIdx = command.toLowerCase().indexOf(" from ");
		int whereIdx = command.toLowerCase().indexOf(" where ");
		if (whereIdx == -1) {
			whereIdx = command.length();
		}
		return command.substring(fromIdx + 5, whereIdx).trim();
	}

	private int getDistinct(String command) {
		return command.toLowerCase().indexOf("distinct ");
	}

	private boolean wrongeFormat(String command) {
		int distinctIdx = command.toLowerCase().indexOf("distinct ");
		int fromIdx = command.toLowerCase().indexOf(" from ");
		int whereIdx = command.toLowerCase().indexOf(" where ");
		if (fromIdx == -1 || (whereIdx != -1 && (whereIdx < fromIdx))) {
			return true;
		}
		if (distinctIdx != -1 && distinctIdx != 0) {
			return true;
		}
		return false;
	}
}