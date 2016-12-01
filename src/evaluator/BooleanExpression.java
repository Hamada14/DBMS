package evaluator;

public class BooleanExpression {

  private String expression;

  public BooleanExpression(String expression) {
    this.expression = expression;
  }

  private boolean calculateBoolean(String firstVal, String secondVal, int type,
      char operator) {
    if (type == 0)
      return compareInt(firstVal, secondVal, operator);
    else {
      firstVal = firstVal.substring(1, firstVal.length() - 1);
      secondVal = secondVal.substring(1, secondVal.length() - 1);
      return compareString(firstVal, secondVal, operator);
    }
  }

  private int booleanToInt(boolean boolVal) {
    if (boolVal) {
      return 1;
    }
    return 0;
  }

  private int getType(String substring) {
    if (substring == null || substring.length() == 0) {
      return -1;
    }
    if (substring.matches("[0-9]+")) {
      return 0;
    } else if (substring.length() > 1) {
      char firstInd = substring.charAt(0);
      char lastInd = substring.charAt(substring.length() - 1);
      if (firstInd == lastInd && (firstInd == '\'' || firstInd == '\"')) {
        return 1;
      }
    }
    return -1;
  }

  private boolean isComparator(char curChar) {
    if (curChar == '<' || curChar == '>' || curChar == '=') {
      return true;
    }
    return false;
  }

  private int getCompPos(String expression) {
    int singleQ = 0, doubleQ = 0;
    int pos = -1;
    for (int idx = 0; idx < expression.length(); idx++) {
      char curChar = expression.charAt(idx);
      if (curChar == '\'') {
        singleQ++;
      } else if (curChar == '\"') {
        doubleQ++;
      } else if (isComparator(curChar) && (singleQ % 2 == 0)
          && (doubleQ % 2 == 0)) {
        if (pos == -1) {
          pos = idx;
        } else {
          return -1;
        }
      }
    }
    return pos;
  }

  private boolean compareString(String firstVal, String secondVal, char compChar) {
    int result = StringComp.getInstance().compare(firstVal, secondVal);
    if ((compChar == '>' && result > 0) || (compChar == '<' && result < 0)
        || (compChar == '=' && result == 0))
      return true;
    return false;
  }

  private boolean compareInt(String firstVal, String secondVal, char compChar) {
    int result = IntComp.getInstance().compare(Integer.parseInt(firstVal),
        Integer.parseInt(secondVal));
    if ((compChar == '>' && result > 0) || (compChar == '<' && result < 0)
        || (compChar == '=' && result == 0))
      return true;
    return false;
  }

  public int getValue() {
    expression = expression.trim();
    int compPos = getCompPos(expression);
    if (compPos == -1) {
      return -1;
    }
    String firstVal = expression.substring(0, compPos).trim();
    String secondVal = expression.substring(compPos + 1).trim();
    int type1 = getType(firstVal);
    int type2 = getType(secondVal);
    if (type1 != type2 || (type1 == -1) || (type2 == -1)) {
      return -1;
    }
    return booleanToInt(calculateBoolean(firstVal, secondVal, type1, expression.charAt(compPos)));
  }
}
