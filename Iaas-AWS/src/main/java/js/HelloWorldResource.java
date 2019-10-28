package js;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
@Path("/path")
public class HelloWorldResource {
	
	public HelloWorldResource() {
		
	}
	
//	test purpose 
	@GET
	@Path("/helloworld-resource")
	@Produces("text/html")
	public String helloWorld() {
		System.out.println("Inside helloworld");
		return "Hello world ";
	}
	
	@GET
	@Path("/get-count")
	@Produces("text/html")
	public String welcome() throws MalformedURLException {
		
		Map<String, Integer>  count = new HashMap<>();
		
//		dom parsing
		URL fileName = new URL("http://www.cs.utexas.edu/~devdatta/traffic_incident_data.xml");
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
		while(!q.isEmpty()) {
			Element e = (Element) q.remove();
			//find the each report in source1
			if (e.getNodeName().equalsIgnoreCase("dsbase:dx9v-zd7x")) {
				NodeList childenodes = e.getChildNodes();
				//set up the record
				for(int i =0; i<childenodes.getLength(); i++) {
					Node temp = childenodes.item(i);
					if(temp.getNodeName().equalsIgnoreCase("ds:issue_reported")){
						String reporttypename = temp.getTextContent();
						if(count.containsKey(reporttypename)) {
							count.put(reporttypename, count.get(reporttypename)+1);
						}
						else {
							count.put(reporttypename, 1);
						}
						break;
					}
				}

			}
			else {
				NodeList nodes = e.getChildNodes();
				for(int i=0; i<nodes.getLength(); i++) {
					Node node = nodes.item(i);
					if(node instanceof Element) {
						q.add((Element) node);
					}
				}
			}
		}
		
		//build the string from the map
		Set<String> keylist = count.keySet();
		StringBuilder result = new StringBuilder();
		for (String s: keylist) {
			result.append(s);
			result.append("+");
			result.append(count.get(s));
			result.append("+");
		}
		return result.toString();	
	}

}
