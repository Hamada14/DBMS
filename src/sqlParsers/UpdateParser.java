package sqlParsers;

import dBManagement.DBManager;
import query.Query;

public class UpdateParser extends Parser {

	public UpdateParser(DBManager dbManager) {
		super(dbManager);
	}

	public Query updateQuery(String command) {
		int setIdx = command.toLowerCase().indexOf(" set ");
		int whereIdx = command.toLowerCase().indexOf(" where ");
		if (invalidUpdateFormat(command)) {
			return new Query(false, "Wrong format: " + command);
		}
		String tableName = command.substring(0, command.indexOf(" ")).toLowerCase();
		String condition = getCondition(command);
		String setString;
		if (condition != null)
			setString = command.substring(setIdx + 5, whereIdx);
		else
			setString = command.substring(setIdx + 5);
		String[][] data = getUpdateData(setString);
		if (data != null && (!checkDuplicate2D(data))) {
			return dbManager.updateQuery(tableName, data, condition);
		} else if (data == null) {
			return new Query(false, "Wrong Format: " + command);
		} else {
			return new Query(false, "Duplicate column name");
		}
	}

	private String[][] getUpdateData(String data) {
		String[] inputArr = data.split(",");
		int len = inputArr.length;
		String[][] updateData = new String[len][2];
		for (int idx = 0; idx < len; idx++) {
			updateData[idx] = inputArr[idx].split("=");
			if (updateData[idx].length != 2)
				return null;
			updateData[idx][0] = updateData[idx][0].trim().toLowerCase();
			if (invalidName(updateData[idx][0]))
				return null;
			updateData[idx][1] = updateData[idx][1].trim();
		}
		return updateData;
	}

	private boolean invalidUpdateFormat(String command) {
		int firstIdx = command.indexOf(" ");
		int setIdx = command.toLowerCase().indexOf(" set ");
		int whereIdx = command.toLowerCase().indexOf(" where ");
		if (firstIdx == -1 || setIdx == -1 || (whereIdx != -1 && whereIdx <= setIdx)) {
			return true;
		}
		return false;
	}
}