package domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "report")
@XmlAccessorType(XmlAccessType.FIELD)
public class TrafficIncidentReport {
	
	private String traffic_report_id = null;
	private String published_date = null;
	private String issue_reported = null;
	private String latitude = null;
	private String longitude = null;
	private String address = null;
	
	public void setName(String id) {
		this.traffic_report_id = id;
	}
	
	public String getid() {
		return traffic_report_id;
	}
	
	public void setDate(String date) {
		this.published_date = date;
	}
	
	public String getDate() {
		return published_date;
	}

	public void setReport(String issue) {
		this.issue_reported = issue;
	}
	
	public String getReport() {
		return issue_reported;
	}
	
	public void setLatitude(String latitiude) {
		this.latitude = latitiude;
	}
	
	public String getLatitude() {
		return latitude;
	}
	
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
	public String getLongitude() {
		return longitude;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getAddress() {
		return address;
	}
}
