package query;

public class DBQuery extends Query {

	public DBQuery(boolean operationSuccess, String errorMessage) {
		super(true, errorMessage);
		if (operationSuccess) {
			setOperationDone();
		}
	}
}
