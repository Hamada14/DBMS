package test.dbms;

import java.io.File;
import java.io.IOException;

public class PathsChecker {

	private static PathsChecker instance = null;

	private PathsChecker() {
	}

	public static PathsChecker getInstance() {
		if (instance == null) {
			instance = new PathsChecker();
		}
		return instance;
	}

	public boolean isPresentDir(String path) {
		File file = new File(path);
		return fileExists(file);
	}

	public boolean isPresentDBPath(String dbmsDir, String dbName) {
		String path = dbmsDir + File.separator + dbName;
		File dbDir = new File(path);
		return fileExists(dbDir);
	}

	public boolean isPresentTablePath(String dbmsDir, String dbName, String tableName) {
		if (!isPresentDBPath(dbmsDir, dbName)) {
			return false;
		}
		String path = dbmsDir + File.separator + dbName + File.separator + tableName;
		File tableDir = new File(path);
		return fileExists(tableDir);
	}

	public boolean isPresentFilesPath(String dbmsDir, String dbName, String tableName) {
		if (!isPresentTablePath(dbmsDir, dbName, tableName)) {
			return false;
		}
		String path = dbmsDir + File.separator + dbName + File.separator + tableName + File.separator + tableName;
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