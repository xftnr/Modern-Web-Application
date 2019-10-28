package resources;
import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


import domain.Error;
import domain.TrafficIncidentReport;

public class TestTrafficIncidentReportsResource {
	
	   private HttpClient client;

	   @Before
	   public void initClient()
	   {
		   client = HttpClientBuilder.create().build();
	   }
	   
	   @Test
	   public void testAllTrafficReport() throws Exception
	   {
	      System.out.println("**** Testing testAllTrafficReport ***");
	      
	      String url = "http://localhost:8080/assignment3/trafficincidentreports/reports";	
	      // Code snippet taken from https://www.mkyong.com/java/apache-httpclient-examples/
	      HttpGet request = new HttpGet(url);
	      HttpResponse response = client.execute(request);

	      System.out.println("Response Code : "
	              + response.getStatusLine().getStatusCode());

		  BufferedReader rd = new BufferedReader(
				new InputStreamReader(response.getEntity().getContent()));
			
		  StringBuffer result = new StringBuffer();
		  String line = "";
		  while ((line = rd.readLine()) != null) {
			  result.append(line.replaceAll("\\s",""));
		  }
		  
		  // Create an XML from this string result, then can parse them and then compare these two XML (this v.s original one)
	      String sourceURL = "http://www.cs.utexas.edu/~devdatta/traffic_incident_data.xml";
	      ArrayList<String> sourceReport = getFromOriginAllReports(sourceURL);
	      ArrayList<String> restReport = getRestReport(url);
	      assertEquals(sourceReport, restReport);

	   }  
	   
	   @Test
	   public void testSpecificReport() throws Exception{
		   System.out.println("**** Testing testAllTrafficReport ***");
		   String id = "C163BCD1CF90C984E9EDA4DBA311BCA369A7D1A1_1528871759";
		   String url = "http://localhost:8080/assignment3/trafficincidentreports/reports/" + id;
		   String sourceURL = "http://www.cs.utexas.edu/~devdatta/traffic_incident_data.xml";
		   HttpGet request = new HttpGet(url);
		   HttpResponse response = client.execute(request);

		   System.out.println("Response Code : "+ response.getStatusLine().getStatusCode());

		   BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));		
		   StringBuffer result = new StringBuffer();
		   String line = "";
		   while ((line = rd.readLine()) != null) {
		 	  result.append(line.replaceAll("\\s",""));
		   }		 
		   
		   TrafficIncidentReport sourceReport = individualReport(sourceURL, id);
		   
		   boolean found = true;
		   if(sourceReport.getid() == null) {
			   found = false;
		   }
		   Error restError = new Error();

		   TrafficIncidentReport restReport = individualReport(url, id);
		   if(restReport.getid() == null) {
			   restError.setName("Traffic report with id " + id + " does not exis");
		   }
		   
		   if(found) {
			   assertEquals(sourceReport.getid(), restReport.getid());
			   assertEquals(sourceReport.getAddress(), restReport.getAddress());
			   assertEquals(sourceReport.getReport(), restReport.getReport());
			   assertEquals(sourceReport.getLatitude(), restReport.getLatitude());
			   assertEquals(sourceReport.getLongitude(), restReport.getLongitude());
			   assertEquals(sourceReport.getDate(), restReport.getDate());
		   }else {
			   String error = "Traffic report with id " + id + " does not exis";
			   assertEquals(error, restError.getName());
		   }
	   }  
		
		public TrafficIncidentReport individualReport(String url, String id) throws MalformedURLException{
			
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
	   
	   

	   private ArrayList<String> getRestReport(String sourceURL) throws Exception{
			ArrayList<String> result = new ArrayList<>();		
			URL fileName = new URL(sourceURL);
			// Dompare from code provide. 
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
			// get the first element from root and pass in to queue 
			Element rootElement = document.getDocumentElement();
			Queue<Element> queue = new LinkedList<Element>();
			queue.add(rootElement);
			while(!queue.isEmpty()) {
				Element e = (Element) queue.remove();	
				// open tag and check if it's not what i want, check it's children put it in queue
				NodeList nodes = e.getChildNodes();
				for(int i=0; i<nodes.getLength(); i++) {
					Node node = nodes.item(i);
					if(node instanceof Element) {
						queue.add((Element) node);
					}
				}		
				
				if (e.getNodeName().equalsIgnoreCase("traffic_report_id")) {
					String valueForId = e.getTextContent();
					result.add(valueForId);
				}			
			}
			return result;
	   }
	   
	   private ArrayList<String> getFromOriginAllReports(String sourceURL) throws Exception {
			ArrayList<String> result = new ArrayList<>();		
			URL fileName = new URL(sourceURL);
			// Dompare from code provide. 
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
			// get the first element from root and pass in to queue 
			Element rootElement = document.getDocumentElement();
			Queue<Element> queue = new LinkedList<Element>();
			queue.add(rootElement);
			while(!queue.isEmpty()) {
				Element e = (Element) queue.remove();	
				// open tag and check if it's not what i want, check it's children put it in queue
				NodeList nodes = e.getChildNodes();
				for(int i=0; i<nodes.getLength(); i++) {
					Node node = nodes.item(i);
					if(node instanceof Element) {
						queue.add((Element) node);
					}
				}		
				
				if (e.getNodeName().equalsIgnoreCase("ds:traffic_report_id")) {
					String valueForId = e.getTextContent();
					result.add(valueForId);
				}			
			}
			return result;
	}
	   
}
