package dBManagement;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.script.ScriptException;

import evaluator.BooleanEvaluatorSingleton;

public class Entry {

  LinkedHashMap<String, String> cells;

  public Entry(LinkedList<String> row, String[] colNames) {
    int i = 0;
    cells = new LinkedHashMap<String, String>();
    for (String x : colNames) {
      cells.put(new String(x), new String(row.get(i++)));
    }
  }

  public boolean isValid(String condition) throws ScriptException {
    if (condition == null)
      return true;
    BooleanEvaluatorSingleton bes = BooleanEvaluatorSingleton.getInstance();
    condition = " " + condition + " ";
    condition = condition.replaceAll("(?i) and (?=([^\"']*[\"'][^\"']*[\"'])*[^\"']*$)",
        " & ");
    condition = condition.replaceAll("(?i) or (?=([^\"']*[\"'][^\"']*[\"'])*[^\"']*$)",
        " | ");
    condition = condition.replaceAll("(?i) not (?=([^\"']*[\"'][^\"']*[\"'])*[^\"']*$)",
        " ! ");

    for (Map.Entry<String, String> curMapEntry : cells.entrySet()) {
      String curIdentity = curMapEntry.getKey();
      String curValue = curMapEntry.getValue();
      condition = condition.replaceAll("(?i) " + curIdentity
          + " (?=([^\"']*[\"'][^\"']*[\"'])*[^\"']*$)", curValue);
    }
    return bes.isValid(condition);
  }

  public void update(String[] keys, String[] content) {
    int i = 0;
    for (String k : keys) {
      cells.put(k, content[i++]);
    }
  }

  public LinkedList<String> getEntry() {
    LinkedList<String> record = new LinkedList<String>();
    for (String x : cells.values())
      record.add(new String(x));
    return record;
  }

  public LinkedList<String> getEntry(String[] selectedCol) {
    LinkedList<String> record = new LinkedList<String>();
    for (String x : selectedCol) {
      record.add(cells.get(x));
    }
    return record;
  }
}