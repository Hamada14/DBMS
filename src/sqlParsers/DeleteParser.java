package sqlParsers;

import dBManagement.DBManager;
import query.Query;

public class DeleteParser extends Parser {

	public DeleteParser(DBManager dbManager) {
		super(dbManager);
	}

	public Query deleteQuery(String command) {
		command = " " + command;
		String tableName;
		int fromIdx = command.toLowerCase().indexOf(" from ");
		int whereIdx = command.toLowerCase().indexOf(" where ");
		if (fromIdx == -1 || (whereIdx != -1 && (whereIdx < fromIdx))) {
			return new Query(false, "Wrong format");
		}
		tableName = getTableName(command.substring(fromIdx + 6));
		command = command.substring(fromIdx + 6);
		if (tableName == null) {
			return new Query(false, "Invalid table name: " + tableName);
		}
		String condition = getCondition(command);
		return dbManager.deleteQuery(tableName.toLowerCase(), condition);
	}
}