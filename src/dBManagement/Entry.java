package dBManagement;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.script.ScriptException;

import evaluator.BooleanEvaluator;

public class Entry {
	/**
	 * Record data.
	 */
	protected LinkedHashMap<String, String> cells;

	public Entry(final LinkedList<String> row, final String[] colNames) {
		int i = 0;
		cells = new LinkedHashMap<String, String>();
		for (String x : colNames) {
			cells.put(new String(x), new String(row.get(i++)));
		}
	}

	public Entry(final Entry entry, final String[] columnNames) {
		cells = new LinkedHashMap<String, String>();
		for (String key : columnNames) {
			addKeyValue(key, entry.getValue(key));
		}
	}

	private void addKeyValue(String key, String value) {
		cells.put(new String(key), new String(value));
	}

	public String getValue(String key) {
		return cells.get(key);
	}

	public boolean isValid(String condition) throws ScriptException {
		if (condition == null)
			return true;
		BooleanEvaluator bes = BooleanEvaluator.getInstance();
		condition = " " + condition + " ";
		condition = condition.replaceAll("(?i) and (?=([^\"']*[\"'][^\"']*[\"'])*[^\"']*$)", " & ");
		condition = condition.replaceAll("(?i) or (?=([^\"']*[\"'][^\"']*[\"'])*[^\"']*$)", " | ");
		condition = condition.replaceAll("(?i) not (?=([^\"']*[\"'][^\"']*[\"'])*[^\"']*$)", " ! ");

		for (Map.Entry<String, String> curMapEntry : cells.entrySet()) {
			String curIdentity = curMapEntry.getKey();
			String curValue = curMapEntry.getValue();
			condition = condition.replaceAll("(?i)" + curIdentity + "(?=([^\"']*[\"'][^\"']*[\"'])*[^\"']*$)",
					curValue);
		}
		return bes.isValid(condition);
	}

	/**
	 * Update records.
	 */
	public void update(final String[] keys, final String[] content) {
		int i = 0;
		for (String k : keys) {
			cells.put(k, content[i++]);
		}
	}

	public Entry getEntry() {
		return this;
	}

	public LinkedList<String> getEntryAsList() {
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