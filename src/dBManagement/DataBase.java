package dBManagement;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import org.apache.commons.io.FileUtils;

public class DataBase {


	private Table tableController;

	public DataBase() {
		tableController = new Table();
	}

	public boolean addTable(String tableName, String[][] tableData, String dbPath) {
		if (hasTable(tableName, dbPath)) {
			errorMessage("This table already exists");
			return false;
		}
		int len = tableData.length;
		String[] columnNames = new String[len];
		String[] colDataType = new String[len];
		for (int i = 0; i < len; i++) {
			columnNames[i] = tableData[i][0];
			colDataType[i] = tableData[i][1];
		}
		String tablePath = dbPath + File.separator + tableName;
		boolean commandDone = false;
		try {
			new File(tablePath).mkdir();
			new Table(tableName, tablePath, columnNames, colDataType);
			commandDone = true;
		} catch (Exception e) {
			System.out.println("Error upon create of the file");
			System.out.println("Make sure the directory isn't in use");
		}
		return commandDone;
	}

	public boolean dropTable(String tableName, String dbPath) {
		if (!hasTable(tableName, dbPath)) {
			errorMessage("No such table");
			return false;
		}
		String fullPath = dbPath + File.separator + tableName;
		return deleteDBDirectory(new File(fullPath));
	}

	private boolean deleteDBDirectory(File file) {
		try {
			FileUtils.deleteDirectory(file);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	private boolean fileExists(File f){
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
	
	public LinkedList<LinkedList<String>> selectQuery(String tableName, String[] colName, String condition, String dbPath) {
		LinkedList<LinkedList<String>> reqQuery = null;
		if (!hasTable(tableName, dbPath)) {
			errorMessage("There is no such table");
			return reqQuery;
		}
		try {
			String fullPath = dbPath + File.separator + tableName + File.separator + tableName;
			reqQuery = tableController.selectFromTable(colName, condition, fullPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reqQuery;
	}

	public boolean insertQuery(String tableName, String[] inputName, String[] varName, String dbPath) {
		if (!hasTable(tableName, dbPath)) {
			errorMessage("There is no such table");
			return false;
		}
		try {
			String fullPath = dbPath + File.separator + tableName + File.separator + tableName;
			return tableController.insertIntoTable(inputName, varName, fullPath);
		} catch (Exception e) {
			errorMessage("Invalid Query");
			return false;
		}
	}
	
	public boolean deleteQuery(String tableName, String condition, String dbPath) {
		if (!hasTable(tableName, dbPath)) {
			errorMessage("There is no such table");
			return false;
		}
		try {
			String fullPath = dbPath + File.separator + tableName + File.separator + tableName;
			return tableController.deleteFromTable(condition, fullPath);
		} catch (Exception e) {
			errorMessage("Invalid deletion");
			return false;
		}
	}

	public boolean updateQuery(String tableName, String[] input, String[] var, String condition, String dbPath) {
		if (!hasTable(tableName, dbPath)) {
			errorMessage("There is no such table");
			return false;
		}
		try {
			String fullPath = dbPath + File.separator + tableName + File.separator + tableName;
			return tableController.updateTable(input, var, condition, fullPath);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private void errorMessage(String error) {
		System.out.println(error);
	}
}