package JDBC.UserInteface;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

public class Table {
	
	private LinkedList<LinkedList<String>> tableData;
	private ResultSet result;
	
	public Table(ResultSet result) throws SQLException {
		this.result = result;
		tableData = new LinkedList<LinkedList<String>>();
		setTable();
		setColName();
	}
	
	public int getLength() {
		return tableData.size();
	}
	
	public int getWidth() {
		try{
			return tableData.get(0).size();			
		} catch (Exception e) {
			return 0;
		}
	}
	
	public String getCell(int row, int col) {
		return tableData.get(row).get(col);
	}
	private void setTable() throws SQLException {
		int colCount = result.getMetaData().getColumnCount();
		LinkedList<String> Row;
		while (result.next()) {
			Row = new LinkedList<String> ();
			for (int i = 1; i <= colCount; i++) {
				Row.add(result.getString(i));
			}
			tableData.addLast(Row);
		}
	}
	
	private void setColName() throws SQLException {
		LinkedList<String> Row = new LinkedList<String>();
		int colCount = result.getMetaData().getColumnCount();
		for (int i = 1; i <= colCount; i++) {
			Row.add(result.getMetaData().getColumnName(i));
		}
		tableData.addFirst(Row);
	}
	
	public LinkedList<LinkedList<String>> getTableData () {
		return tableData;
	}

}
