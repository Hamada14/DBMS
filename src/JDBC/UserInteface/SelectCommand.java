package JDBC.UserInteface;

import java.sql.SQLException;
import java.sql.Statement;

public class SelectCommand extends Command {

	@Override
	public void Execute(Statement statement, String command) {
		try {
			command = command.trim();
			statement.execute(command);
			result = statement.executeQuery(command);
		} catch (SQLException e) {
			message = e.getMessage();
		}
	}

	
}
