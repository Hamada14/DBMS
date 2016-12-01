package testing;

public final class DoCommand {

	private static final String ADDQUERY = "insert into ", ADDTABLE = "CREATE TABLE ", DROPTABLE = "drop table ";
	private static final String CREATDB = "create database ", END = ";", DROP = "drop database ", USE = "use ";
	private DoCommand() {
		
	}
	
	public static String makeDropTableCommand(String tableName) {
		return DROPTABLE + tableName + ";";
	}
	
	public static String makeCreationCommand(String dbName) {
		return CREATDB + dbName + END;
	}
	
	public static String makeSelectCommand(String dbName) {
		return USE + dbName + END;
	}
	
	public static String makeDropCommand(String dbName) {
		return DROP + dbName + END;
	}
	
	public static String makeAddQueryCommand(String tableName, String[] currCols, String[] colsValues) {
		StringBuilder command = new StringBuilder(ADDQUERY);
		command.append(tableName);
		command.append(" (");
		for(int i = 0 ;i < currCols.length; i++) {
			command.append(currCols[i]);
			if(i != currCols.length - 1) {
				command.append(", ");
			}
		}
		command.append(") values (");
		for(int i = 0 ; i < colsValues.length ; i++) {
			command.append(colsValues[i]);
			if(i != colsValues.length - 1) {
				command.append(", ");
			}
		}
		command.append(");");
		return command.toString();
	}
	
	public static String makeAddTableCommad(String tableName, String[] colName, String[] colType) {
		StringBuilder command = new StringBuilder(ADDTABLE);
		command.append(tableName);
		command.append(" (");
		for(int i = 0 ; i < colName.length ; i++) {
			command.append(colName[i]);
			command.append(" ");
			command.append(colType[i]);
			if(i != colType.length - 1) {
				command.append(",");
			}
		}
		command.append(");");
		return command.toString();
	}
	

}
