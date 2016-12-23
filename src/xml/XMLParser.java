package xml;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import backendParsers.Parser;

public class XMLParser implements Parser {

	private DocumentBuilderFactory docFactory;
	private DocumentBuilder docBuilder;
	private TransformerFactory transformerFactory;
	private Transformer transformer;
	private static final String XML_EXTENSION = ".xml";
	private static final String INDENT_TRANSFORM = "{http://xml.apache.org/xslt}indent-amount";

	public XMLParser() throws ParserConfigurationException, TransformerConfigurationException {
		docFactory = DocumentBuilderFactory.newInstance();
		docBuilder = docFactory.newDocumentBuilder();
		transformerFactory = TransformerFactory.newInstance();
		transformer = transformerFactory.newTransformer();
	}

	@Override
	public void createFile(String tableName, String filePath, String colNames[], String types[])
			throws SAXException, IOException, TransformerException {
		filePath = addExtension(filePath);
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement(tableName);
		doc.appendChild(rootElement);
		saveXml(doc, filePath);
		addColumnTypes(filePath, colNames, types);
	}

	@Override
	public void addColumn(String[][] columns, String filePath) throws SAXException, IOException, TransformerException {
		changeColTags(columns, filePath);
		filePath = addExtension(filePath);
		Document doc = docBuilder.parse(filePath);
		addInEachRow(columns, doc);
		saveXml(doc, filePath);
	}

	@Override
	public void dropColumn(String[] columns, String filePath) throws SAXException, IOException, TransformerException {
		String[][] newColumns = deleteColumns(filePath, columns);
	
		filePath = addExtension(filePath);
		Document doc = docBuilder.parse(filePath);
	
		deleteRows(doc, columns);
		saveXml(doc, filePath);
	
		addColumnTypes(filePath, newColumns[0], newColumns[1]);
	}

	@Override
	public void addRow(String filePath, String columns[], String values[])
			throws SAXException, IOException, TransformerException {
		filePath = addExtension(filePath);
		Document doc = docBuilder.parse(filePath);
		Element rootElement = (Element) doc.getChildNodes().item(1);
		Element row = doc.createElement("row");
		rootElement.appendChild(row);
		for (int i = 0; i < columns.length; i++) {
			Element colValue = doc.createElement(columns[i]);
			if (values[i] != null) {
				colValue.appendChild(doc.createTextNode(values[i]));
			}
			row.appendChild(colValue);
		}
		saveXml(doc, filePath);
	}

	@Override
	public int getRowCount(String filePath) throws SAXException, IOException {
		filePath = addExtension(filePath);
		Document doc = docBuilder.parse(filePath);
		int number = doc.getElementsByTagName("row").getLength();
		return number;
	}

	@Override
	public LinkedList<String> getRow(String filePath, int rowNumber) throws SAXException, IOException {
		filePath = addExtension(filePath);
		Document doc = docBuilder.parse(filePath);
		Node row = doc.getElementsByTagName("row").item(rowNumber);
		Node col;
		LinkedList<String> data = new LinkedList<String>();
		for (int i = 0; i < row.getChildNodes().getLength(); i++) {
			col = row.getChildNodes().item(i);
			if (col.getNodeType() == Node.ELEMENT_NODE) {
				data.add(col.getTextContent());
			}
		}
		return data;
	}

	@Override
	public void updateRow(String filePath, String data[], int rowNumber)
			throws SAXException, IOException, TransformerException {
		filePath = addExtension(filePath);
		Document doc = docBuilder.parse(filePath);
		Node row = doc.getElementsByTagName("row").item(rowNumber);
		Node col;
		for (int i = 0, j = 0; j < row.getChildNodes().getLength(); j++) {
			col = row.getChildNodes().item(j);
			if (col.getNodeType() == Node.ELEMENT_NODE) {
				if (data[i] != null) {
					col.setTextContent(data[i]);
				}
				i++;
			}
		}
		saveXml(doc, filePath);
	}

	@Override
	public void deleteRow(String filePath, int rowNumber) throws SAXException, IOException, TransformerException {
		filePath = addExtension(filePath);
		Document doc = docBuilder.parse(filePath);
		Node row = doc.getElementsByTagName("row").item(rowNumber);
		row.getParentNode().removeChild(row);
		saveXml(doc, filePath);
	}

