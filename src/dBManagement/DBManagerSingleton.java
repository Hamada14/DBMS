package dBManagement;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import org.apache.commons.io.FileUtils;

import userInterface.TableShapeSingleton;

public class DBManagerSingleton implements DBInterface{

  private static DBManagerSingleton dbManagerSingleton;
  private DataBase dbController;
  private String selectedDB = null;
  private String dbmsPath;

  private DBManagerSingleton() {
    dbController = new DataBase();
  }

  public static DBManagerSingleton getInstance() {
    if (dbManagerSingleton == null) {
      dbManagerSingleton = new DBManagerSingleton();
    }
    return dbManagerSingleton;
  }

  public void setDBMSPath(String dbmsPath) {
    this.dbmsPath = dbmsPath;
  }

  public boolean useDataBase(String dbName) {
    if (!hasDatabase(dbName)) {
      errorMessage("There is no such database");
      return false;
    }
    selectedDB = dbName;
    return true;
  }

  private boolean fileExists(File f){
    try {
      return f.exists() && f.getCanonicalPath().endsWith(f.getName());
    } catch (IOException e) {
      return false;
    }
  }
  
  public boolean hasDatabase(String dbName) {
    if (dbName == null) {
      return false;
    }
    String fullDirectoryPath = getDBPath(dbName);
    File dbDir = new File(fullDirectoryPath);
    return fileExists(dbDir);
  }

  private String getDBPath(String dbName) {
    return dbmsPath + File.separator + dbName;
  }

  private void checkSelectedDataBase() {
    if (!hasDatabase(selectedDB)) {
      selectedDB = null;
    }
  }

  private void errorMessage(String err) {
    System.out.print("DBMangError::");
    System.out.println(err);
  }
  public boolean dropDataBase(String dbName) {
    if (!hasDatabase(dbName)) {
      errorMessage("There is no such database");
      return false;
    }
    String dbPath = getDBPath(dbName);
    deleteDBDirectory(new File(dbPath));
    if (selectedDB != null && selectedDB.equals(dbName))
      selectedDB = null;
    return true;
  }

  private void deleteDBDirectory(File file) {
    try {
      FileUtils.deleteDirectory(file);
    } catch (IOException e) {
      System.out.println("Please!\nmake sure you aren't using the database files.");
    }
  }

  public boolean addDataBase(String dbName) {
    if (hasDatabase(dbName)) {
      errorMessage("This dataBase already exists");
      return false;
    }
    new File(getDBPath(dbName)).mkdir();
    selectedDB = dbName;
    return true;
  }

  public boolean addTable(String tableName, String[][] tableData) {
    checkSelectedDataBase();
    if (selectedDB == null) {
      errorMessage("No selected data base");
      return false;
    }
    return dbController.addTable(tableName, tableData, getDBPath(selectedDB));
  }

  public boolean dropTable(String tableName) {
    checkSelectedDataBase();
    if (selectedDB == null) {
      errorMessage("There is no selected database");
      return false;
    }
    return dbController.dropTable(tableName, getDBPath(selectedDB));
  }

  public boolean hasTable(String tableName) {
    checkSelectedDataBase();
    if (selectedDB == null) {
      errorMessage("There is no selected database");
      return false;
    }
    return dbController.hasTable(tableName, getDBPath(selectedDB));
  }

  public LinkedList<LinkedList<String>> selectQuery(String tableName, String[] colName,
      String condition) {
    checkSelectedDataBase();
    LinkedList<LinkedList<String>> reqQuery = null;
    if (selectedDB == null) {
      errorMessage("There is no selected database");
      return null;
    }
    colName = avoidRep(colName);
    reqQuery = dbController.selectQuery(tableName, colName, condition,
        getDBPath(selectedDB));
    if (reqQuery == null) {
      return null;
    }
    TableShapeSingleton tablePrint = TableShapeSingleton.getInstance();
    tablePrint.setTable(reqQuery);
    tablePrint.print();
    return reqQuery;
  }
  
  private String[] avoidRep(String[] colNames) {
	  ArrayList<String> withOutRep = new ArrayList<String>();
	  for(int i = 0 ; i < colNames.length ; i++) {
		  boolean isHere = false;
		  for(int j = 0 ; j < withOutRep.size() ; j++) {
			  if(withOutRep.get(j).equals(colNames[i])) {
				  isHere = true;
			  }
		  }
		  if(!isHere) {
			  withOutRep.add(colNames[i]);
		  }
	  }
	  int sz = withOutRep.size();
	  String[] ret = new String[sz];
	  for(int i = 0 ; i < sz ; i++) {
		  ret[i] = withOutRep.get(i);
	  }
	  return ret;
  }

  public boolean insertQuery(String tableName, String[] inputName, String[] varName) {
    checkSelectedDataBase();
    if (selectedDB == null) {
      errorMessage("There is no selected database");
      return false;
    }
    return dbController.insertQuery(tableName, inputName, varName,
        getDBPath(selectedDB));
  }

  public boolean deleteQuery(String tableName, String condition) {
    checkSelectedDataBase();
    if (selectedDB == null) {
      errorMessage("There is no selected database");
      return false;
    }
    return dbController.deleteQuery(tableName, condition, getDBPath(selectedDB));
  }

  private String[] getArr(String[][] arr, int idx) {
    String[] resultArr = new String[arr.length];
    for (int i = 0; i < arr.length; i++) {
      resultArr[i] = new String(arr[i][idx]);
    }
    return resultArr;
  }

  public boolean updateQuery(String tableName, String[][] data, String condition) {
    checkSelectedDataBase();
    if (selectedDB == null) {
      errorMessage("There is no selected dataBase");
      return false;
    }
    return dbController.updateQuery(tableName, getArr(data, 0), getArr(data, 1),
        condition, getDBPath(selectedDB));
  }
}