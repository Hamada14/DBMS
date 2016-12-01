package sqlParsers;

public class DeleteParser extends SQLParser {

  public boolean deleteQuery(String command) {
    command = " " + command;
    String tableName;
    int fromIdx = command.toLowerCase().indexOf(" from ");
    int whereIdx = command.toLowerCase().indexOf(" where ");
    if (fromIdx == -1 || (whereIdx != -1 && (whereIdx < fromIdx))) {
      printError("Wrong format.");
      return false;
    }
    tableName = getTableName(command.substring(fromIdx + 6));
    command = command.substring(fromIdx + 6);
    if (tableName == null) {
      printError("Make sure you type the names correctly "
          + "and the commands in the appropriate format");
      return false;
    }
    String condition = getCondition(command);
    return dbManagerSingleton.deleteQuery(tableName, condition);
  }
}