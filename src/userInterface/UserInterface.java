package userInterface;

import java.util.Scanner;

import dBManagement.DBManagerSingleton;
import sqlParsers.SQLParserSingleton;

public class UserInterface {

	private static Scanner sc;
	private SQLParserSingleton instance;

	public UserInterface() {
		sc = new Scanner(System.in);
		instance = SQLParserSingleton.getInstance();
		interfaceManager();
	}

	private void interfaceManager() {
		creatDirectory();
		interact();
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
		if(command.equalsIgnoreCase("exit")){
		  System.out.println("Bye");
		  return false;
		}
		instance.parseSQL(command);
		return true;
	}

	private void creatDirectory() {
		DirectoryCreatorSingleton dirCreator = DirectoryCreatorSingleton.getInstance();
		String dirPath = dirCreator.manageCreation();
		DBManagerSingleton instance = DBManagerSingleton.getInstance();
		instance.setDBMSPath(dirPath);
	}

	public static void main(String[] args) {
		new UserInterface();
	}
}