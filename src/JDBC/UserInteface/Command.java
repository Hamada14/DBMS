package JDBC.UserInteface;

import java.sql.ResultSet;
import java.sql.Statement;

public abstract class Command {

	protected ResultSet result;
	protected String message;

	public Command() {
		result = null;
		message = null;
	}

	public ResultSet getResult() {
		return result;
	}

	public String getMessage() {
		return message;
	}

	public abstract void Execute(Statement statement, String command);
}
