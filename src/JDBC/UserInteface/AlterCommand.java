package JDBC.UserInteface;

import java.sql.SQLException;
import java.sql.Statement;

public class AlterCommand extends Command {

	@Override
	public void Execute(Statement statement, String command) {
		try {
			statement.execute(command);
			message = createMessage(command);
		} catch (SQLException e) {
			message = e.getMessage();
		}
	}

	private String createMessage(String command) {
		if (command.toLowerCase().contains(" add ")) {
			return "Column is added";
		} else if (command.toLowerCase().contains(" drop ")) {
			return "Column is dropped";
		}
		return "";
	}
}
