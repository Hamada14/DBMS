package JDBC.UserInteface;

import java.io.File;

public class DirectoryCreator {

	private static DirectoryCreator directoryCreator;

	private DirectoryCreator() {
	}

	public static DirectoryCreator getInstance() {
		if (directoryCreator == null) {
			directoryCreator = new DirectoryCreator();
		}
		return directoryCreator;
	}

	public String manageCreation(String dbmsPath) {
		File dbDir = new File(dbmsPath);
		if (!dbDir.exists()) {
			createDirectory(dbmsPath);
		}
		return dbmsPath;
	}

	private boolean createDirectory(String directoryPath) {
		return new File(directoryPath).mkdir();
	}
}