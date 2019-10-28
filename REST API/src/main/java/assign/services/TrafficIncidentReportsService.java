package services;
import java.util.ArrayList;
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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import domain.*;
public class TrafficIncidentReportsService {
	
	public ArrayList<String> getallReports() throws MalformedURLException{
		ArrayList<String> result = new ArrayList<>();
		
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
			if (e.getNodeName().equalsIgnoreCase("ds:traffic_report_id")) {
				String reportId = e.getTextContent();
				result.add(reportId);

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
	
	public TrafficIncidentReport individualReport(String id) throws MalformedURLException{
		
		TrafficIncidentReport target = new TrafficIncidentReport();
		
		
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
		NodeList targetNode = null;
		//query the database
		while(!q.isEmpty()) {
			Element e = (Element) q.remove();			
			if (e.getNodeName().equalsIgnoreCase("dsbase:dx9v-zd7x")) {
				NodeList childnodes = e.getChildNodes();
				for(int j=0; j<childnodes.getLength(); j++) {
					Node temp = childnodes.item(j);
					if (temp.getNodeName().equalsIgnoreCase("ds:traffic_report_id") && temp.getTextContent().equalsIgnoreCase(id)) {
						targetNode = childnodes;
						break;
					}
				}
				if (targetNode != null) {
					for(int k=0; k<targetNode.getLength(); k++) {
						Node temp = targetNode.item(k);
						switch (temp.getNodeName()) {
							case "ds:traffic_report_id":
								target.setName(temp.getTextContent());
								break;
							case "ds:published_date":
								target.setDate(temp.getTextContent());
								break;
							case "ds:issue_reported":
								target.setReport(temp.getTextContent());
								break;
							case "ds:latitude":
								target.setLatitude(temp.getTextContent());
								break;
							case "ds:longitude":
								target.setLongitude(temp.getTextContent());
								break;
							case "ds:address":
								target.setAddress(temp.getTextContent());
								break;
						}
					}
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
		return target;
	}

}
