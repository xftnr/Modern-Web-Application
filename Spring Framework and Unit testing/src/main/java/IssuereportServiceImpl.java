import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.Queue;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class IssuereportServiceImpl implements IssuereportService{
	
	
	public int counting(String issueType) throws MalformedURLException, ParseException{
		
		//code from assignment 1
		URL fileName = new URL("http://www.cs.utexas.edu/~devdatta/traffic_incident_data.xml");
		String tpyerequest = "ds:issue_reported";
		// setup the Dom parser
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = builderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();  
		}

		Document document = null;
		try {
			document = builder.parse(fileName.openStream());
		}
		catch (SAXException e) {
		    e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		Element rootElement = document.getDocumentElement();
		Queue<Element> q = new LinkedList<Element>();
		q.add(rootElement);

		//query the database
		int result = 0;
		while(!q.isEmpty()) {
			Element e = (Element) q.remove();
			//find the node name first
			if (e.getNodeName().equalsIgnoreCase(tpyerequest)) {
				String nodeValue = e.getTextContent();
				//get the content value
				if(nodeValue.equalsIgnoreCase(issueType)){
					result++;
				}
			}
			NodeList nodes = e.getChildNodes();
			for(int i=0; i<nodes.getLength(); i++) {
				Node node = nodes.item(i);
				if(node instanceof Element) {
					q.add((Element) node);
				}
			}
		}
		return result;
	}
	
	public String risklevel(int count) {
		String result = "";
		if( 0 <= count && count <= 5) {
			result = "Rare";
		} else if(6 <= count && count <= 10) {
			result = "Very Low";
		} else if(11 <= count && count <= 15) {
			result = "Low";
		} else if(16 <= count && count <= 30) {
			result = "Medium";
		} else if(31 <= count && count <= 50) {
			result = "Moderate";
		} else if(51 <= count && count <= 100) {
			result = "High";
		} else if(101 <= count && count <= 200) {
			result = "Very High";
		} else {
			result = "Extreme";
		}
		
		return "Risk level: " + result;
	}
}
