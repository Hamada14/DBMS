package evaluator;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class InfixPostfixEvaluation {

  private String infix;
  private Deque<Character> stack = new ArrayDeque<Character>();
  private List<String> postfix = new ArrayList<String>();

  public InfixPostfixEvaluation(String expression) {
    infix = expression;
    convertExpression();
  }

  public List<String> getPostfixAsList() {
    return postfix;
  }
  
  private int getPrecedence(char op) {
    if (op == '!')
      return 3;
    else if (op == '&')
      return 2;
    else
      return 1;
  }
  
  private void convertExpression() {
    StringBuilder temp = new StringBuilder();
    for (int i = 0; i != infix.length(); ++i) {
      if (Character.isDigit(infix.charAt(i))) {
        temp.append(infix.charAt(i));
        postfix.add(temp.toString());
        temp.delete(0, temp.length());
      } else
        inputToStack(infix.charAt(i));
    }
    clearStack();
  }



  private void clearStack() {
    while (!stack.isEmpty()) {
      postfix.add(stack.removeLast().toString());
    }
  }
  
  private void inputToStack(char input) {
    if (stack.isEmpty() || input == '(')
      stack.addLast(input);
    else {
      if (input == ')') {
        while (!stack.getLast().equals('(')) {
          postfix.add(stack.removeLast().toString());
        }
        stack.removeLast();
      } else {
        if (stack.getLast().equals('('))
          stack.addLast(input);
        else {
          while (!stack.isEmpty() && !stack.getLast().equals('(')
              && getPrecedence(input) <= getPrecedence(stack.getLast())) {
            postfix.add(stack.removeLast().toString());
          }
          stack.addLast(input);
        }
      }
    }
  }
}