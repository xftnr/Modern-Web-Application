import static org.junit.Assert.*;


import java.net.MalformedURLException;
import java.text.ParseException;

import org.junit.Before;
import org.junit.Test;



public class ServiceTest {
	IssuereportService issueServiceTest;
	
	@Before
	public void setup() {
		issueServiceTest = new IssuereportServiceImpl();
	}
	
	
	@Test //1. test riskLevelCalculator method when count is 1000, level should be high
	public void testRiskLevelCalculator() {
		String ret = issueServiceTest.risklevel(1000);
		assertEquals("Risk level: Extreme", ret);
	}
	
	@Test //2. test riskLevelCalculator method when count is 10, level should be Very Low
	public void testRiskLevelCalculator2() {
		String ret = issueServiceTest.risklevel(100);
		assertEquals("Risk level: High", ret);
	}
	
	@Test //3. test riskLevelCalculator method when count is 10, level should be Moderate
	public void testRiskLevelCalculator3() {
		String ret = issueServiceTest.risklevel(10);
		assertEquals("Risk level: Very Low", ret);
	}
	
}
