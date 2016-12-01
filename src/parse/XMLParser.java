package parse;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class XMLParser {

  private DocumentBuilderFactory docFactory;
  private DocumentBuilder docBuilder;
  private TransformerFactory transformerFactory;
  private Transformer transformer;
  private static XMLParser xmlParserSingleton = null;
  private static final String XML_EXTENSION = ".xml";
  private static final String INDENT_TRANSFORM = "{http://xml.apache.org/xslt}indent-amount";

  private XMLParser() throws Exception {
    docFactory = DocumentBuilderFactory.newInstance();
    docBuilder = docFactory.newDocumentBuilder();
    transformerFactory = TransformerFactory.newInstance();
    transformer = transformerFactory.newTransformer();
  }

  public static XMLParser getInstance() throws Exception {
    if (xmlParserSingleton == null) {
      xmlParserSingleton = new XMLParser();
    }
    return xmlParserSingleton;
  }

  private String addExtension(String path) {
    return path + XML_EXTENSION;
  }

  public void creatXmlFile(String tableName, String filePath,
      String colNames[], String types[]) throws Exception {
    filePath = addExtension(filePath);
    Document doc = docBuilder.newDocument();
    Element rootElement = doc.createElement(tableName);
    doc.appendChild(rootElement);
    SaveXml(doc, filePath);
    addColTypes(filePath, colNames, types);
  }

  private void addColTypes(String filePath, String colNames[], String types[])
      throws Exception {
    Document doc = docBuilder.parse(filePath);
    Element rootElement = (Element) doc.getChildNodes().item(1);
    rootElement.setAttribute("colNames", toString(colNames));
    rootElement.setAttribute("types", toString(types));
    SaveXml(doc, filePath);
  }

  public String[] getColNames(String filePath) throws SAXException, IOException {
    filePath = addExtension(filePath);
    Document doc = docBuilder.parse(filePath);
    Element rootElement = (Element) doc.getChildNodes().item(1);
    String namesString = rootElement.getAttribute("colNames");
    return namesString.split(",");
  }

  public String[] getColTypes(String filePath) throws SAXException, IOException {
    filePath = addExtension(filePath);
    Document doc = docBuilder.parse(filePath);
    Element rootElement = (Element) doc.getChildNodes().item(1);

    String namesString = rootElement.getAttribute("types");
    return namesString.split(",");
  }

  private String toString(String array[]) {
    StringBuilder string = new StringBuilder();
    for (int i = 0; i < array.length; i++) {
      string.append(array[i] + ",");
    }
    return string.toString();
  }

  public void addRow(String filePath, String columns[], String values[])
      throws Exception {
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
    SaveXml(doc, filePath);
  }

  public void updateRow(String filePath, String data[], int rowNumber)
      throws Exception {
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
    SaveXml(doc, filePath);
  }

  public LinkedList<String> getRow(String filePath, int rowNumber)
      throws Exception {
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

  public int getRowsNumb(String filePath) throws SAXException, IOException {
    filePath = addExtension(filePath);
    Document doc = docBuilder.parse(filePath);
    int number = doc.getElementsByTagName("row").getLength();
    return number;
  }

  public void deleteRaw(String filePath, int rowNumber) throws Exception {
    filePath = addExtension(filePath);
    Document doc = docBuilder.parse(filePath);
    Node row = doc.getElementsByTagName("row").item(rowNumber);
    row.getParentNode().removeChild(row);
    SaveXml(doc, filePath);
  }

  private void SaveXml(Document doc, String filePath) throws Exception {
    DOMSource source = new DOMSource(doc);
    StreamResult result = new StreamResult(new File(filePath));
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    transformer.setOutputProperty(INDENT_TRANSFORM, "2");
    String tableName = getTableName(doc);
    transformer
        .setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, tableName + ".dtd");
    transformer.transform(source, result);
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
}