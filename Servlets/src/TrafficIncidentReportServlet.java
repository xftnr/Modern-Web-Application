import java.text.ParseException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.net.MalformedURLException;
import java.net.URL;

//import javax.lang.model.util.Elements;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class TrafficIncidentReportServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static ArrayList<String> listofurls = new ArrayList<String>();
	private static Boolean sessionStart = false;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException
	{
		PrintWriter writer = response.getWriter();

		String session = request.getParameter("session");
		String date = request.getParameter("published_date");
		String report = request.getParameter("issue_reported");
		
		
		// set the cookie
		Cookie cookies [] = request.getCookies();
		
		//check constraints
		if(session != null) {
			if(!session.equalsIgnoreCase("start") && !session.equalsIgnoreCase("end")) {
				writer.println("Error Message:");
				writer.println("You must enter \"start\" or \"end\" for session");
				return;
			}	
		}

		
		//constraints 2 only one parameter should be passed in one request
		if(date!=null && report != null) {
			writer.println("Error Message:");
			writer.print("Error. Pass only one parameter");
			return;
		}

		// We will get only one cookie 
		for(int i=0; cookies != null && i<cookies.length; i++) {
			Cookie ck = cookies[i];
			String cookieName = ck.getName();
			String cookieValue = ck.getValue();
//			writer.println(cookieName);
			if (sessionStart && (cookieName != null && cookieName.equals("Session")) 
					&& cookieValue != null && cookieValue.equals("Start")) {
				if(request.getQueryString()!=null) {
					listofurls.add(request.getRequestURL().toString() + "?"+ request.getQueryString());
				}
				//print out all history and clear the list
				if(session != null && session.equals("end")) {
					sessionStart = false;
					writer.println("History");
					for (int j = 0; j < listofurls.size(); j++ ) {
						writer.println(listofurls.get(j));
					}
					writer.println("\nData\n");
					listofurls.clear();
					//be carefull here cookies may not delete the node
					ck.setMaxAge(0);
					// debug purpose 
					return;
				} 
				else if(date != null) {
					writer.println("History");
					for (int j = 0; j < listofurls.size(); j++ ) {
						writer.println(listofurls.get(j));
					}
					writer.println("\nData");
					try {
						writer.println("Number of issues on "+ date +": "+ Domparse("ds:published_date", date));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return;
				}
				
				else if(report != null) {
					writer.println("History");
					for (int j = 0; j < listofurls.size(); j++ ) {
						writer.println(listofurls.get(j));
					}
					writer.println("\nData");
					try {
						writer.println("Number of issues of type "+ report +": "+ Domparse("ds:issue_reported", report));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return;
				}
				else {
					writer.println("History");
					for (int j = 0; j < listofurls.size(); j++ ) {
						writer.println(listofurls.get(j));
					}
					writer.println("\nData");
					return;
				}
			}
		}
		// create the cookie
		if(session != null) {
			if (!sessionStart && session.equalsIgnoreCase("start")) {
				Cookie cookie = new Cookie("Session","Start");
				cookie.setDomain("localhost");
				cookie.setPath("/Assignment1" + request.getServletPath());
				//cookie will close when the session ends
				cookie.setMaxAge(1000);
				response.addCookie(cookie);
				sessionStart = true;
				listofurls.clear();
				writer.println("History");
				// if session start with other parameter only care session start
				listofurls.add(request.getRequestURL().toString() + "?session=start");
				writer.println(request.getRequestURL().toString() + "?session=start");
				writer.println("\nData\n");
				return;
			}
		}


		//not in session
		else if(date != null) {
			writer.println("History");
			writer.println(request.getRequestURL().toString() + "?"+request.getQueryString());
			writer.println("\nData");
			try {
				writer.println("Number of issues on "+ date +": "+ Domparse("ds:published_date", date));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
		
		else if(report != null) {
			writer.println("History");
			writer.println(request.getRequestURL().toString() + "?"+request.getQueryString());
			writer.println("\nData");
			try {
				writer.println("Number of issues of type "+ report +": "+ Domparse("ds:issue_reported", report));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
		else {
			writer.println("History");
			writer.println(request.getRequestURL().toString() + "?");
			writer.println("\nData");
			return;
		}

	}


	private int Domparse(String tpyerequest, String input) throws MalformedURLException, ParseException {
//		String fileName = "http://www.cs.utexas.edu/~devdatta/traffic_incident_data.xml";
		
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
		int result = 0;
		while(!q.isEmpty()) {
			Element e = (Element) q.remove();			
			if (e.getNodeName().equalsIgnoreCase(tpyerequest)) {
				String nodeValue = e.getTextContent();
				if(tpyerequest.equals("ds:published_date")) {
//					SimpleDateFormat sd1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
//					Date dt = sd1.parse(nodeValue);
//					SimpleDateFormat sd2 = new SimpleDateFormat("yyyy-MM-dd");
//					nodeValue = sd2.format(dt);
					nodeValue = nodeValue.substring(0, 10);
				}
				if(nodeValue.equalsIgnoreCase(input)) {
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

}


