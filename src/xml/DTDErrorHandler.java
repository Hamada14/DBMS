package xml;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class DTDErrorHandler implements ErrorHandler {

  public DTDErrorHandler(){
    
  }
  
  private void printError(String errorMsg){
    System.out.println("ERROR: " + errorMsg);
  }
  
  @Override
  public void error(SAXParseException e) throws SAXException {
    printError(e.getMessage());
    throw e;
  }

  public void fatalError(SAXParseException e) throws SAXException {
    printError(e.getMessage());
    throw e;
  }

  public void warning(SAXParseException e) throws SAXException {
    printError(e.getMessage());
  }
}
