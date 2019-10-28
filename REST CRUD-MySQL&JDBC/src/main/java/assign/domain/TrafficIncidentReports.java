package domain;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;

@XmlRootElement(name = "reports")
@XmlAccessorType(XmlAccessType.FIELD)
public class TrafficIncidentReports {
private List<TrafficIncidentReport> report = null;

    public List<TrafficIncidentReport> getReports() {
    	return report;
    }
    
    public void setReports(List<TrafficIncidentReport> issue_report) {
    	this.report = issue_report;
    }
}

