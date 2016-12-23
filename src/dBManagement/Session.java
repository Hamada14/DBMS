package dBManagement;

import query.Query;
import JDBC.statement.Statement;
import sqlParsers.SQLParser;

public class Session {

	private DBManager dbManager;
	private SQLParser sqlParser;

	public Session(String directoryPath, int protocolType) {
		dbManager = new DBManager(directoryPath, protocolType);
		sqlParser = new SQLParser(dbManager);
	}

	public Query execute(Statement statement) {
		return sqlParser.parseSQL(statement.getStatement());
	}
}
