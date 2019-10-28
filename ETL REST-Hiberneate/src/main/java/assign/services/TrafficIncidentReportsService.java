package services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import domain.Report;;

public class TrafficIncidentReportsService {

	private SessionFactory sessionFactory;
	
	public TrafficIncidentReportsService() {
		//init the info
		sessionFactory = new Configuration()
                .configure() // configures settings from hibernate.cfg.xml
                .buildSessionFactory();
		String url1 = "http://www.cs.utexas.edu/~devdatta/traffic_incident_data.xml";
		String url2 = "http://www.cs.utexas.edu/~devdatta/traffic_incident_with_zipcodes.xml";
		try {
			Map<String, String> dic = getzipcodemap(url2);
			mergesource(url1, url2, dic);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void mergesource (String url1, String url2, Map<String, String> dic) throws Exception{
		
//		dom parsing
		URL fileName = new URL(url1);
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
				String date = null;
				String reported = null;
				String address = null;
				String reportId = e.getAttribute("rdf:about");
				NodeList childenodes = e.getChildNodes();
				//set up the record
				for(int i =0; i<childenodes.getLength(); i++) {
					Node temp = childenodes.item(i);
					switch (temp.getNodeName()) {
						case "ds:published_date":
							date = temp.getTextContent();
							break;
						case "ds:issue_reported":
							reported = temp.getTextContent();
							break;
						case "ds:address":
							address = temp.getTextContent();
							break;
					}
				}
				//find zipcode in resource2
				String zipcode = dic.get(address);
				//put in database
				addReport(date, reported, address, zipcode);

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
	}
	
	public void addReport(String date, String issue, String address, String zipcode) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		Report newTrafficIncidentReport= null;
		try {
			tx = session.beginTransaction();
			newTrafficIncidentReport = new Report(date, issue, address, zipcode);
			session.save(newTrafficIncidentReport);
		    tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
				throw e;
			}
		}
		finally {
			session.close();
		}
	}
	
	public Map<String, String> getzipcodemap(String url2)throws MalformedURLException {
		Map<String, String> result = new HashMap<>();
		URL fileName = new URL(url2);
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
			if (e.getNodeName().equalsIgnoreCase("dsbase:dx9v-zd7x")) {
				String zipcode = null;
				String address = null;
				NodeList childenodes = e.getChildNodes();
				for(int i =0; i<childenodes.getLength(); i++) {
					Node temp = childenodes.item(i);
					switch (temp.getNodeName()) {
						case "ds:zipcode":
							zipcode = temp.getTextContent();
							break;
						case "ds:address":
							address = temp.getTextContent();
							break;
					}
				}
				result.put(address, zipcode);
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
		return result;
	}
	
	
	//count
	public int getcount(String countype, String content) throws Exception {
		Session session = sessionFactory.openSession();		
		session.beginTransaction();
		Criteria criteria = session.createCriteria(Report.class).
        		add(Restrictions.eq(countype, content));
		if (criteria!=null) {
			List<Report> result = criteria.list();
			return result.size();
		}
		return 0;
		
	}
	

}
