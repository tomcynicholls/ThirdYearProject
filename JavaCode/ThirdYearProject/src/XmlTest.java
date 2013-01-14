import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
 
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
 
public class XmlTest {
 
	public static void main(String argv[]) {
 
	  try {
 
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
 
		// root elements
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("xmlmessage");
		doc.appendChild(rootElement);
 
		// message elements
		Element message = doc.createElement("message");
		rootElement.appendChild(message);
 
		// set attribute to message element
		Attr attr = doc.createAttribute("id");
		attr.setValue("1");
		message.setAttributeNode(attr);
 
		// shorten way
		// staff.setAttribute("id", "1");
 
		// sender elements
		Element sender = doc.createElement("sender");
		sender.appendChild(doc.createTextNode("me"));
		message.appendChild(sender);
 
		// receiver elements
		Element receiver = doc.createElement("receiver");
		receiver.appendChild(doc.createTextNode("them"));
		message.appendChild(receiver);

				
		// text elements
		Element text = doc.createElement("text");
		text.appendChild(doc.createTextNode("blah"));
		message.appendChild(text);
 
		// other elements
		Element other = doc.createElement("other");
		other.appendChild(doc.createTextNode("19191919191"));
		message.appendChild(other);
 
		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File("C:\\Users/Tom/xmlfile.xml"));
 
		// Output to console for testing
		// StreamResult result = new StreamResult(System.out);
 
		transformer.transform(source, result);
 
		System.out.println("File saved!");
 
	  } catch (ParserConfigurationException pce) {
		pce.printStackTrace();
	  } catch (TransformerException tfe) {
		tfe.printStackTrace();
	  }
	}
}