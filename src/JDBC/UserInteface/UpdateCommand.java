package JDBC.UserInteface;

import java.sql.SQLException;
import java.sql.Statement;

public class UpdateCommand extends Command {

	@Override
	public void Execute(Statement statement, String command) {
		try {
			int counter;
			counter = statement.executeUpdate(command);
			message = "Query ok, " + counter + " rows affected";
		} catch (SQLException e) {
			message = e.getMessage();
		}
	}
}
