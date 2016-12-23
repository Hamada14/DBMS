package json;

import java.util.ArrayList;

public class TableStructure {

	private ArrayList<String> columnNames;
	private ArrayList<String> columnTypes;
	private ArrayList<ArrayList<String>> entries;

	public TableStructure() {
		columnNames = new ArrayList<String>();
		entries = new ArrayList<ArrayList<String>>();
		columnTypes = new ArrayList<String>();
	}

	public ArrayList<ArrayList<String>> getEntries() {
		return this.entries;
	}

	public void setEntries(ArrayList<ArrayList<String>> entries) {
		this.entries = entries;
	}

	public void addEntry(ArrayList<String> newEntry) {
		entries.add(newEntry);
	}

	public ArrayList<String> getColumnNames() {
		return this.columnNames;
	}

	public void setColumnNames(ArrayList<String> colNames) {
		this.columnNames = colNames;
	}

	public ArrayList<String> getColumnTypes() {
		return this.columnTypes;
	}

	public void setColumnTypes(ArrayList<String> colTypes) {
		this.columnTypes = colTypes;
	}

}
