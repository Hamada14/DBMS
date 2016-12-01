package evaluator;

import java.util.Comparator;

public class StringComp implements Comparator<String> {

  private static StringComp stringCompSingleton;

  private StringComp() {

  }

  public static StringComp getInstance() {
    if (stringCompSingleton == null) {
      stringCompSingleton = new StringComp();
    }
    return stringCompSingleton;
  }

  @Override
  public int compare(String o1, String o2) {
    return (o1).compareTo(o2);
  }
}
