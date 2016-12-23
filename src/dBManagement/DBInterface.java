package dBManagement;

import query.Query;

public interface DBInterface {

	public Query dropDataBase(String dbName);

	public Query addDataBase(String dbName);

	public Query addTable(String tableName, String[][] tableData);

	public Query dropTable(String tableName);

	public Query selectQuery(String tableName, String[] colName, String condition, boolean distinct);

	public Query insertQuery(String tableName, String[] inputName, String[] varName);

	public Query insertQuery(String tableName, String[] varName);

	public Query deleteQuery(String tableName, String condition);

	public Query updateQuery(String tableName, String[][] data, String condition);

	public Query alterQuery(String tableName, String[][] columns);

}