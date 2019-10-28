package resources;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import domain.*;
import services.*;

@Path("/")
public class TrafficIncidentReportsResource {
	
	TrafficIncidentReportsService trafficIncidentReportsService;
	String dbname;
	String dbpassword;
	String dbusername;
	String dburl;
	String dbhost;
	
	public TrafficIncidentReportsResource(@Context ServletContext servletContext){
		dbhost = servletContext.getInitParameter("DBHOST");
		dbname = servletContext.getInitParameter("DBNAME");
		dbusername = servletContext.getInitParameter("DBUSERNAME");
		dbpassword = servletContext.getInitParameter("DBPASSWORD");
		dburl = "jdbc:mysql://" + dbhost + ":3306/" + dbname;
		// load the database
		this.trafficIncidentReportsService = new TrafficIncidentReportsService(dburl, dbusername, dbpassword);
	}
	
	@GET
	@Produces("text/html")
	public String welcome() {
		return "Welcome to Traffic Incident Reports REST API<br>"
				+"For All reports use: http://localhost:8080/assignment4/trafficincidentreports/reports<br>"
				+"For a specific report use: http://localhost:8080/assignment4/trafficincidentreports/reports/{traffic_report_id}";		
	}
	
	@POST
	@Path("/reports")
	@Produces("application/xml")
	public Object addCReport(TrafficIncidentReport reportToAdd) throws Exception {
		if(reportToAdd == null || reportToAdd.getAddress().length()<=0 || reportToAdd.getDate().length()<=0 || 
				reportToAdd.getReport().length()<=0 || reportToAdd.getzipcode().length()<=0) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}else {
			TrafficIncidentReport output = trafficIncidentReportsService.addReport(reportToAdd);
			if(output != null) {
				return output;
			}
		}
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	
	@GET
	@Path("/reports/{report_id}")
	@Produces("application/xml")
	public Object getSpecificReport(@PathParam("report_id") int reportId) throws Exception{
		if(reportId <= 0) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		else {
			System.out.println("XAXAXA");
			TrafficIncidentReport result = trafficIncidentReportsService.getindividualReport(reportId);
			if (result==null) {
				System.out.println("XAXAXA");
				return Response.status(Response.Status.BAD_REQUEST).build();
			}else {
				return result;
			}
			
		}
	}
	
	
	@DELETE
	@Path("/reports/{report_id}")
	public Response deleteReport(@PathParam("report_id") int reportId) throws Exception {
		if(reportId <= 0) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		try {
			
			if (trafficIncidentReportsService.deleteReport(reportId)) {	      
			      return Response.status(Response.Status.OK).build();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(Response.Status.NOT_FOUND).build();	
	}
	
	
	@PUT
	@Path("/reports/{report_id}")
	@Produces("application/xml")
	public Object updateCourse(@PathParam("report_id") int reportId, TrafficIncidentReport newreport) throws Exception {
		if(newreport == null || newreport.getAddress().length()<=0 || newreport.getDate().length()<=0 || 
				newreport.getReport().length()<=0 || newreport.getzipcode().length()<=0 || reportId<=0) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		else {
			TrafficIncidentReport result = trafficIncidentReportsService.updateReport(reportId, newreport);
			if (result == null) {	      
			      return Response.status(Response.Status.OK).build();
			}
			else {
				return result;
			}
		}
	}
	
	@GET
	@Path("/reports")
	@Produces("application/xml")
	public TrafficIncidentReports getReportFromZipcode(@QueryParam("zipcode") int zipcode) throws Exception {
		if(zipcode == 0) {
			List<TrafficIncidentReport> listofeach = this.trafficIncidentReportsService.getallReports();
			TrafficIncidentReports result = new TrafficIncidentReports();
			result.setReports(listofeach);
			return result;
		} else {
			List<TrafficIncidentReport> listofeach = this.trafficIncidentReportsService.getReportByZipCode(zipcode);
			TrafficIncidentReports result = new TrafficIncidentReports();
			result.setReports(listofeach);
			return result;
		}

	}
	

}