	@Override
	public String[] getColumnNames(String filePath) throws SAXException, IOException {
		filePath = addExtension(filePath);
		Document doc = docBuilder.parse(filePath);
		Element rootElement = (Element) doc.getChildNodes().item(1);
		String namesString = rootElement.getAttribute("colNames");
		return namesString.split(",");
	}

	@Override
	public String[] getColumnTypes(String filePath) throws SAXException, IOException {
		filePath = addExtension(filePath);
		Document doc = docBuilder.parse(filePath);
		Element rootElement = (Element) doc.getChildNodes().item(1);
	
		String namesString = rootElement.getAttribute("types");
		return namesString.split(",");
	}

	private void saveXml(Document doc, String filePath) throws TransformerException {
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(filePath));
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(INDENT_TRANSFORM, "2");
		String tableName = getTableName(doc);
		transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, tableName + ".dtd");
		transformer.transform(source, result);
	}

	private String addExtension(String path) {
		return path + XML_EXTENSION;
	}

	private void addColumnTypes(String filePath, String colNames[], String types[])
			throws SAXException, IOException, TransformerException {
		Document doc = docBuilder.parse(filePath);
		Element rootElement = (Element) doc.getChildNodes().item(1);
		rootElement.setAttribute("colNames", toString(colNames));
		rootElement.setAttribute("types", toString(types));

		saveXml(doc, filePath);
	}

	private String getTableName(Document doc) {
		for (int i = 0; i < doc.getChildNodes().getLength(); i++) {
			Node node = doc.getChildNodes().item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				return ((Element) node).getTagName();
			}
		}
		return null;
	}

	private String toString(String array[]) {
		StringBuilder string = new StringBuilder();
		for (int i = 0; i < array.length; i++) {
			string.append(array[i] + ",");
		}
		return string.toString();
	}

	private void addInEachRow(String[][] columns, Document doc) {
		NodeList allRows = doc.getElementsByTagName("row");
		for (int i = 0; i < allRows.getLength(); i++) {
			Node row = allRows.item(i);
			for (int j = 0; j < columns[0].length; j++) {
				Element col = doc.createElement(columns[0][j]);
				col.appendChild(doc.createTextNode("null"));
				row.appendChild(col);
			}
		}
	}

	private void changeColTags(String[][] columns, String filePath)
			throws SAXException, IOException, TransformerException {
		String oldColNames[] = getColumnNames(filePath);
		String oldColTypes[] = getColumnTypes(filePath);

		int len = oldColNames.length + columns.length;
		String newColumns[][] = new String[len][2];

		newColumns[0] = mergeArrays(oldColNames, columns[0]);
		newColumns[1] = mergeArrays(oldColTypes, columns[1]);

		filePath = addExtension(filePath);
		addColumnTypes(filePath, newColumns[0], newColumns[1]);
	}

	private String[] mergeArrays(String[] array1, String[] array2) {
		int len1 = array1.length;
		int len2 = array2.length;

		String[] newArray = new String[len1 + len2];

		System.arraycopy(array1, 0, newArray, 0, len1);
		System.arraycopy(array2, 0, newArray, len1, len2);

		return newArray;
	}

	private void deleteRows(Document doc, String[] columns) {
		for (int i = 0; i < columns.length; i++) {
			NodeList oldNodes = doc.getElementsByTagName(columns[i]);
			while (oldNodes.getLength() > 0) {
				Node node = oldNodes.item(0);
				Node prev = node.getPreviousSibling();
				if (prev != null && prev.getNodeType() == Node.TEXT_NODE && prev.getNodeValue().trim().length() == 0) {
					node.getParentNode().removeChild(prev);
				}
				node.getParentNode().removeChild(node);
			}
		}
	}

	private String[][] deleteColumns(String filePath, String columns[]) throws SAXException, IOException {
		String oldColNames[] = getColumnNames(filePath);
		String oldColTypes[] = getColumnTypes(filePath);
		LinkedList<String> newNames = new LinkedList<String>();
		LinkedList<String> newTypes = new LinkedList<String>();
		for (int i = 0; i < oldColNames.length; i++) {
			boolean delete = false;
			for (int j = 0; j < columns.length; j++) {
				if (oldColNames[i].equals(columns[j])) {
					delete = true;
				}
			}
			if (!delete) {
				newNames.add(oldColNames[i]);
				newTypes.add(oldColTypes[i]);
			}
		}
		int newLen = newNames.size();
		String[][] newArray = { newNames.toArray(new String[newLen]), newTypes.toArray(new String[newLen]) };
		return newArray;
	}

}