package resources;

import static org.junit.Assert.assertEquals;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Before;
import org.junit.Test;

import domain.TrafficIncidentReport;
import services.TrafficIncidentReportsService;

public class TestTrafficIncidentReportsResource {
	TrafficIncidentReportsService trService = null;
	private HttpClient client;

	@Before
	public void setUp() {
		client = HttpClientBuilder.create().build();
		String dburl = "jdbc:mysql://localhost:3306/issue_report";
		String dbusername = "pengdi";
		String dbpassword = "";
		trService = new TrafficIncidentReportsService(dburl, dbusername, dbpassword);
	}


	@Test
	public void testPOST() throws Exception {
		try {
			TrafficIncidentReport temp = new TrafficIncidentReport();
			temp.setDate("2018-06-13T06:35:59Z");
			temp.setAddress("W 21ST ST and GUADALUPE ST");
			temp.setReport("CrashService");
			temp.setZipcode("78717");
			TrafficIncidentReport result = trService.addReport(temp);
			boolean vaild = false;
			if (result.getid() != 0) {
				vaild = true;
			}
			assertEquals(true, vaild);		
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testPUT() throws Exception {
		//post a new data
		TrafficIncidentReport temp = new TrafficIncidentReport();
		temp.setDate("2018-06-13T06:35:59Z");
		temp.setAddress("W 21ST ST and GUADALUPE ST");
		temp.setReport("CrashService");
		temp.setZipcode("78717");
		TrafficIncidentReport result = trService.addReport(temp);
		// change this data
		temp.setReport("lol");
		TrafficIncidentReport result2 = trService.updateReport(result.getid(), temp);
		assertEquals("lol",result2.getReport());
		//delete
		trService.deleteReport(result.getid());
	}

	@Test
	public void testDELETE() throws Exception {
		//post a new data
		TrafficIncidentReport temp = new TrafficIncidentReport();
		temp.setDate("2018-06-13T06:35:59Z");
		temp.setAddress("W 21ST ST and GUADALUPE ST");
		temp.setReport("CrashService");
		temp.setZipcode("78717");
		TrafficIncidentReport result = trService.addReport(temp);
		//test
		boolean successdelete = trService.deleteReport(result.getid());
		assertEquals(true, successdelete);
	}

	@Test
	public void testGET() throws Exception {
		//post a new data
		TrafficIncidentReport temp = new TrafficIncidentReport();
		temp.setDate("2018-06-13T06:35:59Z");
		temp.setAddress("W 21ST ST and GUADALUPE ST");
		temp.setReport("CrashService");
		temp.setZipcode("78717");
		TrafficIncidentReport result = trService.addReport(temp);
		//test
		TrafficIncidentReport result2 = trService.getindividualReport(result.getid());
		assertEquals("2018-06-13T06:35:59Z", result2.getDate());
		assertEquals("W 21ST ST and GUADALUPE ST", result2.getAddress());
		assertEquals("78717", result2.getzipcode());
		assertEquals("CrashService", result.getReport());
		//delete
		trService.deleteReport(result.getid());
	}
}
