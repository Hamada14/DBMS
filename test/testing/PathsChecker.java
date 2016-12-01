package testing;

import java.io.File;
import java.io.IOException;

import dBManagement.DBManagerSingleton;
import userInterface.DirectoryCreatorSingleton;

public class PathsChecker {

	private static PathsChecker instance = null;
	private String dbmsDir;

	private PathsChecker() {
	}

	public static PathsChecker getInstance() {
		if (instance == null) {
			instance = new PathsChecker();
		}
		return instance;
	}

	public void creatDirectory() {
		DirectoryCreatorSingleton dirCreator = DirectoryCreatorSingleton.getInstance();
		dbmsDir = dirCreator.manageCreation();
		DBManagerSingleton instance = DBManagerSingleton.getInstance();
		instance.setDBMSPath(dbmsDir);
	}

	public boolean isPresentDBPath(String dbName) {
		String path = dbmsDir + File.separator + dbName;
		File dbDir = new File(path);
		return fileExists(dbDir);
	}

	public boolean isPresentTablePath(String dbName, String tableName) {
		if (!isPresentDBPath(dbName)) {
			return false;
		}
		String path = dbmsDir + File.separator + dbName + File.separator
				+ tableName;
		File tableDir = new File(path);
		return fileExists(tableDir);
	}

	public boolean isPresentFilesPath(String dbName, String tableName) {
		if (!isPresentTablePath(dbName, tableName)) {
			return false;
		}
		String path = dbmsDir + File.separator + dbName + File.separator + tableName
				+ File.separator + tableName;
		String dtdPath = path + ".dtd";
		String xmlPath = path + ".xml";
		File xmlFile = new File(dtdPath);
		File dtdFile = new File(xmlPath);
		return fileExists(dtdFile) && fileExists(xmlFile);
	}

	private boolean fileExists(File f) {
		try {
			return f.exists() && f.getCanonicalPath().endsWith(f.getName());
		} catch (IOException e) {
			return false;
		}
	}
}