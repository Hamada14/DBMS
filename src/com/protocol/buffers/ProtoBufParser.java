package com.protocol.buffers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;

import com.protocol.buffers.TabularStructure.ColInformation;
import com.protocol.buffers.TabularStructure.Entry;
import com.protocol.buffers.TabularStructure.Table;

import backendParsers.Parser;

public class ProtoBufParser implements Parser {

    private static final String TXTEX = ".txt";
    private static final int NOTFOUND = -1;

    public ProtoBufParser() {
    }

    @Override
	public void createFile(String tableName, String filePath, String[] colNames, String[] type) throws IOException {
	    filePath = getFullPath(filePath);
	    if (!initializeFile(filePath)) {
	        throw new IOException();
	    }
	    if (!initColsToFile(filePath, colNames, type)) {
	        throw new IOException();
	    }
	}
    
	@Override
	public void addColumn(String[][] columns, String filePath) throws FileNotFoundException, IOException {
	    File file = makeFile(getFullPath(filePath));
	    Table.Builder table = Table.newBuilder();
	    table.mergeFrom(new FileInputStream(file));
	    for (int i = 0; i < columns[0].length; i++) {
	        table.addColumns(fillCols(columns[0][i], columns[1][i]));
	        for (int j = 0; j < table.getEntryCount(); j++) {
	            table.getEntryBuilder(j).addInformation("null");
	        }
	    }
	    FileOutputStream output = new FileOutputStream(file);
	    table.build().writeTo(output);
	    output.close();
	}

	@Override
	public void dropColumn(String[] columns, String filePath) throws FileNotFoundException, IOException {
	    File file = makeFile(getFullPath(filePath));
	    Table.Builder table = Table.newBuilder();
	    table.mergeFrom(new FileInputStream(file));
	    boolean[] isRem = new boolean[table.getColumnsCount()];
	    for (int i = table.getColumnsCount() - 1; i >= 0; i--) {
	        boolean remove = false;
	        for (int j = 0; j < columns.length && !remove; j++) {
	            if (table.getColumns(i).getColName().equals(columns[j])) {
	                remove = true;
	            }
	        }
	        if (remove)
	            table.removeColumns(i);
	        isRem[i] = remove;
	    }
	    for (int i = table.getEntryCount() - 1; i >= 0; i--) {
	        Entry.Builder entry = Entry.newBuilder();
	        for (int j = 0; j < table.getEntry(i).getInformationCount(); j++) {
	            if (!isRem[j]) {
	                entry.addInformation(table.getEntry(i).getInformation(j));
	            }
	        }
	        table.addEntry(i, entry);
	        table.removeEntry(i + 1);
	    }
	    FileOutputStream output = new FileOutputStream(file);
	    table.build().writeTo(output);
	    output.close();
	}

	@Override
	public String[] getColumnNames(String filePath) throws FileNotFoundException, IOException {
	    File file = makeFile(getFullPath(filePath));
	    String[] colNames = null;
	    Table table = Table.parseFrom(new FileInputStream(file));
	    int sz = table.getColumnsCount();
	    colNames = new String[sz];
	    for (int i = 0; i < sz; i++) {
	        colNames[i] = table.getColumns(i).getColName();
	    }
	    return colNames;
	}

	@Override
	public String[] getColumnTypes(String filePath) throws FileNotFoundException, IOException {
	    File file = makeFile(getFullPath(filePath));
	    String[] colNames = null;
	    Table table = Table.parseFrom(new FileInputStream(file));
	    int sz = table.getColumnsCount();
	    colNames = new String[sz];
	    for (int i = 0; i < sz; i++) {
	        colNames[i] = table.getColumns(i).getColType();
	    }
	    return colNames;
	}

