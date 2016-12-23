package query;

public class UpdateQuery extends CounterQuery {

	public UpdateQuery() throws InvalidOperation {
		super(true, "Condition is being checked");
	}
}
