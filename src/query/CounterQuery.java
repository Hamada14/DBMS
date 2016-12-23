package query;

public class CounterQuery extends Query {

	private int updateCounter;

	public CounterQuery(boolean correctSyntax, String errorMessage) {
		super(correctSyntax, errorMessage);
		updateCounter = 0;
	}

	public void incrementUpdateCounter() throws InvalidOperation {
		if (operationDone) {
			throw new InvalidOperation();
		}
		updateCounter++;
	}

	public int getUpdateCounter() {
		return updateCounter;
	}
}
