package query;

import java.util.LinkedList;

import dBManagement.Entry;
import dBManagement.QueryData;

public class SelectQuery extends CounterQuery {

	private QueryData operationData;

	public SelectQuery(String tableName, LinkedList<String> colNames, LinkedList<String> colTypes)
			throws InvalidOperation {
		super(true, "Select query is executed");
		this.operationData = new QueryData(tableName, colNames, colTypes);
	}

	public SelectQuery() {
		super(false, null);
	}

	public QueryData getQueryData() {
		return operationData;
	}

	public void addEntry(Entry entry) {
		operationData.addEntry(entry);
	}
}
