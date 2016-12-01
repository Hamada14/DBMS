package dBManagement;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import org.xml.sax.SAXException;

import evaluator.BooleanEvaluatorSingleton;
import parse.DTDParser;
import parse.XMLParser;

public class Table {

  private String name;
  private String path, tablePath;

  public Table(String name, String filePath, String[] colName, String[] types)
      throws Exception {
    path = filePath;
    tablePath = filePath;
    path = path + File.separator + name;
    DTDParser.getInstance().createDTD(path, name, colName);
    XMLParser.getInstance().creatXmlFile(name, path, colName, types);
  }

  public String getPath() {
    return this.tablePath;
  }

  // Using existing table
  public Table(String name, String filePath) throws Exception {
    path = filePath;
    this.name = name;
  }

  public Table() {
  }

  public String getName() {
    return name;
  }

  private String getColumnValue(String type) {
    if (type.equals("i")) {
      return new String("0");
    }
    return new String("\'m\'");
  }

  private boolean invalidString(String value) {
    if (value.length() < 2) {
      return true;
    }
    char quoteType = value.charAt(0);
    if ((quoteType != '\'' && quoteType != '\"')
        || quoteType != value.charAt(value.length() - 1)) {
      return true;
    }
    return false;
  }

  private boolean checkType(String type, String value) {
    if (type.equals("i")) {
      if (!value.matches("[0-9]+"))
        return false;
    } else if (invalidString(value)) {
      return false;
    }
    return true;
  }

  private boolean checkSelectedColumns(String filePath, String selectedCol[])
      throws SAXException, IOException, Exception {
    String colNames[] = XMLParser.getInstance().getColNames(filePath);
    boolean found;
    for (int i = 0; i < selectedCol.length; i++) {
      found = false;
      for (int j = 0; j < colNames.length; j++) {
        if (selectedCol[i].equals(colNames[j])) {
          found = true;
          break;
        }
      }
      if (!found) {
        return false;
      }
    }
    return true;
  }

  public boolean checkCondition(String condition, String path)
      throws SAXException, IOException, Exception {
    if (condition == null)
      return true;
    String[] colName = XMLParser.getInstance().getColNames(path);
    String[] colType = XMLParser.getInstance().getColTypes(path);
    BooleanEvaluatorSingleton bes = BooleanEvaluatorSingleton.getInstance();
    condition = " " + condition + " ";
    condition = condition.replaceAll(
        "(?i) and (?=([^\"']*[\"'][^\"']*[\"'])*[^\"']*$)", " & ");
    condition = condition.replaceAll(
        "(?i) or (?=([^\"']*[\"'][^\"']*[\"'])*[^\"']*$)", " | ");
    condition = condition.replaceAll(
        "(?i) not (?=([^\"']*[\"'][^\"']*[\"'])*[^\"']*$)", " ! ");
    for (int idx = 0; idx < colName.length; idx++) {
      String curIdentity = colName[idx];
      String curValue = getColumnValue(colType[idx]);
      condition = condition.replaceAll("(?i) " + curIdentity
          + " (?=([^\"']*[\"'][^\"']*[\"'])*[^\"']*$)", curValue);
    }
    bes.isValid(condition);
    return true;
  }

  private boolean checkType(String key[], String[] values, String path)
      throws SAXException, IOException, Exception {
    String[] colTypes = XMLParser.getInstance().getColTypes(path);
    String[] colNames = XMLParser.getInstance().getColNames(path);
    for (int i = 0, j = 0; i < values.length; i++) {
      for (; j < colNames.length; j++) {
        if (key[i].equals(colNames[j])) {
          if (!checkType(colTypes[j], values[i])) {
            System.out.println("error in types");
            return false;
          } else
            break;
        }
      }
      if (j == colNames.length) {
        System.out.println("error in column names");
        return false;
      }
    }
    return true;
  }

  private String[] fixArray(String[] newValues) {
    String[] retValues = new String[newValues.length];
    for (int idx = 0; idx < newValues.length; idx++) {
      if (newValues[idx] == null)
        retValues[idx] = new String("null");
      else
        retValues[idx] = newValues[idx];
    }
    return retValues;
  }

