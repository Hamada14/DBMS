package JDBC.UserInteface;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Scanner;

import JDBC.driver.DBMSDriver;

public class UserInterface {

	private Scanner sc;
	private CommandManager manager;
	private Driver driver;
	private Connection connection;
	private Statement statement;

	public UserInterface() {
		sc = new Scanner(System.in);
		if (CreateConnection()) {
			interact();
		}
	}

	private boolean CreateConnection() {
		driver = new DBMSDriver();
		try {
			Properties info = readProp();
			String DBWriter = info.getProperty("DataBaseWriter");
			connection = driver.connect("jdbc:" + DBWriter + "://localhost", info);
			statement = connection.createStatement();
			manager = new CommandManager(statement);
		} catch (SQLException e) {
			System.out.println("Unable to create a connection");
			return false;
		} catch (IOException e) {
			System.out.println("Unable to find Configuration file");
			return false;
		}
		return true;
	}

	private Properties readProp() throws IOException {
		Properties prop = new Properties();
		InputStream inputStream = new FileInputStream("config.properties");
		prop.load(inputStream);
		return prop;
	}

	private void interact() {
		boolean working = true;
		while (working) {
			System.out.print(">> ");
			working = getCommand();

		}
	}

	private boolean getCommand() {
		String command = sc.nextLine();
		if (command.equalsIgnoreCase("exit")) {
			System.out.println("Bye");
			CloseConnection();
			return false;
		}
		if (!command.trim().isEmpty()) {
			manager.manage(command);
		}
		return true;
	}

	private void CloseConnection() {
		try {
			statement.close();
			connection.close();
		} catch (SQLException e) {
			System.out.println("Unable to close the connection");
		}
	}

	public static void main(String[] args) {

		new UserInterface();

	}
}