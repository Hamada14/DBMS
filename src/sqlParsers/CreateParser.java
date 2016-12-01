package sqlParsers;

public class CreateParser extends SQLParser {

  public CreateParser() {

  }

  private boolean validateName(String name) {
    if (invalidName(name)) {
      printError("Wrong or unidentified name.");
      return false;
    }
    return true;
  }

  public boolean useCommand(String dbName) {
    if (validateName(dbName)) {
      return dbManagerSingleton.useDataBase(dbName);
    }
    return false;
  }

  public boolean addDataBase(String dbName) {
    if (validateName(dbName)) {
      return dbManagerSingleton.addDataBase(dbName);
    }
    return false;
  }

  public boolean dropDataBase(String dbName) {
    if (validateName(dbName)) {
      return dbManagerSingleton.dropDataBase(dbName);
    }
    return false;
  }

  public boolean addTable(String command) {
    int firstIdx = command.indexOf("(");
    int secondIdx = command.lastIndexOf(")");
    if (firstIdx == -1 || secondIdx == -1 || secondIdx != command.length() - 1) {
      printError("Unidentified format.");
      return false;
    }
    String tableName = command.substring(0, firstIdx).trim();
    String[][] tableData = getTableData(command.substring(command.indexOf("(")));
    if (tableData == null) {
      printError("Wrong syntax declaring table schema.");
      return false;
    } else if (dupColumnName2D(tableData)) {
      printError("Duplicate column name");
      return false;
    } else if (validateName(tableName)) {

      return dbManagerSingleton.addTable(tableName, tableData);
    }
    return false;
  }

  public boolean dropTable(String tableName) {
    if (validateName(tableName)) {
      return dbManagerSingleton.dropTable(tableName);
    }
    return false;
  }
}