  public LinkedList<LinkedList<String>> selectFromTable(String[] selectedCol,
      String condition, String path) throws Exception {
    LinkedList<LinkedList<String>> retTable = new LinkedList<LinkedList<String>>();
    if ((!checkSelectedColumns(path, selectedCol) || (!checkCondition(
        condition, path))) && (!selectedCol[0].equals("*"))) {
      System.out.println("Error in column names");
      return null;
    }
    if (selectedCol.length == 1 && selectedCol[0].equals("*"))
      retTable = selectAllColumns(condition, path);
    else
      retTable = selectedColumns(selectedCol, condition, path);
    return retTable;
  }

  private LinkedList<LinkedList<String>> selectAllColumns(String condition,
      String path) throws SAXException, IOException, Exception {
    LinkedList<LinkedList<String>> retTable = new LinkedList<LinkedList<String>>();
    LinkedList<String> names = new LinkedList<String>();
    String[] colName = XMLParser.getInstance().getColNames(path);
    for (String x : colName)
      names.add(x);
    retTable.add(names);
    for (int i = 0; i < XMLParser.getInstance().getRowsNumb(path); i++) {
      Entry tmp = new Entry(XMLParser.getInstance().getRow(path, i), XMLParser
          .getInstance().getColNames(path));
      try {
        if (tmp.isValid(condition))
          retTable.add(tmp.getEntry());
      } catch (Exception e) {
        System.out.println("Invalid format");
      }
    }
    return retTable;
  }

  private LinkedList<LinkedList<String>> selectedColumns(String[] selectedCol,
      String condition, String path) throws SAXException, IOException,
      Exception {
    LinkedList<LinkedList<String>> retTable = new LinkedList<LinkedList<String>>();
    LinkedList<String> names = new LinkedList<String>();
    for (String x : selectedCol)
      names.add(x);
    retTable.add(names);
    for (int i = 0; i < XMLParser.getInstance().getRowsNumb(path); i++) {
      Entry tmp = new Entry(XMLParser.getInstance().getRow(path, i), XMLParser
          .getInstance().getColNames(path));
      try {
        if (tmp.isValid(condition)) {
          retTable.add(tmp.getEntry(selectedCol));
        }
      } catch (Exception e) {
        System.out.println("Invalid format");
      }
    }

    return retTable;
  }

  // --- Insert new Entry to the table
  public boolean insertIntoTable(String[] columns, String[] values, String path)
      throws Exception {
    if (!checkSelectedColumns(path, columns)) {
      System.out.println("Make sure that all the columns exist in this table.");
      return false;
    }
    String colNames[] = XMLParser.getInstance().getColNames(path);
    String colTypes[] = XMLParser.getInstance().getColTypes(path);
    String newValues[] = new String[colNames.length];
    for (int i = 0; i < colNames.length; i++) {
      for (int j = 0; j < columns.length; j++) {
        if (colNames[i].equals(columns[j])) {
          if (!checkType(colTypes[i], values[j])) {
            System.out
                .println("Make sure the table data is written correctly.");
            return false;
          } else
            newValues[i] = new String(values[j]);
        }
      }
    }
    newValues = fixArray(newValues);
    XMLParser.getInstance().addRow(path, colNames, newValues);
    return true;
  }

  // --- Delete Entry
  public boolean deleteFromTable(String condition, String path)
      throws Exception {
    if (!checkCondition(condition, path)) {
      return false;
    }
    for (int i = 0; i < XMLParser.getInstance().getRowsNumb(path); i++) {
      Entry tmp = new Entry(XMLParser.getInstance().getRow(path, i), XMLParser
          .getInstance().getColNames(path));
      try {
        if (tmp.isValid(condition)){
          XMLParser.getInstance().deleteRaw(path, i);
          i--;
        }
      } catch (Exception e) {
        System.out.println("Invalid format");
        return false;
      }
    }
    return true;
  }

  public boolean updateTable(String[] keys, String[] content, String condition,
      String Path) throws Exception {
    try {
      if (!checkType(keys, content, Path)) {
        return false;
      } else if (!checkCondition(condition, Path)) {
        return false;
      }
      for (int i = 0; i < XMLParser.getInstance().getRowsNumb(Path); i++) {
        Entry tmp = new Entry(XMLParser.getInstance().getRow(Path, i),
            XMLParser.getInstance().getColNames(Path));
        if (tmp.isValid(condition)) {
          tmp.update(keys, content);
          XMLParser.getInstance().updateRow(Path,
              tmp.getEntry().toArray(new String[tmp.cells.size()]), i);
        }
      }
      return true;
    } catch (Exception e) {
      System.out.println("Invalid format");
      return false;
    }
  }
}