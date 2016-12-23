package JDBC.UserInteface;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CommandManager {

	private CommandParseSingleton parser;
	private Command command;
	private Statement statm;

	private Table resultTable;
	private TableShape tableShape;

	public CommandManager(Statement statm) {

		parser = CommandParseSingleton.getInstance();
		tableShape = TableShape.getInstance();
		this.statm = statm;
	}

	public void manage(String sentence) {
		sentence = RemoveSemiCol(sentence);
		if (sentence != null) {
			String[] commands = SplitCommands(sentence);
			for (int i = 0; i < commands.length; i++) {
				Execute(commands[i]);
				System.out.println();
			}
		}
	}

	private void Execute(String sentence) {
		if (sentence.trim().isEmpty()) {
			System.out.println("No Query specified");
			return;
		}
		command = parser.getCommand(sentence);
		if (command == null) {
			System.out.println("Error in SQL syntax");
			return;
		}
		command.Execute(statm, sentence);
		printMessage(command);
		printResult(command);
	}

	private void printMessage(Command command) {
		String message = command.getMessage();
		if (message != null) {
			System.out.println(message);
		}
	}

	private void printResult(Command command) {
		ResultSet result = command.getResult();
		if (result != null) {
			if (isEmpty(result)) {
				System.out.println("Empty set");
			} else {
				try {
					resultTable = new Table(result);
					tableShape.setTable(resultTable);
					tableShape.print();
				} catch (SQLException e) {
					System.out.println("Unable to print the result");
				}
			}
		}
	}

	private boolean isEmpty(ResultSet result) {
		try {
			int i = 0;
			while (result.next()) {
				i++;
			}
			if (i == 0) {
				return true;
			}
			result.beforeFirst();
		} catch (SQLException e) {
		}
		return false;
	}

	private String RemoveSemiCol(String sentence) {
		sentence = sentence.trim();
		int len = sentence.length();
		if (sentence.charAt(len - 1) == ';') {
			return sentence.substring(0, len - 1);
		}
		System.out.println("Error : Commands end with ; ");
		return null;
	}

	private String[] SplitCommands(String sentence) {
		return sentence.split(";");
	}

}
