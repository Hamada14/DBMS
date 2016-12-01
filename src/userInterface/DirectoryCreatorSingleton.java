package userInterface;

import java.io.File;

public class DirectoryCreatorSingleton {

  private static DirectoryCreatorSingleton directoryCreatorSingleton;
  private String homePath;
  private static final String DBDIRECTORY = "DBMS";
  private static final String HOME_VAR = "user.home";
  
  private DirectoryCreatorSingleton() {
  }

  public static DirectoryCreatorSingleton getInstance() {
    if (directoryCreatorSingleton == null) {
      directoryCreatorSingleton = new DirectoryCreatorSingleton();
    }
    return directoryCreatorSingleton;
  }

  public String manageCreation() {
    homePath = getHomePath();
    String directoryPath = homePath + File.separator + DBDIRECTORY;
    File dbDir = new File(directoryPath);
    if (!dbDir.exists()) {
      creatDirectory(directoryPath);
    }
    return directoryPath;
  }

  private String getHomePath() {
    return System.getProperty(HOME_VAR);
  }

  private boolean creatDirectory(String directoryPath) {
    return new File(directoryPath).mkdir();
  }
}