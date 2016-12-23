package JDBC.resultsetMetaData;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import dBManagement.QueryData;

public class ResultsetMetaData implements ResultSetMetaData {

	QueryData queryData;

	public ResultsetMetaData(QueryData queryData) {
		this.queryData = queryData;
	}

	@Override
	public int getColumnCount() throws SQLException {
		return queryData.columnCount();
	}

	@Override
	public String getColumnLabel(int column) throws SQLException {
		if (column > 0 && column <= queryData.columnCount())
			return queryData.getColumnName(column);
		return null;
	}

	@Override
	public String getColumnName(int column) throws SQLException {
		if (column > 0 && column <= queryData.columnCount())
			return queryData.getColumnName(column);
		return null;
	}

	@Override
	public String getTableName(int column) throws SQLException {
		if (column > 0 && column <= queryData.columnCount())
			return queryData.getTableName();
		return null;
	}

	@Override
	public int getColumnType(int column) throws SQLException {
		if (column > 0 && column <= queryData.columnCount())
			return queryData.getColumnType(column);
		return java.sql.Types.NULL;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isAutoIncrement(int column) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isCaseSensitive(int column) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isSearchable(int column) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isCurrency(int column) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int isNullable(int column) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isSigned(int column) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getColumnDisplaySize(int column) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getSchemaName(int column) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getPrecision(int column) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getScale(int column) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getCatalogName(int column) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getColumnTypeName(int column) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isReadOnly(int column) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isWritable(int column) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isDefinitelyWritable(int column) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getColumnClassName(int column) throws SQLException {
		throw new UnsupportedOperationException();
	}

}
