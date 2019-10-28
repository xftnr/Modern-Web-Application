import static org.junit.Assert.assertEquals;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.text.ParseException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;



public class ControllerTest {

	TrafficIncidentReportServlet controllerTest; 
	IssuereportService mockService = null;
	
	@Before
	public void setup() {
		mockService = mock(IssuereportService.class);
		controllerTest = new TrafficIncidentReportServlet(mockService);
	}
	
	
	@Test //1. Test index page in controller 
	public void testindex() throws ServletException, IOException {
		PrintWriter writer = mock(PrintWriter.class);		
		HttpServletResponse response = mock(HttpServletResponse.class);
		when(response.getWriter()).thenReturn(writer);
		response.getWriter().println(controllerTest.Index());
		verify(writer).println("Welcome to assignemnt 2.");		
		verify(response).getWriter();
	}
	
	@Test //2. Test trafficincidentreports
	public void testwelcome() throws ServletException, IOException {
		PrintWriter writer = mock(PrintWriter.class);		
		HttpServletResponse response = mock(HttpServletResponse.class);
		when(response.getWriter()).thenReturn(writer);
		response.getWriter().println(controllerTest.welcome());
		verify(writer).println("Welcome to Traffic Incident Report statistics calculation page. Please provide issue_reported as query parameter.");		
		verify(response).getWriter();
	}
	
	@Test //3. Test output page
	public void testoutput() throws MalformedURLException, ParseException {
		when(mockService.counting("Collision")).thenReturn(35);
		when(mockService.risklevel(35)).thenReturn("Risk level: Moderate");
		String result = controllerTest.output("Collision");
		assertEquals("Number of Collision incidents: 35. Risk level: Moderate.", result);
	}
	
	@Test //4. Test output page
	public void testoutput2() throws MalformedURLException, ParseException {
		when(mockService.counting("collision with injury")).thenReturn(8);
		when(mockService.risklevel(8)).thenReturn("Risk level: Very Low");
		String result = controllerTest.output("collision with injury");
		assertEquals("Number of collision with injury incidents: 8. Risk level: Very Low.", result);
	}
}
