package JDBC.UserInteface;

public class CommandParseSingleton {

	private static CommandParseSingleton Parser;

	private CommandParseSingleton() {

	}

	public static CommandParseSingleton getInstance() {
		if (Parser == null) {
			Parser = new CommandParseSingleton();
		}
		return Parser;
	}

	public Command getCommand(String statement) {
		statement = statement.trim().toLowerCase();
		if (statement.indexOf("select ") == 0) {
			return new SelectCommand();
		} else if (statement.indexOf("use ") == 0) {
			return new UseCommand();
		} else if (statement.indexOf("create ") == 0) {
			return new createCommand();
		} else if (statement.indexOf("delete ") == 0) {
			return new UpdateCommand();
		} else if (statement.indexOf("drop ") == 0) {
			return new UpdateCommand();
		} else if (statement.indexOf("insert ") == 0) {
			return new UpdateCommand();
		} else if (statement.indexOf("update ") == 0) {
			return new UpdateCommand();
		} else if (statement.indexOf("alter ") == 0) {
			return new AlterCommand();
		}
		return null;
	}
}
