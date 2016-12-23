package JDBC.UserInteface;

import java.sql.SQLException;
import java.sql.Statement;

public class UseCommand extends Command {

	@Override
	public void Execute(Statement statement, String command) {
		try {
			statement.execute(command);
			message = "Database changed";
		} catch (SQLException e) {
			message = e.getMessage();
		}
	}
}
