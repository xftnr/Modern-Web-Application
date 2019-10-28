package domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "report")
@XmlAccessorType(XmlAccessType.FIELD)
public class TrafficIncidentReport {
	
	private int id = 0;
	private String published_date = null;
	private String issue_reported = null;
	private String address = null;
	private String zipcode = null;
	
	public void setid(int id) {
		this.id = id;
	}
	
	public int getid() {
		return id;
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
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setZipcode(String zip) {
		this.zipcode = zip;
	}
	
	public String getzipcode() {
		return zipcode;
	}
}