	@Override
	public void addRow(String filePath, String columns[], String values[]) throws FileNotFoundException, IOException {
	    File file = makeFile(getFullPath(filePath));
	    Table.Builder table = Table.newBuilder();
	    table.mergeFrom(new FileInputStream(file));
	    Entry.Builder entry = Entry.newBuilder();
	    for (int i = 0; i < table.getColumnsCount(); i++) {
	        int colIndex = getColIndex(table.getColumns(i).getColName(), columns);
	        if (colIndex != NOTFOUND && values[colIndex] != null) {
	            entry.addInformation(values[colIndex]);
	        } else {
	            entry.addInformation("null");
	        }
	    }
	    table.addEntry(entry.build());
	    FileOutputStream output = new FileOutputStream(file);
	    table.build().writeTo(output);
	    output.close();
	}

	@Override
	public int getRowCount(String filePath) throws FileNotFoundException, IOException {
	    File file = makeFile(getFullPath(filePath));
	    Table table = Table.parseFrom(new FileInputStream(file));
	    return table.getEntryCount();
	}

	@Override
	public LinkedList<String> getRow(String filePath, int rowNumber) throws FileNotFoundException, IOException {
	    LinkedList<String> reqRow = new LinkedList<String>();
	    File file = makeFile(getFullPath(filePath));
	    Table table = Table.parseFrom(new FileInputStream(file));
	    if (rowNumber >= table.getEntryCount()) {
	        throw new RuntimeException();
	    }
	    for (int i = 0; i < table.getEntry(rowNumber).getInformationCount(); i++) {
	        reqRow.add(table.getEntry(rowNumber).getInformation(i));
	    }
	    return reqRow;
	}

	@Override
	public void updateRow(String filePath, String data[], int rowNumber)
	        throws FileNotFoundException, IOException, RuntimeException {
	    File file = makeFile(getFullPath(filePath));
	    Table.Builder table = Table.newBuilder();
	    table.mergeFrom(new FileInputStream(file));
	    if (rowNumber >= table.getEntryCount()) {
	        throw new RuntimeException();
	    }
	    Entry.Builder entry = Entry.newBuilder();
	    for (int i = 0; i < data.length && i < table.getColumnsCount(); i++) {
	        entry.addInformation(data[i]);
	    }
	    table.addEntry(rowNumber, entry.build());
	    table.removeEntry(rowNumber + 1);
	    FileOutputStream output = new FileOutputStream(file);
	    table.build().writeTo(output);
	    output.close();
	}

	@Override
	public void deleteRow(String filePath, int rowNumber) throws FileNotFoundException, IOException {
	    File file = makeFile(getFullPath(filePath));
	    Table.Builder table = Table.newBuilder();
	    table.mergeFrom(new FileInputStream(file));
	    if (rowNumber >= table.getEntryCount()) {
	        throw new RuntimeException();
	    }
	    table.removeEntry(rowNumber);
	    FileOutputStream output = new FileOutputStream(file);
	    table.build().writeTo(output);
	    output.close();
	}

	private String getFullPath(String path) {
        return path + TXTEX;
    }

    private boolean initColsToFile(String filePath, String[] colNames, String[] colTypes) {
        File file = new File(filePath);
        Table.Builder table = Table.newBuilder();
        try {
            table.mergeFrom(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
        for (int i = 0; i < colNames.length; i++) {
            table.addColumns(fillCols(colNames[i], colTypes[i]));
        }
        try {
            FileOutputStream output = new FileOutputStream(file);
            table.build().writeTo(output);
            output.close();
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    private ColInformation fillCols(String name, String type) {
        ColInformation.Builder colInf = ColInformation.newBuilder();
        colInf.setColName(name);
        colInf.setColType(type);
        return colInf.build();
    }

    private boolean initializeFile(String filePath) {
        File file = new File(filePath);
        try {
            file.createNewFile();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    private File makeFile(String path) {
        File file = new File(path);
        return file;
    }

    private int getColIndex(String colName, String[] columns) {
        for (int i = 0; i < columns.length; i++) {
            if (colName.equals(columns[i])) {
                return i;
            }
        }
        return NOTFOUND;
    }
}
