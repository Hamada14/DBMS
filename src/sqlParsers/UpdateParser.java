package sqlParsers;

public class UpdateParser extends SQLParser {

  private boolean invalidUpdateFormat(String command) {
    int firstIdx = command.indexOf(" ");
    int setIdx = command.toLowerCase().indexOf(" set ");
    int whereIdx = command.toLowerCase().indexOf(" where ");
    if (firstIdx == -1 || setIdx == -1
        || (whereIdx != -1 && whereIdx <= setIdx)) {
      return true;
    }
    return false;
  }

  private String[][] getUpdateData(String data) {
    String[] inputArr = data.split(",");
    int len = inputArr.length;
    String[][] updateData = new String[len][2];
    for (int idx = 0; idx < len; idx++) {
      updateData[idx] = inputArr[idx].split("=");
      if (updateData[idx].length != 2)
        return null;
      updateData[idx][0] = updateData[idx][0].trim();
      if (invalidName(updateData[idx][0]))
        return null;
      updateData[idx][1] = updateData[idx][1].trim();
    }
    return updateData;
  }

  public boolean updateQuery(String command) {
    int setIdx = command.toLowerCase().indexOf(" set ");
    int whereIdx = command.toLowerCase().indexOf(" where ");
    if (invalidUpdateFormat(command)) {
      printError("Wrong format.");
      return false;
    }
    String tableName = command.substring(0, command.indexOf(" "));
    String condition = getCondition(command);
    String setString;
    if (condition != null)
      setString = command.substring(setIdx + 5, whereIdx);
    else
      setString = command.substring(setIdx + 5);
    String[][] data = getUpdateData(setString);
    if (data != null && (!dupColumnName2D(data))) {
      return dbManagerSingleton.updateQuery(tableName, data, condition);
    } else if (data == null) {
      printError("Wrong Format");
      return false;
    } else {
      printError("Duplicate column name");
      return false;
    }
  }
}