package sqlParsers;

import dBManagement.DBManager;
import query.Query;

public class InsertParser extends Parser {

	public InsertParser(DBManager dbManager) {
		super(dbManager);
	}

	public Query insertQuery(String command) {
		if (calculateOpenBrackets(command) == 1) {
			return insertWithoutColNames(command);
		}
		if (invalidInsertFormat(command)) {
			return new Query(false, "Wrong format: " + command);
		}
		String tableName = command.substring(4, command.indexOf("("));
		tableName = tableName.toLowerCase().trim();
		String input, var;
		int idx = getValueIdx(command);
		int shiftVal = 0;
		if (command.charAt(idx + shiftVal) != ')')
			shiftVal--;
		input = command.substring(command.indexOf("("), idx + shiftVal + 1).trim();
		var = command.substring(idx + 7).trim();
		String[] inputArr = getColumnInput(input, 0);
		String[] varArr = getColumnInput(var, 1);
		if (invalidName(tableName) || inputArr == null || varArr == null || inputArr.length != varArr.length) {
			return new Query(false, "Table data is not written correctly");
		} else if (checkDuplicate(inputArr)) {
			return new Query(false, "Duplicate column names");
		}
		return dbManager.insertQuery(tableName, inputArr, varArr);
	}

	private Query insertWithoutColNames(String command) {
		String tableName = command.substring(4, command.indexOf("(")).trim();
		tableName = tableName.substring(0, tableName.length() - 6).trim();
		String[] rowData = command.substring(command.indexOf("(") + 1, command.indexOf(")")).split(",");
		for (int i = 0; i < rowData.length; i++) {
			rowData[i] = rowData[i].trim();
		}
		return dbManager.insertQuery(tableName, rowData);
	}

	private boolean invalidInsertFormat(String command) {
		if (!command.substring(0, 5).equalsIgnoreCase("into ") || command.indexOf("(") == command.lastIndexOf("(")
				|| command.indexOf(")") == command.lastIndexOf(")") || getValueIdx(command) == -1
				|| command.lastIndexOf(")") != command.length() - 1) {
			return true;
		}
		return false;
	}

	private boolean getBit(int msk, int bitIndx) {
		int bitVal = msk & (1 << bitIndx);
		return bitVal == 1;
	}

	private int getValueIdx(String command) {
		int idx = -1;
		for (int mask = 0; mask < (1 << 2); mask++) {
			String val = "values";
			if (getBit(mask, 0)) {
				val = ")" + val;
			} else {
				val = " " + val;
			}
			if (getBit(mask, 1)) {
				val = val + "(";
			} else {
				val = val + " ";
			}
			idx = Math.max(idx, command.toLowerCase().indexOf(val));
		}
		return idx;
	}

	private int calculateOpenBrackets(String command) {
		int open = 0;
		for (int i = 0; i < command.length(); i++) {
			if (command.charAt(i) == '(') {
				open++;
			}
		}
		return open;
	}
}