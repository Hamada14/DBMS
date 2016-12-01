package dBManagement;

import java.util.LinkedList;

public interface DBInterface {

    public boolean dropDataBase(String dbName);

    public boolean addDataBase(String dbName);

    public boolean addTable(String tableName, String[][] tableData);

    public boolean dropTable(String tableName);

    public LinkedList<LinkedList<String>> selectQuery(String tableName, String[] colName, String condition);

    public boolean insertQuery(String tableName, String[] inputName, String[] varName);

    public boolean deleteQuery(String tableName, String condition);

    public boolean updateQuery(String tableName, String[][] data, String condition);

}