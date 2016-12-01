package evaluator;

public class BooleanEvaluatorSingleton {

  private static BooleanEvaluatorSingleton booleanEvaluatorSingleton;
  private BooleanEquationParser bep;
  private InfixPostfixEvaluation infixPostfix;
  private PostFixCalculator postfixCal;

  private BooleanEvaluatorSingleton() {
  }

  public static BooleanEvaluatorSingleton getInstance() {
    if (booleanEvaluatorSingleton == null)
      booleanEvaluatorSingleton = new BooleanEvaluatorSingleton();
    return booleanEvaluatorSingleton;
  }

  public boolean isValid(String statement) throws RuntimeException{
    bep = new BooleanEquationParser(statement);
    if(!bep.parseBoolean()){
      throw new RuntimeException();
    }
    String fixedStatement = bep.getFixedStatement();
    infixPostfix = new InfixPostfixEvaluation(fixedStatement);
    postfixCal = new PostFixCalculator(infixPostfix.getPostfixAsList());
    return postfixCal.getResult();
  }
  

}
