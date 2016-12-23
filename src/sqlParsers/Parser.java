package sqlParsers;

import dBManagement.DBManager;
import data.DataValidator;

public abstract class Parser {

	private static final String[] forbiddenName = { "create", "select", "insert", "delete", "update", "use", "from",
			"drop", "where", "and", "not", "or", "database", "table", "alter", "distinct", "varchar", "int", "date",
			"float" };

	protected DBManager dbManager;

	public Parser(DBManager dbManager) {
		this.dbManager = dbManager;
	}

	protected String fixWhiteSpace(String command) {
		StringBuilder sb = new StringBuilder();
		for (int idx = 0; idx < command.length(); idx++) {
			if (Character.isWhitespace(command.charAt(idx))) {
				sb.append(" ");
			} else {
				sb.append(command.charAt(idx));
			}
		}
		return command;
	}

	private boolean isForbidden(String name) {
		for (int i = 0; i < forbiddenName.length; i++) {
			if (name.equalsIgnoreCase(forbiddenName[i])) {
				return true;
			}
		}
		return false;
	}

	protected boolean invalidName(String name) {
		if (name == null || name.length() == 0 || hasSpace(name) || hasSymbol(name) || isNumeric(name.charAt(0))
				|| isForbidden(name)) {
			return true;
		}
		return false;
	}

	protected boolean hasSymbol(String command) {
		for (int idx = 0; idx < command.length(); idx++) {
			Character ch = command.charAt(idx);
			if ((!Character.isAlphabetic(ch) && !isNumeric(ch)) && ch != '_')
				return true;
		}
		return false;
	}

	protected boolean hasSpace(String command) {
		for (int idx = 0; idx < command.length(); idx++) {
			if (Character.isWhitespace(command.charAt(idx)))
				return true;
		}
		return false;
	}

	protected boolean isNumeric(char digit) {
		if (digit >= '0' && digit <= '9') {
			return true;
		}
		return false;
	}

	protected boolean checkDuplicate(String[] data) {
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < i; j++) {
				if (data[i].equals(data[j])) {
					return true;
				}
			}
		}
		return false;
	}

	protected boolean checkDuplicate2D(String[][] data) {
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < i; j++) {
				if (data[i][0].equals(data[j][0])) {
					return true;
				}
			}
		}
		return false;
	}

	protected int countOccurrence(String statement, char reqChar) {
		int charCount = 0;
		for (int idx = 0; idx < statement.length(); idx++) {
			if (statement.charAt(idx) == reqChar) {
				charCount++;
			}
		}
		return charCount;
	}

	protected void printError(String errorMsg) {
		System.out.println("Error!\n" + errorMsg);
	}

	protected String getTableName(String command) {
		String tableName;
		int whereIdx = command.toLowerCase().indexOf(" where ");
		if (whereIdx == -1)
			tableName = new String(command).trim();
		else
			tableName = new String(command.substring(0, command.toLowerCase().indexOf(" where"))).trim();
		tableName = tableName.trim();
		if (invalidName(tableName)) {
			return null;
		}
		return tableName;
	}

	protected String[][] getTableData(String command) {
		command = command.substring(1, command.length() - 1);
		String[] input = command.split(",");
		String[][] data = new String[input.length][];
		for (int idx = 0; idx < input.length; idx++) {
			String curStr = input[idx].trim();
			int emptyIdx = curStr.indexOf(" ");
			data[idx] = new String[2];
			data[idx][0] = curStr.substring(0, emptyIdx).trim();
			String type = curStr.substring(emptyIdx + 1).trim();
			if (invalidName(data[idx][0]))
				return null;
			data[idx][1] = DataValidator.getInstance().getDataType(type);
			if (data[idx][1] == null)
				return null;
		}
		return data;
	}

	protected String[] getColumnNames(String command, String tableName) {
		int fromIdx = command.toLowerCase().indexOf(" from ");
		command = command.substring(0, fromIdx);
		String[] colNames = null;
		colNames = command.split(",");
		boolean corrupted = false;
		boolean astFound = false;
		for (int idx = 0; idx < colNames.length; idx++) {
			colNames[idx] = colNames[idx].trim();
			if (colNames[idx].equals("*")) {
				astFound = true;
				continue;
			}
			corrupted = corrupted || invalidName(colNames[idx]);
		}
		if (corrupted || (astFound && colNames.length != 1))
			return null;
		return colNames;
	}

	protected String[] getColumnInput(String input, int type) {
		if (input.charAt(0) != '(' || input.charAt(input.length() - 1) != ')') {
			return null;
		}
		input = input.substring(1, input.length() - 1);
		String[] inputArr = input.split(",");
		int len = inputArr.length;
		for (int i = 0; i < len; i++) {
			inputArr[i] = inputArr[i].trim();
			if (type == 0)
				inputArr[i] = inputArr[i].toLowerCase();
			if (hasSpace(inputArr[i]) && type == 0)
				return null;
		}
		return inputArr;
	}

	protected String getCondition(String command) {
		int whereIdx = command.toLowerCase().indexOf(" where ");
		if (whereIdx == -1) {
			return null;
		}
		return command.substring(whereIdx + 7);
	}

}