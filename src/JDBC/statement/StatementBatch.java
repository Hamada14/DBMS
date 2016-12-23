package JDBC.statement;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;

import dBManagement.Session;

public class StatementBatch {
	private Queue<Statement> statements;
	private Session session;

	public StatementBatch(Session session) {
		statements = new LinkedList<Statement>();
		this.session = session;
	}

	public void addStatement(Statement statement) {
		statements.add(statement);
	}

	public void clearBatch() {
		while (!statements.isEmpty()) {
			statements.remove();
		}
	}

	public int[] executeBatch() throws SQLException {
		int[] operationResult = new int[statements.size()];
		int idx = 0;
		while (!statements.isEmpty()) {
			Statement curStatement = statements.peek();
			operationResult[idx] = curStatement.executeStatement(session);
			idx++;
			statements.remove();
		}
		return operationResult;
	}
}
