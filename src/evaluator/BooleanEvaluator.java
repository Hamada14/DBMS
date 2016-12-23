package evaluator;

public class BooleanEvaluator {

	private static BooleanEvaluator booleanEvaluator;

	private BooleanEquationParser bep;
	private InfixPostfixEvaluation infixPostfix;
	private PostFixCalculator postfixCal;

	private BooleanEvaluator() {
	}

	public static BooleanEvaluator getInstance() {
		if (booleanEvaluator == null)
			booleanEvaluator = new BooleanEvaluator();
		return booleanEvaluator;
	}

	public boolean isValid(String statement) throws RuntimeException {
		bep = new BooleanEquationParser(statement);
		if (!bep.parseBoolean()) {
			throw new RuntimeException();
		}
		String fixedStatement = bep.getFixedStatement();
		infixPostfix = new InfixPostfixEvaluation(fixedStatement);
		postfixCal = new PostFixCalculator(infixPostfix.getPostfixAsList());
		return postfixCal.getResult();
	}

}
