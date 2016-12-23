package xml;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class DTDParser {

  private static DTDParser dtdParserSingleton = null;
  private static final String ENCODING = "UTF-8";
  private static final String DTD_EXTENSION = ".dtd";
  
  private DTDParser() {

  }

  public static DTDParser getInstance() {
    if (dtdParserSingleton == null) {
      dtdParserSingleton = new DTDParser();
    }
    return dtdParserSingleton;
  }

  public void createDTD(String filePath, String tableName, String Columns[])
      throws Exception, UnsupportedEncodingException {
    filePath = filePath + DTD_EXTENSION;
    PrintWriter writer = new PrintWriter(filePath, ENCODING);
    writer.println("<!ELEMENT " + tableName + " (row*)>");
    writer.print("<!ELEMENT row (");
    for (int i = 0; i < Columns.length; i++) {
      writer.print(Columns[i]);
      if (i != Columns.length - 1) {
        writer.print(",");
      }
    }
    writer.println(")>");
    for (int i = 0; i < Columns.length; i++) {
      writer.println("<!ELEMENT " + Columns[i] + " (#PCDATA)>");
    }
    writer.println("<!ATTLIST " + tableName + " colNames CDATA #REQUIRED>");
    writer.println("<!ATTLIST " + tableName + " types CDATA #REQUIRED>");
    writer.close();

  }

  public static boolean validateWithDTDU(String filePath)
      throws ParserConfigurationException, IOException {
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setValidating(true);
      factory.setNamespaceAware(true);
      DocumentBuilder builder = factory.newDocumentBuilder();
      builder.setErrorHandler(new DTDErrorHandler());
      builder.parse(new InputSource(filePath));
      return true;
    } catch (ParserConfigurationException pce) {
      throw pce;
    } catch (IOException io) {
      throw io;
    } catch (SAXException se) {
      return false;
    }
  }
}
