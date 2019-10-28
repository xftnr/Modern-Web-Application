package domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "output")
@XmlAccessorType(XmlAccessType.FIELD)
public class Output {
	private String published_date = null;
	private String issue_reported = null;
	private String address = null;
	private String zipcode = null;
	private int count = 0;
	

	public String getDate() {
		return published_date;
	}
	
	public void setDate(String date) {
		this.published_date = date;
	}
	
	public String getReport() {
		return issue_reported;
	}

	public void setReport(String issue) {
		this.issue_reported = issue;
	}

	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}

	public String getzipcode() {
		return zipcode;
	}
	
	public void setZipcode(String zip) {
		this.zipcode = zip;
	}
	
	public void setcount(int num) {
		this.count = num;
	}
	
	public int getcount() {
		return count;
	}
}
