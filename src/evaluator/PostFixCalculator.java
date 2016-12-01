package evaluator;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class PostFixCalculator {
  private List<String> expression = new ArrayList<String>();
  private Deque<Integer> stack = new ArrayDeque<Integer>();

  public PostFixCalculator(List<String> postfix) {
    expression = postfix;
  }

  private int booleanToInt(boolean val) {
    if (val) {
      return 1;
    }
    return 0;
  }

  private boolean intToBoolean(int val){
    if(val == 1){
      return true;
    }
    return false;
  }
  
  public boolean getResult() {
    for (int i = 0; i < expression.size(); i++) {
      if (Character.isDigit(expression.get(i).charAt(0))) {
        stack.addLast(Integer.parseInt(expression.get(i)));
      } else {
        Integer tempResult = 0, temp = 0;
        switch (expression.get(i)) {
        case "!":
          temp = stack.removeLast();
          tempResult = booleanToInt((stack.removeLast() != 0) ^ (temp != 0));
          break;

        case "&":
          temp = stack.removeLast();
          tempResult = booleanToInt((stack.removeLast() != 0) && (temp != 0));
          break;

        case "|":
          temp = stack.removeLast();
          tempResult = booleanToInt((stack.removeLast() != 0) || (temp != 0));
          break;
        }
        stack.addLast(tempResult);
      }
    }
    return intToBoolean(new Integer(stack.removeLast()));
  }
}