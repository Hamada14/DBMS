package dBManagement;

import java.io.File;
import java.io.IOException;

import query.DBQuery;
import query.Query;

public class DataBase {

	private Table tableController;

	public DataBase() {
	}

	public Query addTable(String tableName, String[][] tableData, String dbPath, int parserType) {
		if (hasTable(tableName, dbPath)) {
			return new DBQuery(false, "This table name " + tableName + " already exists");
		}
		int len = tableData.length;
		String[] columnNames = new String[len];
		String[] colDataType = new String[len];
		for (int i = 0; i < len; i++) {
			columnNames[i] = tableData[i][0];
			colDataType[i] = tableData[i][1];
		}
		String tablePath = dbPath + File.separator + tableName;
		try {
			new File(tablePath).mkdir();
			new Table(tableName, tablePath, columnNames, colDataType, parserType);
			return new DBQuery(true, "Table " + tableName + " is created");
		} catch (Exception e) {
			return new Query(false, "Error upon file creation for table: " + tableName);
		}
	}

	public Query dropTable(String tableName, String dbPath) {
		if (!hasTable(tableName, dbPath)) {
			return new Query(false, "No such table named: " + tableName);
		}
		String fullPath = dbPath + File.separator + tableName;
		deleteDBDirectory(new File(fullPath));
		return new DBQuery(true, "Table " + tableName + " is dropped");
	}

	void deleteDBDirectory(File f) {
		try {
			if (f.isDirectory()) {
				for (File c : f.listFiles())
					deleteDBDirectory(c);
			}
			f.delete();
		} catch (Exception e)  {
			e.printStackTrace();
		}
	}

	private boolean fileExists(File f) {
		try {
			return f.exists() && f.getCanonicalPath().endsWith(f.getName());
		} catch (IOException e) {
			return false;
		}
	}

	public boolean hasTable(String tableName, String dbPath) {
		String fullDirectoryPath = dbPath + File.separator + tableName;
		File dbDir = new File(fullDirectoryPath);
		return fileExists(dbDir);
	}

	public Query selectQuery(String tableName, String[] colName, String condition, boolean distinct, String dbPath, int parserType) {
		if (!hasTable(tableName, dbPath)) {
			return new Query(false, "There is no such table named: " + tableName);
		}
		try {
		    tableController = new Table(tableName);
			String fullPath = dbPath + File.separator + tableName + File.separator + tableName;
			return tableController.selectFromTable(colName, condition, distinct, fullPath, parserType);
		} catch (Exception e) {
			return new Query(false, "Unexpected error!");
		}
	}

	public Query insertQuery(String tableName, String[] inputName, String[] varName, String dbPath, int parserType) {
	  if (!hasTable(tableName, dbPath)) {
			return new Query(false, "There is no such table named: " + tableName);
		}
		try {
		    tableController = new Table(tableName);
			String fullPath = dbPath + File.separator + tableName + File.separator + tableName;
			return tableController.insertIntoTable(inputName, varName, fullPath, parserType);
		} catch (Exception e) {
			return new Query(false, "Insertion failed");
		}
	}

	public Query insertQuery(String tableName, String[] varName, String dbPath, int parserType) {
		if(!hasTable(tableName, dbPath)) {
			return new Query(false, "There is no such table");
		}
		try {
		    tableController = new Table(tableName);
			String fullPath = dbPath + File.separator + tableName + File.separator + tableName;
			return tableController.insertIntoTable(varName, fullPath, parserType);
		} catch (Exception e) {
			return new Query(false, "Invalid Query, Insert Failed");
		}
	}

	public Query deleteQuery(String tableName, String condition, String dbPath, int parserType) {
		if (!hasTable(tableName, dbPath)) {
			return new Query(false, "There is no such table named: " + tableName);
		}
		try {
		    tableController = new Table(tableName);
			String fullPath = dbPath + File.separator + tableName + File.separator + tableName;
			return tableController.deleteFromTable(condition, fullPath, parserType);
		} catch (Exception e) {
			return new Query(false, "Invalid deletion");
		}
	}

	public Query updateQuery(String tableName, String[] input, String[] var, String condition, String dbPath, int parserType) {
		if (!hasTable(tableName, dbPath)) {
			return new Query(false, "There is no such table named: " + tableName);
		}
		try {
		    tableController = new Table(tableName);
			String fullPath = dbPath + File.separator + tableName + File.separator + tableName;
			return tableController.updateTable(input, var, condition, fullPath, parserType);
		} catch (Exception e) {
			return new Query(false, "invalid condition");
		}
	}

	public Query alterQuery(String tableName, String[][] columns, String dbPath, int parserType) {
		if (!hasTable(tableName, dbPath)) {
			return new Query(false, "There is no such table named: " + tableName);
		}
		try {
		    tableController = new Table(tableName);
			String fullPath = dbPath + File.separator + tableName + File.separator + tableName;
			return tableController.alterTable(columns, fullPath, parserType);
		} catch (Exception e) {
			return new Query(false, "Error in alter command");
		}
	}
}