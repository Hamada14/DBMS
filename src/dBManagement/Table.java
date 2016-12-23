package dBManagement;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import org.xml.sax.SAXException;

import com.protocol.buffers.ProtoBufParser;

import backendParsers.Parser;
import data.DataValidator;
import evaluator.BooleanEvaluator;
import json.JsonWriter;
import query.DBQuery;
import query.Query;
import query.SelectQuery;
import query.UpdateQuery;
import xml.DTDParser;
import xml.XMLParser;

public class Table {

	private static final int XMLPARSE = 1, PROTOBUF = 2, JSONPARESER = 3;

	private String name;
    private String path, tablePath;
    private String[] colName;
    private String[] colType;
    private LinkedList<String> types;
    private Parser parserController;

    public Table(String name, String filePath, String[] colName, String[] types, int parserType) throws Exception {
        this.name = name;
        path = filePath;
        tablePath = filePath;
        path = path + File.separator + name;
        for (int idx = 0; idx < colName.length; idx++) {
            colName[idx] = colName[idx].toLowerCase();
        }
        DTDParser.getInstance().createDTD(path, name, colName);
        parserController = getParserType(parserType);
        parserController.createFile(name, path, colName, types);
    }

    public String getPath() {
        return this.tablePath;
    }

    public Table(String name) throws Exception {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Query selectFromTable(String[] selectedCol, String condition, boolean distinct, String path, int parserType)
	        throws Exception {
	    parserController = getParserType(parserType);
	    colName = parserController.getColumnNames(path);
	    colType = parserController.getColumnTypes(path);
	    types = new LinkedList<String>();
	    SelectQuery query;
	    if ((!checkSelectedColumns(path, selectedCol, parserType) || (!checkCondition(condition, path, parserType)))
	            && (!selectedCol[0].equals("*"))) {
	        return new Query(false, "Invalid Query");
	    }
	    if (!distinct) {
	        if (selectedCol.length == 1 && selectedCol[0].equals("*"))
	            query = selectAll(condition, path, parserType);
	        else
	            query = selected(selectedCol, condition, path, parserType);
	    } else {
	        if (selectedCol.length == 1 && selectedCol[0].equals("*"))
	            query = distinctSelectAll(condition, path, parserType);
	        else
	            query = distinctSelect(selectedCol, condition, path, parserType);
	    }
	    query.setOperationDone();
	    return query;
	}

	public Query insertIntoTable(String[] columns, String[] values, String path, int parserType) throws Exception {
	    if (!checkSelectedColumns(path, columns, parserType)) {
	        return new Query(false, "column names is not correct");
	    }
	    parserController = getParserType(parserType);
	    String colNames[] = parserController.getColumnNames(path);
	    String colTypes[] = parserController.getColumnTypes(path);
	    String newValues[] = new String[colNames.length];
	    for (int i = 0; i < colNames.length; i++) {
	        for (int j = 0; j < columns.length; j++) {
	            if (colNames[i].equals(columns[j])) {
	                if (!checkType(colTypes[i], values[j])) {
	                    return new Query(false, "Table's data is not correct");
	                } else
	                    newValues[i] = new String(values[j]);
	            }
	        }
	    }
	    newValues = fixArray(newValues);
	    parserController.addRow(path, colNames, newValues);
	    DBQuery query = new DBQuery(true, "insertion query is executed");
	    return query;
	}

	public Query insertIntoTable(String[] values, String path, int parserType) throws Exception {
	    parserController = getParserType(parserType);
	    String colNames[] = parserController.getColumnNames(path);
	    if (values.length != colNames.length)
	        return new Query(false, "Make sure the table's data is written correctly");
	    parserController = getParserType(parserType);
	    String colTypes[] = parserController.getColumnTypes(path);
	    String newValues[] = new String[colNames.length];
	    UpdateQuery query = new UpdateQuery();
	    for (int i = 0; i < colNames.length; i++) {
	        if (!checkType(colTypes[i], values[i])) {
	            return new Query(false, "Make sure the table's data is written correctly");
	        } else
	            newValues[i] = new String(values[i]);
	    }
	    newValues = fixArray(newValues);
	    parserController.addRow(path, colNames, newValues);
	    query.incrementUpdateCounter();
	    query.setOperationDone();
	    return query;
	}

	public Query deleteFromTable(String condition, String path, int parserType) throws Exception {
	    if (!checkCondition(condition, path, parserType)) {
	        return new Query(false, "Invalid condition: " + condition);
	    }
	    parserController = getParserType(parserType);
	    UpdateQuery query = new UpdateQuery();
	    for (int i = 0; i < parserController.getRowCount(path); i++) {
	        Entry tmp = new Entry(parserController.getRow(path, i), parserController.getColumnNames(path));
	        try {
	            if (tmp.isValid(condition)) {
	                parserController.deleteRow(path, i);
	                query.incrementUpdateCounter();
	                i--;
	            }
	        } catch (Exception e) {
	            return new Query(false, "Invalid format");
	        }
	    }
	    query.setOperationDone();
	    query.setLogMessage("Deletion query is executed");
	    return query;
	}

	public Query updateTable(String[] keys, String[] content, String condition, String Path, int parserType)
	        throws Exception {
	    try {
	        if (!checkType(keys, content, Path, parserType)) {
	            return new Query(false, "Invalid data types");
	        } else if (!checkCondition(condition, Path, parserType)) {
	            return new Query(false, "Invalid condition: " + condition);
	        }
	        UpdateQuery query = new UpdateQuery();
	        for (int i = 0; i < parserController.getRowCount(Path); i++) {
	            Entry tmp = new Entry(parserController.getRow(Path, i), parserController.getColumnNames(Path));
	            if (tmp.isValid(condition)) {
	                tmp.update(keys, content);
	                parserController.updateRow(Path, tmp.getEntryAsList().toArray(new String[tmp.cells.size()]), i);
	                query.incrementUpdateCounter();
	            }
	        }
	        query.setOperationDone();
	        query.setLogMessage("Update query is executed");
	        return query;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return new Query(false, "Invalid format");
	    }
	}

	public Query alterTable(String[][] columns, String fullPath, int parserType) {
	    try {
	        parserController = getParserType(parserType);
	        if (columns[1][0] != null) {
	            if (!checkAddedColumns(fullPath, columns[0], parserType))
	                return new Query(false, "Invalid columns names");
	            parserController.addColumn(columns, fullPath);
	        } else
	        {
	            if(!checkSelectedColumns(fullPath, columns[0], parserType))
	                return new Query(false, "Invalid columns names");
	            parserController.dropColumn(columns[0], fullPath);
	        }
	        DBQuery query = new DBQuery(true, "Altering query is executed");
	        query.setOperationDone();
	        return query;
	    } catch (Exception e) {
	        return new Query(false, "Altering table failed");
	    }
	}

	private SelectQuery distinctSelectAll(String condition, String path, int parserType) throws Exception {
	    Set<LinkedList<String>> set = new HashSet<LinkedList<String>>();
	    SelectQuery q = new SelectQuery(name, new LinkedList<String>(Arrays.asList(colName)),
	            new LinkedList<String>(Arrays.asList(colType)));
	    for (int i = 0; i < parserController.getRowCount(path); i++) {
	        Entry tmp = new Entry(parserController.getRow(path, i), parserController.getColumnNames(path));
	        try {
	            if (tmp.isValid(condition)) {
	                LinkedList<String> crntEntry = tmp.getEntryAsList();
	                set.add(crntEntry);
	            }
	        } catch (Exception e) {
	        }
	    }
	    for (LinkedList<String> crnt : set) {
	        Entry entry = new Entry(crnt, colName);
	        q.addEntry(entry);
	    }
	    q.setOperationDone();
	    return q;
	}

	private SelectQuery distinctSelect(String[] selectedCol, String condition, String path, int parserType)
	        throws Exception {
	    SelectQuery q = new SelectQuery(name, new LinkedList<String>(Arrays.asList(selectedCol)), types);
	    Set<LinkedList<String>> set = new HashSet<LinkedList<String>>();
	    parserController = getParserType(parserType);
	    for (int i = 0; i < colName.length; i++) {
	        if (Arrays.asList(selectedCol).contains(colName[i]))
	            types.add(colType[i]);
	    }
	    int rowNumber = parserController.getRowCount(path);
	    for (int i = 0; i < rowNumber; i++) {
	        Entry tmp = new Entry(parserController.getRow(path, i), parserController.getColumnNames(path));
	        try {
	            if (tmp.isValid(condition)) {
	                LinkedList<String> crntEntry = tmp.getEntry(selectedCol);
	                set.add(crntEntry);
	            }
	        } catch (Exception e) {

	        }
	    }
	    for (LinkedList<String> crnt : set) {
	        Entry entry = new Entry(crnt, selectedCol);
	        q.addEntry(entry);
	    }
	    q.setOperationDone();
	    return q;
	}

	private SelectQuery selectAll(String condition, String path, int parserType) throws Exception {
	    SelectQuery q = new SelectQuery(name, new LinkedList<String>(Arrays.asList(colName)),
	            new LinkedList<String>(Arrays.asList(colType)));
	    for (int i = 0; i < parserController.getRowCount(path); i++) {
	        Entry tmp = new Entry(parserController.getRow(path, i), parserController.getColumnNames(path));
	        try {
	            if (tmp.isValid(condition))
	                q.addEntry(tmp);
	        } catch (Exception e) {
	        }
	    }
	    q.setOperationDone();
	    return q;
	}

	private SelectQuery selected(String[] selectedCol, String condition, String path, int parserType)
	        throws Exception {
	    SelectQuery q = new SelectQuery(name, new LinkedList<String>(Arrays.asList(selectedCol)), types);
	    parserController = getParserType(parserType);
	    for (int i = 0; i < colName.length; i++) {
	        if (Arrays.asList(selectedCol).contains(colName[i]))
	            types.add(colType[i]);
	    }
	    for (int i = 0; i < parserController.getRowCount(path); i++) {
	        Entry tmp = new Entry(parserController.getRow(path, i), parserController.getColumnNames(path));
	        try {
	            if (tmp.isValid(condition)) {
	                Entry entry = new Entry(tmp, selectedCol);
	                q.addEntry(entry);
	            }
	        } catch (Exception e) {
	            return new SelectQuery();
	        }
	    }
	    q.setOperationDone();
	    return q;
	}

	private boolean checkType(String type, String value) {
        return DataValidator.getInstance().assertType(type, value);
    }

    private boolean checkType(String key[], String[] values, String path, int parserType)
	        throws SAXException, IOException, Exception {
	    parserController = getParserType(parserType);
	    String[] colTypes = parserController.getColumnTypes(path);
	    String[] colNames = parserController.getColumnNames(path);
	    for (int i = 0, j = 0; i < values.length; i++) {
	        for (; j < colNames.length; j++) {
	            if (key[i].equals(colNames[j])) {
	                if (!checkType(colTypes[j], values[i])) {
	                    return false;
	                } else
	                    break;
	            }
	        }
	        if (j == colNames.length) {
	            return false;
	        }
	    }
	    return true;
	}

	private boolean checkSelectedColumns(String filePath, String selectedCol[], int parserType)
            throws SAXException, IOException, Exception {
        parserController = getParserType(parserType);
        String colNames[] = parserController.getColumnNames(filePath);
        boolean found;
        for (int i = 0; i < selectedCol.length; i++) {
            found = Arrays.asList(colNames).contains(selectedCol[i].toLowerCase());
            if (!found) {
                return false;
            }
        }
        return true;
    }

	private boolean checkAddedColumns(String filePath, String addedCol[], int parserType)
            throws SAXException, IOException, Exception {
        parserController = getParserType(parserType);
        String colNames[] = parserController.getColumnNames(filePath);
        boolean found;
        for (int i = 0; i < addedCol.length; i++) {
            found = Arrays.asList(colNames).contains(addedCol[i].toLowerCase());
            if (found)
                return false;
        }
        return true;
    }

    private boolean checkCondition(String condition, String path, int parserType)
            throws SAXException, IOException, Exception {
        if (condition == null)
            return true;
        parserController = getParserType(parserType);
        String[] colName = parserController.getColumnNames(path);
        String[] colType = parserController.getColumnTypes(path);
        BooleanEvaluator bes = BooleanEvaluator.getInstance();
        condition = " " + condition + " ";
        condition = condition.replaceAll("(?i) and (?=([^\"']*[\"'][^\"']*[\"'])*[^\"']*$)", " & ");
        condition = condition.replaceAll("(?i) or (?=([^\"']*[\"'][^\"']*[\"'])*[^\"']*$)", " | ");
        condition = condition.replaceAll("(?i) not (?=([^\"']*[\"'][^\"']*[\"'])*[^\"']*$)", " ! ");
        for (int idx = 0; idx < colName.length; idx++) {
            String curIdentity = colName[idx];
            String curValue = DataValidator.getInstance().getTestValue(colType[idx]);
            condition = condition.replaceAll("(?i) " + curIdentity + "(?=([^\"']*[\"'][^\"']*[\"'])*[^\"']*$)",
                    curValue);
        }
        try {
        	bes.isValid(condition);
        } catch (Exception e) {
        	return false;
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

    private Parser getParserType(int parserType) {
        switch (parserType) {
        case XMLPARSE:
            try {
                return new XMLParser();
            } catch (TransformerConfigurationException e) {
                System.out.println("Invalid Parser");
            } catch (ParserConfigurationException e) {
                System.out.println("Invalid Parser");
            }
        case PROTOBUF:
            return new ProtoBufParser();
        case JSONPARESER:
            return new JsonWriter();
        default:
            return null;
        }
    }
}