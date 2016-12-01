package sqlParsers;


public class SQLParserSingleton extends SQLParser {

  private static SQLParserSingleton sqlParserSingleton;
  private CreateParser createParser;
  private SelectParser selectParser;
  private UpdateParser updateParser;
  private InsertParser insertParser;
  private DeleteParser deleteParser;
  private static final int USE = 0, ADD_DATA = 1, DROP_DATA = 2, ADD_TABLE = 3,
      DROP_TABLE = 4, SELECT = 5, INSERT = 6, UPDATE = 7, DELETE = 8;
  private static final String[] keyWords = new String[] { "use",
      "create database", "drop database", "create table", "drop table",
      "select", "insert", "update", "delete" };

  private SQLParserSingleton() {
    createParser = new CreateParser();
    selectParser = new SelectParser();
    updateParser = new UpdateParser();
    insertParser = new InsertParser();
    deleteParser = new DeleteParser();
  }

  public static SQLParserSingleton getInstance() {
    if (sqlParserSingleton == null)
      sqlParserSingleton = new SQLParserSingleton();
    return sqlParserSingleton;
  }

  private String[] getKeyWords(String statement){
    int firstEmptyIdx = statement.indexOf(" ");
    String[] keyWordPart = new String[2];
    keyWordPart[0] = statement.substring(0, firstEmptyIdx).trim();
    keyWordPart[1] = statement.substring(firstEmptyIdx + 1).trim();
    if (keyWordPart[0].equalsIgnoreCase("create") || keyWordPart[0].equalsIgnoreCase("drop")) {
      int secondEmptyIdx = keyWordPart[1].indexOf(" ");
      if (secondEmptyIdx == -1) {
        return null;
      }
      keyWordPart[0] = keyWordPart[0] + " " + keyWordPart[1].substring(0, secondEmptyIdx);
      keyWordPart[1] = keyWordPart[1].substring(secondEmptyIdx);
    }
    return keyWordPart;
  }
  
  public boolean parseSQL(String statement) {
    statement = fixWhiteSpace(statement).trim();
    try{
    int semiColonCount = countOccurrence(statement, ';');
    if(semiColonCount == 0){
      printError("Missing Semicolon.");
      return false;
    }else if(semiColonCount > 1){
      printError("Semicolon maybe used inappropriately");
      return false;
    }else if(statement.charAt(statement.length() - 1) != ';'){
      printError("Missing Semicolon at the end of the command.");
      return false;
    }
    return parseStatement(statement.substring(0, statement.length() - 1).trim());
    }catch(Exception e){
      System.out.println("Invalid statement");
      return false;
    }
  }

  private boolean parseStatement(String statement){
    int firstEmptyIdx = statement.indexOf(" ");
    if (firstEmptyIdx == -1) {
      printError("Wrong command format.");
      return false;
    }
    String keyWordPart[] = getKeyWords(statement);
    if(keyWordPart == null){
      printError("Wrong command format.");
      return false;
    }
    return identifyStatement(keyWordPart[0].trim(), keyWordPart[1].trim());
  }
  
  private boolean identifyStatement(String keyWord, String command) {
    boolean commandDone = false, commandFound = false;
    keyWord = keyWord.trim();
    command = command.trim();
    for (int idx = 0; idx < keyWords.length; idx++) {
      if (keyWord.equalsIgnoreCase(keyWords[idx])) {
        commandDone = executeAction(idx, command);
        commandFound = true;
        break;
      }
    }
    if(!commandFound){
      printError("Unidentified command.");
      return false;
    }
    return commandDone;
  }

  public boolean executeAction(int type, String command) {
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
    default:
      printError("Unidentified command");
      return false;
    }
  }  
}