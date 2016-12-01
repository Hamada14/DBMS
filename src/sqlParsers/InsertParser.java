package sqlParsers;

public class InsertParser extends SQLParser {

  public InsertParser() {

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

  private boolean invalidInsertFormat(String command) {
    if (!command.substring(0, 5).equalsIgnoreCase("into ")
        || command.indexOf("(") == command.lastIndexOf("(")
        || command.indexOf(")") == command.lastIndexOf(")")
        || getValueIdx(command) == -1
        || command.lastIndexOf(")") != command.length() - 1) {
      return true;
    }
    return false;
  }

  private String[] getColumnInput(String input, int type) {
    if (input.charAt(0) != '(' || input.charAt(input.length() - 1) != ')') {
      return null;
    }
    input = input.substring(1, input.length() - 1);
    String[] inputArr = input.split(",");
    int len = inputArr.length;
    for (int i = 0; i < len; i++) {
      inputArr[i] = inputArr[i].trim();
      if (hasSpace(inputArr[i]) && type == 0)
        return null;
    }
    return inputArr;
  }

  public boolean insertQuery(String command) {
    if (invalidInsertFormat(command)) {
      printError("Wrong format.");
      return false;
    }
    String tableName = command.substring(4, command.indexOf("("));
    tableName = tableName.trim();
    String input, var;
    int idx = getValueIdx(command);
    int shiftVal = 0;
    if (command.charAt(idx + shiftVal) != ')')
      shiftVal--;
    input = command.substring(command.indexOf("("), idx + shiftVal + 1).trim();
    var = command.substring(idx + 7).trim();
    String[] inputArr = getColumnInput(input, 0);
    String[] varArr = getColumnInput(var, 1);
    if (invalidName(tableName) || inputArr == null || varArr == null
        || inputArr.length != varArr.length) {
      printError("Make sure the table data is written correctly.");
      return false;
    } else if (duplicateColumnName(inputArr)) {
      printError("Duplicate column names.");
      return false;
    }
    return dbManagerSingleton.insertQuery(tableName, inputArr, varArr);
  }
}