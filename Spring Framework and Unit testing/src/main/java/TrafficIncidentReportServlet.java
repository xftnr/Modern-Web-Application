import java.net.MalformedURLException;
import java.text.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TrafficIncidentReportServlet {

	private IssuereportService reportservive;


	public TrafficIncidentReportServlet() {
		
	}
	
	public TrafficIncidentReportServlet(IssuereportService issuereportService) {
		this.reportservive = issuereportService;
	}
	

	@ResponseBody
    @RequestMapping(value = "/")
    public String Index()
    {
        return "Welcome to assignemnt 2.";
    }
	
	@ResponseBody
    @RequestMapping(value = "/trafficincidentreports")
    public String welcome()
    {
        return "Welcome to Traffic Incident Report statistics calculation page. Please provide issue_reported as query parameter.";
    }

	
	@ResponseBody
	@RequestMapping(value = "/trafficincidentreports" , params = {"issue_reported"}, method=RequestMethod.GET)
	public String output(@RequestParam("issue_reported") String issue_reported) throws MalformedURLException, ParseException {
		int totalSum = this.reportservive.counting(issue_reported);
		String riskLevel = this.reportservive.risklevel(totalSum);
		return "Number of " + issue_reported + " incidents: " + totalSum + ". " + riskLevel + ".";
	}
	
}