package evaluator;

import java.util.Comparator;

public class IntComp implements Comparator<Integer> {

  private static IntComp intCompSingleton;

  private IntComp() {

  }

  public static IntComp getInstance() {
    if (intCompSingleton == null) {
      intCompSingleton = new IntComp();
    }
    return intCompSingleton;
  }
  
  @Override
  public int compare(Integer num1, Integer num2) {
    return num1 - num2;
  }
}
