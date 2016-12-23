package json;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import backendParsers.Parser;

public class JsonWriter implements Parser {

	private static final String JSONEXTENSION = ".json", EMPTY = "null";
	private static final int NOTFOUND = -1;

	public JsonWriter() {
	}

	public void createFile(String tableName, String filePath, String[] colNames, String[] types) throws IOException {
		filePath = getFullPath(filePath);
		initializeFile(filePath);
		initColsToFile(filePath, colNames, types);
	}

	public String[] getColumnNames(String filePath) throws FileNotFoundException, IOException {
		TableStructure tableStructure = getTable(getFullPath(filePath));
		return convertToArray(tableStructure.getColumnNames());
	}

	public String[] getColumnTypes(String filePath) throws IOException {
		TableStructure tableStructure = getTable(getFullPath(filePath));
		return convertToArray(tableStructure.getColumnTypes());
	}

	public void addColumn(String[][] columns, String filePath) throws IOException {
		filePath = getFullPath(filePath);
		TableStructure tableStructure = getTable(filePath);
		int sz = tableStructure.getEntries().size();
		for (int i = 0; i < columns[0].length; i++) {
			tableStructure.getColumnNames().add(columns[0][i]);
			tableStructure.getColumnTypes().add(columns[1][i]);
			for (int j = 0; j < sz; j++) {
				tableStructure.getEntries().get(j).add(EMPTY);
			}
		}
		writeTable(filePath, tableStructure);
	}

	public void dropColumn(String[] columns, String filePath) throws IOException {
		filePath = getFullPath(filePath);
		TableStructure tableStructure = getTable(filePath);
		int rows = tableStructure.getEntries().size();
		int cols = tableStructure.getColumnNames().size();
		for (int i = cols - 1; i >= 0; i--) {
			if (getColumnIndex(tableStructure.getColumnNames().get(i), columns) != NOTFOUND) {
				tableStructure.getColumnNames().remove(i);
				tableStructure.getColumnTypes().remove(i);
				for (int j = 0; j < rows; j++) {
					tableStructure.getEntries().get(j).remove(i);
				}
			}
		}
		writeTable(filePath, tableStructure);
	}

	public void addRow(String filePath, String[] columns, String[] values) throws IOException {
		filePath = getFullPath(filePath);
		TableStructure tableStructure = getTable(filePath);
		ArrayList<String> addedRow = new ArrayList<String>();
		ArrayList<String> colNames = tableStructure.getColumnNames();
		for (int i = 0; i < colNames.size(); i++) {
			int colIndex = getColumnIndex(colNames.get(i), columns);
			if (colIndex != NOTFOUND && values[colIndex] != null) {
				addedRow.add(values[colIndex]);
			} else {
				addedRow.add(EMPTY);
			}
		}
		tableStructure.addEntry(addedRow);
		writeTable(filePath, tableStructure);
	}

	public int getRowCount(String filePath) throws IOException {
		TableStructure tableStructure = getTable(getFullPath(filePath));
		return tableStructure.getEntries().size();
	}

	public LinkedList<String> getRow(String filePath, int rowNumber) throws IOException, RuntimeException {
		TableStructure tableStructure = getTable(getFullPath(filePath));
		if (rowNumber >= tableStructure.getEntries().size()) {
			throw new RuntimeException();
		}
		return new LinkedList<String>(tableStructure.getEntries().get(rowNumber));
	}

	/// end of read and write json table

	public void updateRow(String filePath, String[] data, int rowNumber) throws IOException, RuntimeException {
		filePath = getFullPath(filePath);
		TableStructure tableStructure = getTable(filePath);
		int sz = tableStructure.getEntries().get(rowNumber).size();
		if (rowNumber > tableStructure.getEntries().size() || data.length != sz) {
			throw new RuntimeException();
		}
		for (int i = 0; i < sz; i++) {
			tableStructure.getEntries().get(rowNumber).set(i, data[i]);
		}
		writeTable(filePath, tableStructure);
	}

	public void deleteRow(String filePath, int rowNumber) throws IOException, RuntimeException {
		filePath = getFullPath(filePath);
		TableStructure tableStructure = getTable(filePath);
		if (rowNumber >= tableStructure.getEntries().size()) {
			throw new RuntimeException();
		}
		tableStructure.getEntries().remove(rowNumber);
		writeTable(filePath, tableStructure);
	}

	private void initializeFile(String filePath) throws IOException {
		File file = new File(filePath);
		file.createNewFile();
	}

	private void initColsToFile(String filePath, String[] colNames, String[] colTypes) throws IOException {
		ArrayList<String> columnNames = convertToArrayList(colNames);
		ArrayList<String> columnTypes = convertToArrayList(colTypes);
		TableStructure table = new TableStructure();
		table.setColumnNames(columnNames);
		table.setColumnTypes(columnTypes);
		writeTable(filePath, table);
	}

	private File makeFile(String path) {
		File file = new File(path);
		return file;
	}

	// read and write json Table
	private void writeTable(String filePath, TableStructure tableStructure) throws IOException {
		Gson gsonTable = new GsonBuilder().setPrettyPrinting().create();
		String jsonWriter = gsonTable.toJson(tableStructure);
		FileWriter writer = new FileWriter(filePath);
		writer.write(jsonWriter);
		writer.close();
	}

	private String getFullPath(String path) {
		return path + JSONEXTENSION;
	}

	private TableStructure getTable(String filePath) throws FileNotFoundException {
		File file = makeFile(filePath);
		Gson gsonTable = new Gson();
		BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
		TableStructure tableStructure = gsonTable.fromJson(bufferedReader, TableStructure.class);
		return tableStructure;
	}

	private int getColumnIndex(String colName, String[] columns) {
		for (int i = 0; i < columns.length; i++) {
			if (columns[i] != null && colName != null && colName.equals(columns[i])) {
				return i;
			}
		}
		return NOTFOUND;
	}

	private ArrayList<String> convertToArrayList(String[] ordinaryArray) {
		ArrayList<String> convertedArray = new ArrayList<String>();
		for (int i = 0; i < ordinaryArray.length; i++) {
			convertedArray.add(ordinaryArray[i]);
		}
		return convertedArray;
	}

	private String[] convertToArray(ArrayList<String> arrayList) {
		String[] ordinaryArray = new String[arrayList.size()];
		for (int i = 0; i < arrayList.size(); i++) {
			ordinaryArray[i] = arrayList.get(i);
		}
		return ordinaryArray;
	}
}
