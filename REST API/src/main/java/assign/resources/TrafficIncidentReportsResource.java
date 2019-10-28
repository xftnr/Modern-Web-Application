package resources;
import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.WebApplicationException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import java.io.IOException;
import java.io.OutputStream;
import domain.*;
import domain.Error;
import services.*;

@Path("/")
public class TrafficIncidentReportsResource {
	
	TrafficIncidentReportsService trafficIncidentReportsService;
	
	public TrafficIncidentReportsResource(){
		this.trafficIncidentReportsService = new TrafficIncidentReportsService();
	}
	
	@GET
	@Produces("text/html")
	public String welcome() {
		return "Welcome to Traffic Incident Reports REST API<br>"
				+"For All reports use: http://localhost:8080/assignment3/trafficincidentreports/reports<br>"
				+"For a specific report use: http://localhost:8080/assignment3/trafficincidentreports/reports/{traffic_report_id}";		
	}
	
	@GET
	@Path("/reports")
	@Produces("application/xml")
	public StreamingOutput getAllReport() throws Exception {
		ArrayList<String> allReport = trafficIncidentReportsService.getallReports();
		final TrafficIncidentReports finalreports = new TrafficIncidentReports();
		ArrayList<TrafficIncidentReport> reportList = new ArrayList<TrafficIncidentReport>();
		for(String s: allReport) {
			TrafficIncidentReport trafficIncidentReport = new TrafficIncidentReport();
			trafficIncidentReport.setName(s);				
			reportList.add(trafficIncidentReport);					
		}

		finalreports.setReports(reportList);
	    return new StreamingOutput() {
			
			@Override
			public void write(OutputStream output) throws IOException, WebApplicationException {
				// TODO Auto-generated method stub
				outputReports(output, finalreports);
			}
		};
	}
	
	@GET
	@Path("/reports/{report_id}")
	@Produces("application/xml")
	public StreamingOutput getSpecificReport(@PathParam("report_id") String reportID) throws Exception{
		final TrafficIncidentReport finalreport = trafficIncidentReportsService.individualReport(reportID);
		if(finalreport.getid() == null) {
			final Error errorout = new Error();
			errorout.setName("Traffic report with id " + reportID + " does not exist");
			return new StreamingOutput() {
				
				@Override
				public void write(OutputStream output) throws IOException, WebApplicationException {
					// TODO Auto-generated method stub
					outputError(output, errorout);
				}
			};
		}
		return new StreamingOutput() {
			
			@Override
			public void write(OutputStream output) throws IOException, WebApplicationException {
				// TODO Auto-generated method stub
				outputReport(output, finalreport);
			}
		};
	}

	
	protected void outputReports(OutputStream os, TrafficIncidentReports trafficIncidentReports) throws IOException {
		try { 
			JAXBContext jaxbContext = JAXBContext.newInstance(TrafficIncidentReports.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	 
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(trafficIncidentReports, os);
		} catch (JAXBException jaxb) {
			jaxb.printStackTrace();
			throw new WebApplicationException();
		}
	}
	
	protected void outputReport(OutputStream os, TrafficIncidentReport trafficIncidentReport) throws IOException {
		try { 
			JAXBContext jaxbContext = JAXBContext.newInstance(TrafficIncidentReport.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	 
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(trafficIncidentReport, os);
		} catch (JAXBException jaxb) {
			jaxb.printStackTrace();
			throw new WebApplicationException();
		}
	}
	
	protected void outputError(OutputStream os, Error out) throws IOException {
		try { 
			JAXBContext jaxbContext = JAXBContext.newInstance(Error.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	 
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(out, os);
		} catch (JAXBException jaxb) {
			jaxb.printStackTrace();
			throw new WebApplicationException();
		}
	}

}
