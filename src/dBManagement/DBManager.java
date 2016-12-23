package dBManagement;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import JDBC.UserInteface.DirectoryCreator;
import query.DBQuery;
import query.Query;

public class DBManager implements DBInterface {

	private static final Logger logger = LogManager.getLogger(DBManager.class);

	private DataBase dbController;
	private String selectedDB = null;
	private String dbmsPath;
	private int parserType;

	public DBManager(int parserType) {
		dbController = new DataBase();
		this.parserType = parserType;
		this.createDirectory("");
	}

	public DBManager(String dbmsPath, int parserType) {
		dbController = new DataBase();
		this.createDirectory(dbmsPath);
		this.parserType = parserType;
	}

	public void setDBMSPath(String dbmsPath) {
		this.dbmsPath = dbmsPath;
	}

	public boolean hasDatabase(String dbName) {
		if (dbName == null) {
			return false;
		}
		String fullDirectoryPath = getDBPath(dbName);
		File dbDir = new File(fullDirectoryPath);
		return fileExists(dbDir);
	}

	public Query useDataBase(String dbName) {
		if (!hasDatabase(dbName)) {
			return new DBQuery(false, "There is no such database named: " + dbName);
		}
		selectedDB = dbName;
		return new DBQuery(true, "Database " + dbName + " is selected");
	}

	@Override
	public Query addDataBase(String dbName) {
		if (hasDatabase(dbName)) {
			return new Query(false, "This database already exists: " + dbName);
		}
		String dbPath = getDBPath(dbName);
		new File(dbPath).mkdir();
		selectedDB = dbName;
		DBQuery query = new DBQuery(true, "Database " + dbName + " is Created");
		query.setOperationDone();
		return query;
	}

	@Override
	public Query dropDataBase(String dbName) {
		if (!hasDatabase(dbName)) {
			return new DBQuery(false, "There is no such database named: " + dbName);
		}
		String dbPath = getDBPath(dbName);
		deleteDBDirectory(new File(dbPath));
		if (selectedDB != null && selectedDB.equals(dbName)) {
			logger.info("No database is selected");
		}
		selectedDB = null;
		return new DBQuery(true, "Database " + dbName + " is dropped");
	}

	public boolean hasTable(String tableName) {
		checkSelectedDataBase();
		if (selectedDB == null) {
			return false;
		}
		return dbController.hasTable(tableName, getDBPath(selectedDB));
	}

	@Override
	public Query addTable(String tableName, String[][] tableData) {
		checkSelectedDataBase();
		if (selectedDB == null) {
			return new Query(false, "No selected database to create table: " + tableName + " in");
		}
		return dbController.addTable(tableName, tableData, getDBPath(selectedDB), parserType);
	}

	@Override
	public Query dropTable(String tableName) {
		checkSelectedDataBase();
		if (selectedDB == null) {
			return new Query(false, "There is no selected database!");
		}
		return dbController.dropTable(tableName, getDBPath(selectedDB));
	}

	@Override
	public Query selectQuery(String tableName, String[] colName, String condition, boolean distinct) {
		checkSelectedDataBase();
		if (selectedDB == null) {
			return new Query(false, "There is no selected database");
		}
		colName = avoidRep(colName);
		return dbController.selectQuery(tableName, colName, condition, distinct, getDBPath(selectedDB), parserType);
	}

	@Override
	public Query insertQuery(String tableName, String[] inputName, String[] varName) {
		checkSelectedDataBase();
		if (selectedDB == null) {
			return new Query(false, "There is no selected database");
		}
		return dbController.insertQuery(tableName, inputName, varName, getDBPath(selectedDB), parserType);
	}

	@Override
	public Query insertQuery(String tableName, String[] varName) {
		checkSelectedDataBase();
		if (selectedDB == null) {
			return new Query(false, "There is no selected database");
		}
		return dbController.insertQuery(tableName, varName, getDBPath(selectedDB), parserType);
	}

	@Override
	public Query updateQuery(String tableName, String[][] data, String condition) {
		checkSelectedDataBase();
		if (selectedDB == null) {
			return new Query(false, "There is no selected dataBase!");
		}
		return dbController.updateQuery(tableName, getArryDimension(data, 0), getArryDimension(data, 1), condition, getDBPath(selectedDB),
				parserType);
	}

	@Override
	public Query deleteQuery(String tableName, String condition) {
		checkSelectedDataBase();
		if (selectedDB == null) {
			return new Query(false, "There is no selected database");
		}
		return dbController.deleteQuery(tableName, condition, getDBPath(selectedDB), parserType);
	}

	@Override
	public Query alterQuery(String tableName, String[][] columns) {
		checkSelectedDataBase();
		if (selectedDB == null) {
			return new Query(false, "There is no selected database!");
		}
		return dbController.alterQuery(tableName, columns, getDBPath(selectedDB), parserType);
	}

	private boolean fileExists(File f) {
		try {
			return f.exists() && f.getCanonicalPath().endsWith(f.getName());
		} catch (IOException e) {
			return false;
		}
	}

	private void createDirectory(String dbmsPath) {
		DirectoryCreator dirCreator = DirectoryCreator.getInstance();
		String dirPath = dirCreator.manageCreation(dbmsPath);
		this.setDBMSPath(dirPath);
	}

	private void checkSelectedDataBase() {
		if (!hasDatabase(selectedDB)) {
			selectedDB = null;
		}
	}

	void deleteDBDirectory(File f) {
		try {
			if (f.isDirectory()) {
				for (File c : f.listFiles())
					deleteDBDirectory(c);
			}
			f.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String[] avoidRep(String[] colNames) {
		ArrayList<String> withOutRep = new ArrayList<String>();
		for (int i = 0; i < colNames.length; i++) {
			boolean isHere = false;
			for (int j = 0; j < withOutRep.size(); j++) {
				if (withOutRep.get(j).equals(colNames[i])) {
					isHere = true;
				}
			}
			if (!isHere) {
				withOutRep.add(colNames[i]);
			}
		}
		int sz = withOutRep.size();
		String[] ret = new String[sz];
		for (int i = 0; i < sz; i++) {
			ret[i] = withOutRep.get(i);
		}
		return ret;
	}

	private String getDBPath(String dbName) {
		return dbmsPath + File.separator + dbName;
	}

	private String[] getArryDimension(String[][] arr, int idx) {
		String[] resultArr = new String[arr.length];
		for (int i = 0; i < arr.length; i++) {
			resultArr[i] = new String(arr[i][idx]);
		}
		return resultArr;
	}
}