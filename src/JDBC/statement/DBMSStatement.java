package JDBC.statement;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;

import JDBC.ResultSet.Resultset;
import JDBC.connection.DBMSConnection;
import dBManagement.Session;
import query.CounterQuery;
import query.DBQuery;
import query.Query;
import query.SelectQuery;

public class DBMSStatement implements java.sql.Statement {

	private DBMSConnection connection;
	private StatementBatch statementBatch;
	private Session session;
	private ResultSet result;
	private int updateCounter = -1;
	private boolean closed;

	public DBMSStatement(DBMSConnection connection, Session session) {
		this.connection = connection;
		this.session = session;
		statementBatch = new StatementBatch(session);
		closed = false;
	}

	@Override
	public void addBatch(String arg0) throws SQLException {
		if (isClosed()) {
			throw new SQLException();
		}
		statementBatch.addStatement(new Statement(arg0));
	}

	@Override
	public void clearBatch() throws SQLException {
		if (isClosed()) {
			throw new SQLException();
		}
		statementBatch.clearBatch();
	}

	@Override
	public void close() throws SQLException {
		closed = true;
		this.connection = null;
		if (result != null) {
			result.close();
		}
	}

	@Override
	public boolean execute(String arg0) throws SQLException {
		if (isClosed()) {
			throw new SQLException();
		}
		Query query = session.execute(new Statement(arg0));
		if (query.checkOperationFail()) {
			throw new SQLException(query.getLogMessage());
		} else if (query instanceof DBQuery) {
			return false;
		}
		if (query instanceof CounterQuery) {
			if (query instanceof SelectQuery) {
				result = new Resultset(((SelectQuery) query).getQueryData(), this);
				return ((SelectQuery) query).getQueryData().getSize() > 0;
			} else {
				updateCounter = ((CounterQuery) query).getUpdateCounter();
				return updateCounter > 0;
			}
		}
		return false;
	}

	@Override
	public int[] executeBatch() throws SQLException {
		if (isClosed()) {
			throw new SQLException();
		}
		return statementBatch.executeBatch();
	}

	@Override
	public ResultSet executeQuery(String arg0) throws SQLException {
		if (isClosed()) {
			throw new SQLException("Statement closed");
		}
		Statement curStatement = new Statement(arg0);
		char firstLetter = curStatement.getStatement().toLowerCase().charAt(0);
		if (firstLetter != 's') {
			return null;
		}
		Query query = session.execute(curStatement);
		if (query.checkOperationFail()) {
			return null;
		}
		result = new Resultset(((SelectQuery) query).getQueryData(), this);
		return result;
	}

	@Override
	public int executeUpdate(String arg0) throws SQLException {
		if (isClosed()) {
			throw new SQLException("Statement closed");
		} else if (arg0.length() == 0) {
			return 0;
		}
		Statement curStatement = new Statement(arg0);
		char firstLetter = curStatement.getStatement().trim().toLowerCase().charAt(0);
		if (firstLetter != 'u' && firstLetter != 'd' && firstLetter != 'i') {
			return 0;
		}
		Query query = session.execute(curStatement);
		if (query.checkOperationFail()) {
			throw new SQLException(query.getLogMessage());
		} else if (query instanceof CounterQuery) {
			updateCounter = ((CounterQuery) query).getUpdateCounter();
			return updateCounter;
		}
		return 1;
	}

	@Override
	public ResultSet getResultSet() throws SQLException {
		if (result == null || isClosed()) {
			return null;
		}
		return result;
	}

	@Override
	public int getUpdateCount() throws SQLException {
		if (updateCounter == -1 || isClosed()) {
			throw new SQLException();
		}
		return updateCounter;
	}

	@Override
	public Connection getConnection() throws SQLException {
		return connection;
	}

	@Override
	public boolean isWrapperFor(Class<?> arg0) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T unwrap(Class<T> arg0) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void cancel() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clearWarnings() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void closeOnCompletion() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean execute(String arg0, int arg1) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean execute(String arg0, int[] arg1) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean execute(String arg0, String[] arg1) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int executeUpdate(String arg0, int arg1) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int executeUpdate(String arg0, int[] arg1) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int executeUpdate(String arg0, String[] arg1) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getFetchDirection() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getFetchSize() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public ResultSet getGeneratedKeys() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getMaxFieldSize() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getMaxRows() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean getMoreResults() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean getMoreResults(int arg0) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getQueryTimeout() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getResultSetConcurrency() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getResultSetHoldability() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getResultSetType() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isCloseOnCompletion() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isClosed() throws SQLException {
		return closed;
	}

	@Override
	public boolean isPoolable() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setCursorName(String arg0) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setEscapeProcessing(boolean arg0) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setFetchDirection(int arg0) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setFetchSize(int arg0) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setMaxFieldSize(int arg0) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setMaxRows(int arg0) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setPoolable(boolean arg0) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setQueryTimeout(int arg0) throws SQLException {
		throw new UnsupportedOperationException();
	}

}
