package resources;

import java.net.MalformedURLException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import domain.Output;
import services.TrafficIncidentReportsService;

@Path("/")
public class TrafficIncidentReportsResource {
	
	private static TrafficIncidentReportsService trafficIncidentReportsService = new TrafficIncidentReportsService();
	
	public TrafficIncidentReportsResource() throws Exception{
	}
	 
	@GET
	@Produces("text/html")
	public String welcome() {
		return "Welcome to Traffic Incident Reports REST API.<br>"
				+"Following queries are supported by this REST API.<br>"
				+"<br>"
				+"For zipcodes: http://localhost:8080/assignment5/trafficincidentreports/reports/zipcode/<zipcode><br>"
				+"For published_date:<br>http://localhost:8080/assignment5/trafficincidentreports/reports/published_date/<published_date><br>"
				+"For issue_reported:<br>http://localhost:8080/assignment5/trafficincidentreports/reports/issue_reported/<issue_reported><br>"
				+"For address: http://localhost:8080/assignment5/trafficincidentreports/reports/address/<address>";		
	}
	
	@GET
	@Path("/reports/zipcode/{zipcode}")
	@Produces("application/xml")
	public Output getzipcode(@PathParam("zipcode") String zipcode) throws Exception{
		Output result = new Output();
		result.setZipcode(zipcode);
		result.setcount(trafficIncidentReportsService.getcount("zipcode", zipcode));
		return result;
	}
	
	@GET
	@Path("/reports/published_date/{published_date}")
	@Produces("application/xml")
	public Output getpublished_date(@PathParam("published_date") String published_date) throws Exception{
		Output result = new Output();
		result.setDate(published_date);
		result.setcount(trafficIncidentReportsService.getcount("published_date", published_date));
		return result;
	}
	@GET
	@Path("/reports/issue_reported/{issue_reported}")
	@Produces("application/xml")
	public Output getissue_reported(@PathParam("issue_reported") String issue_reported) throws Exception{
		Output result = new Output();
		result.setReport(issue_reported);
		result.setcount(trafficIncidentReportsService.getcount("issue_reported", issue_reported));
		return result;
	}
	@GET
	@Path("/reports/address/{address}")
	@Produces("application/xml")
	public Output getSpecificReport(@PathParam("address") String address) throws Exception{
		Output result = new Output();
		result.setAddress(address);
		result.setcount(trafficIncidentReportsService.getcount("address", address));
		return result;
	}

}
