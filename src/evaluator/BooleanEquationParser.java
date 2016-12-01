package evaluator;

public class BooleanEquationParser {

  private String statement;
  private String fixedStatement;

  public BooleanEquationParser(String statement) {
    this.statement = statement;
  }

  public String getFixedStatement() {
    return fixedStatement;
  }

  private void setFixedStatement(String st) {
    fixedStatement = removeSpace(st);
  }

  private boolean isOperator(char c) {
    if (c == '&' || c == '|' || c == '!') {
      return true;
    }
    return false;
  }

  private String removeSpace(String st) {
    StringBuilder sb = new StringBuilder();
    int singleQ = 0, doubleQ = 0;
    for (int idx = 0; idx < st.length(); idx++) {
      if (!Character.isWhitespace(st.charAt(idx))) {
        sb.append(st.charAt(idx));
      } else if ((singleQ % 2 != 0) || (doubleQ % 2 != 0)) {
        sb.append(st.charAt(idx));
      }
      if (st.charAt(idx) == '\'') {
        singleQ++;
      } else if (st.charAt(idx) == '\"') {
        doubleQ++;
      }
    }
    return sb.toString();
  }

  public boolean parseBoolean() {
    StringBuilder finalStatement = new StringBuilder();
    StringBuilder expression = new StringBuilder();
    int singleCount = 0, doubleCount = 0;
    for (int idx = 0; idx < statement.length(); idx++) {
      char curChar = statement.charAt(idx);
      if (isOperator(curChar) && (singleCount % 2 == 0)
          && (doubleCount % 2) == 0) {
        if (curChar == '!') {
          finalStatement.append(1);
        } else {
          int curVal = (new BooleanExpression(expression.toString()))
              .getValue();
          if (curVal == -1)
            return false;
          finalStatement.append(curVal);
        }
        finalStatement.append(curChar);
        expression = new StringBuilder();
      } else {
        expression.append(curChar);
        if (curChar == '\'') {
          singleCount++;
        } else if (curChar == '\"') {
          doubleCount++;
        }
      }
    }
    if (expression.length() != 0) {
      int curVal = (new BooleanExpression(expression.toString())).getValue();
      if (curVal == -1)
        return false;
      finalStatement.append(curVal);
    }
    setFixedStatement(finalStatement.toString());
    return true;
  }
}
