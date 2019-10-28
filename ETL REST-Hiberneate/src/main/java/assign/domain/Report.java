package domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "reports")
public class Report {
	
	private Long id;
	private String published_date;
	private String issue_reported;
	private String address;
	private String zipcode;
	
	public Report() {
		
	}
	
	public Report(String date, String reported, String address, String zipcode) {
		this.published_date = date;
		this.issue_reported = reported;
		this.zipcode = zipcode;
		this.address = address;
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Long getid() {
		return id;
	}
	
	public void setid(Long id) {
		this.id = id;
	}

	@Column(name="published_date")
	public String getDate() {
		return published_date;
	}
	
	public void setDate(String date) {
		this.published_date = date;
	}
	
	@Column(name="issue_reported")
	public String getReport() {
		return issue_reported;
	}

	public void setReport(String issue) {
		this.issue_reported = issue;
	}
	
	@Column(name="address")
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	@Column(name="zipcode")
	public String getzipcode() {
		return zipcode;
	}
	
	public void setZipcode(String zip) {
		this.zipcode = zip;
	}
	
}
