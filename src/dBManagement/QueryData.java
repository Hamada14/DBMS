package dBManagement;

import java.sql.SQLException;
import java.util.LinkedList;

public class QueryData {

	private LinkedList<Entry> records;
	private LinkedList<String> colTypes;
	private LinkedList<String> colNames;
	private String tableName;
	private int counter;
	private int cursor;

	public QueryData(String name, LinkedList<String> colNames, LinkedList<String> colTypes) {
		this.tableName = name;
		this.colNames = colNames;
		this.colTypes = colTypes;
		records = new LinkedList<Entry>();
		cursor = 0;
	}

	public String getCell(int rowIndx, int colIndx) {
		return records.get(rowIndx - 1).cells.get(colNames.get(colIndx - 1));
	}

	public int getColumnByName(String name) {
		int index = colNames.indexOf(name);
		if (index != -1)
			return index + 1;
		return 0;
	}

	public int findColumn(String columnLabel) throws SQLException {
		int index = getColumnByName(columnLabel);
		if (index == 0) {
			throw new SQLException();
		}
		return index;
	}

	public void checkRowIndex() throws SQLException {
		if (getCursor() > rowsCount()) {
			throw new SQLException();
		}
	}

	public void checkColIndex(int i) throws SQLException {
		if (i > columnCount() || i < 1) {
			throw new SQLException();
		}
	}

	public void countUpdate() {
		counter++;
	}

	public int rowsCount() {
		return records.size();
	}

	public int columnCount() {
		return colNames.size();
	}

	public String getColumnName(int index) {
		return colNames.get(index - 1);
	}

	public String columnType(int index) {
		return colTypes.get(index - 1);
	}

	public int getColumnType(int index) {
		switch (columnType(index)) {
		case "int":
			return java.sql.Types.INTEGER;
		case "varchar":
			return java.sql.Types.VARCHAR;
		case "date":
			return java.sql.Types.DATE;
		case "float":
			return java.sql.Types.FLOAT;
		default:
			return java.sql.Types.NULL;
		}
	}

	public String getTableName() {
		return tableName;
	}

	public int getCounter() {
		return counter;
	}

	public boolean next() {
		cursor++;
		if (cursor > rowsCount()) {
			cursor = rowsCount() + 1;
			return false;
		}
		return true;
	}

	public boolean previous() {
		cursor--;
		if (cursor < 1) {
			cursor = 0;
			return false;
		}
		return true;
	}

	public boolean isBeforeFirst() throws SQLException {
		if (cursor == 0)
			return true;
		return false;
	}

	public boolean isAfterLast() throws SQLException {
		if (cursor > rowsCount())
			return true;
		return false;
	}

	public boolean absolute(int row) throws SQLException {
		if (row >= 0) {
			cursor = row;
		} else {
			cursor = rowsCount() + row + 1;
		}
		return (cursor > 0 && cursor <= rowsCount());
	}

	public int getCursor() {
		return cursor;
	}

	public void setCursor(int cursor) {
		this.cursor = cursor;
	}

	public int getSize() {
		return records.size();
	}

	public int getWidth() {
		if (getSize() > 0) {
			return colNames.size();
		}
		return 0;
	}

	public String getElement(int row, int column) {
		return records.get(row).cells.get(colNames.get(column));
	}

	public void addEntry(Entry entry) {
		records.add(entry);

	}
}
