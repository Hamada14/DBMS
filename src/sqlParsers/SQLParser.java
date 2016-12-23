package sqlParsers;

import dBManagement.DBManager;
import query.Query;

public class SQLParser extends Parser {

	private CreateParser createParser;
	private SelectParser selectParser;
	private UpdateParser updateParser;
	private InsertParser insertParser;
	private DeleteParser deleteParser;
	private AlterParser alterParser;
	private static final int USE = 0, ADD_DATA = 1, DROP_DATA = 2, ADD_TABLE = 3, DROP_TABLE = 4, SELECT = 5,
			INSERT = 6, UPDATE = 7, DELETE = 8, ALTER = 9;
	private static final String[] keyWords = new String[] { "use", "create database", "drop database", "create table",
			"drop table", "select", "insert", "update", "delete", "alter table" };

	public SQLParser(DBManager dbManager) {
		super(dbManager);
		createParser = new CreateParser(dbManager);
		selectParser = new SelectParser(dbManager);
		updateParser = new UpdateParser(dbManager);
		insertParser = new InsertParser(dbManager);
		deleteParser = new DeleteParser(dbManager);
		alterParser = new AlterParser(dbManager);
	}

	public Query parseSQL(String statement) {
		try {
			return parseStatement(statement.trim());
		} catch (Exception e) {
			return new Query(false, "Invalid statement");
		}
	}

	public Query executeAction(int type, String command) {
		switch (type) {
		case USE:
			return createParser.useCommand(command);
		case ADD_DATA:
			return createParser.addDataBase(command);
		case DROP_DATA:
			return createParser.dropDataBase(command);
		case ADD_TABLE:
			return createParser.addTable(command);
		case DROP_TABLE:
			return createParser.dropTable(command);
		case SELECT:
			return selectParser.selectQuery(command);
		case INSERT:
			return insertParser.insertQuery(command);
		case UPDATE:
			return updateParser.updateQuery(command);
		case DELETE:
			return deleteParser.deleteQuery(command);
		case ALTER:
			return alterParser.alterQuery(command);
		default:
			return new Query(false, "Unidentified command!");
		}
	}

	private boolean isTwoWorded(String command) {
		command = command.toLowerCase();
		if (command.equals("create") || command.equals("drop") || command.equals("alter")) {
			return true;
		}
		return false;
	}

	private String[] getKeyWords(String statement) {
		int firstEmptyIdx = statement.indexOf(" ");
		String[] keyWordPart = new String[2];
		keyWordPart[0] = statement.substring(0, firstEmptyIdx).trim();
		keyWordPart[1] = statement.substring(firstEmptyIdx + 1).trim();
		if (isTwoWorded(keyWordPart[0])) {
			int secondEmptyIdx = keyWordPart[1].indexOf(" ");
			if (secondEmptyIdx == -1) {
				return null;
			}
			keyWordPart[0] = keyWordPart[0] + " " + keyWordPart[1].substring(0, secondEmptyIdx);
			keyWordPart[1] = keyWordPart[1].substring(secondEmptyIdx);
		}
		return keyWordPart;
	}

	private Query parseStatement(String statement) {
		int firstEmptyIdx = statement.indexOf(" ");
		if (firstEmptyIdx == -1) {
			return new Query(false, "Wrong command format: " + statement);
		}
		String keyWordPart[] = getKeyWords(statement);
		if (keyWordPart == null) {
			return new Query(false, "Wrong command format: " + statement);
		}
		return identifyStatement(keyWordPart[0].trim(), keyWordPart[1].trim());
	}

	private Query identifyStatement(String keyWord, String command) {
		Query commandDone = null;
		boolean commandFound = false;
		keyWord = keyWord.trim();
		command = command.trim();
		for (int idx = 0; idx < keyWords.length; idx++) {
			if (keyWord.equalsIgnoreCase(keyWords[idx])) {
				commandDone = executeAction(idx, command);
				commandFound = true;
				break;
			}
		}
		if (!commandFound) {
			return new Query(false, "Unidentified command: " + command);
		}
		return commandDone;
	}
}