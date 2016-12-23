package JDBC.statement;

import java.sql.SQLException;

import query.CounterQuery;
import query.Query;
import dBManagement.Session;

public class Statement {

	public static int QUERY = 0, RESULT_SET = 1, INVALID = 2;

	private String statement;

	public Statement(String statement) {
		this.statement = fixWhiteSpace(statement);
	}

	public String getStatement() {
		return new String(this.statement);
	}

	private String fixWhiteSpace(String statement) {
		StringBuilder sb = new StringBuilder();
		boolean foundSpace = false;
		for (int idx = 0; idx < statement.length(); idx++) {
			if (foundSpace && Character.isWhitespace(statement.charAt(idx))) {
				continue;
			} else if (Character.isWhitespace(statement.charAt(idx))) {
				foundSpace = true;
				sb.append(" ");
			} else {
				foundSpace = false;
				sb.append(statement.charAt(idx));
			}
		}
		return sb.toString();
	}

	public String getFirstKeyWord(String statement) {
		return statement.substring(0, getFirstSpace(statement));
	}

	private int getFirstSpace(String statement) {
		int spacePosition = statement.indexOf(Character.SPACE_SEPARATOR);
		return spacePosition;
	}

	public int executeStatement(Session session) throws SQLException {
		Query query = session.execute(this);
		if (query.checkOperationFail()) {
			return DBMSStatement.EXECUTE_FAILED;
		} else if (query instanceof CounterQuery) {
			return ((CounterQuery) query).getUpdateCounter();
		}
		return DBMSStatement.SUCCESS_NO_INFO;
	}
}