package backendParsers;

import java.io.IOException;
import java.util.LinkedList;

import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

/**
 * Back-end writer and parser interface.
 *
 */
public interface Parser {

	public void createFile(String tableName, String filePath, String colNames[], String types[])
			throws SAXException, IOException, TransformerException;

	public String[] getColumnNames(String filePath) throws SAXException, IOException;

	public String[] getColumnTypes(String filePath) throws SAXException, IOException;

	public void addRow(String filePath, String columns[], String values[])
			throws SAXException, IOException, TransformerException;

	public void updateRow(String filePath, String data[], int rowNumber)
			throws SAXException, IOException, TransformerException;

	public LinkedList<String> getRow(String filePath, int rowNumber) throws SAXException, IOException;

	public int getRowCount(String filePath) throws SAXException, IOException;

	public void deleteRow(String filePath, int rowNumber) throws SAXException, IOException, TransformerException;

	public void addColumn(String[][] columns, String filePath) throws SAXException, IOException, TransformerException;

	public void dropColumn(String[] columns, String filePath) throws SAXException, IOException, TransformerException;
}
