package JDBC.UserInteface;

import java.sql.SQLException;
import java.sql.Statement;

public class createCommand extends Command {

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
		if (command.toLowerCase().contains(" table ")) {
			return "Table is created";
		} else if (command.toLowerCase().contains(" database ")) {
			return "Database is created";
		}
		return "";
	}
}
