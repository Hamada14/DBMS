package sqlParsers;

public class SelectParser extends SQLParser {

  public boolean selectQuery(String command) {
    String tableName;
    String[] colName;
    int fromIdx = command.toLowerCase().indexOf(" from ");
    int whereIdx = command.toLowerCase().indexOf(" where ");
    if (fromIdx == -1 || (whereIdx != -1 && (whereIdx < fromIdx))) {
      printError("Wrong format.");
      return false;
    }
    tableName = getTableName(command.substring(fromIdx + 6));
    colName = getColNames(command.substring(0, fromIdx), tableName);
    command = command.substring(fromIdx + 6);
    if (colName == null || tableName == null) {
      printError("Make sure you type the names correctly "
          + "and the commands in the appropriate format");
      return false;
    } else if (duplicateColumnName(colName)){
      printError("Duplicate column name");
      return false;
    }
    String condition = getCondition(command);
    return dbManagerSingleton.selectQuery(tableName, colName, condition) != null;
  }
}