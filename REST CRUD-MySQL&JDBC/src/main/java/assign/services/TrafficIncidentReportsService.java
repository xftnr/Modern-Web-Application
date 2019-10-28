package services;

import org.apache.commons.dbcp.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import domain.TrafficIncidentReport;

public class TrafficIncidentReportsService {
	String dburl;
	String username;
	String password;
	BasicDataSource ds;
	
	public TrafficIncidentReportsService(String dburl, String username, String password) {
		//init the info
		this.dburl = dburl;
		this.username = username;
		this.password = password;
		ds = setDataSource();
	}
	
	public BasicDataSource setDataSource() {
        BasicDataSource ds = new BasicDataSource();
        ds.setUsername(this.username);
        ds.setPassword(this.password);
        ds.setUrl(this.dburl);
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        return ds;
    }
	
	public TrafficIncidentReport addReport(TrafficIncidentReport newreport) throws Exception {
		Connection conn = ds.getConnection();
		
		String insert = "INSERT INTO reports(published_date, issue_reported, address, zipcode) VALUES(?, ?, ?, ?)";
		PreparedStatement stmt = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
		
		stmt.setString(1, newreport.getDate());
		stmt.setString(2, newreport.getReport());
		stmt.setString(3, newreport.getAddress());
		stmt.setString(4, newreport.getzipcode());
		
		int affectedRows = stmt.executeUpdate();

        if (affectedRows <= 0) {
            throw new SQLException("Creating report failed, no rows affected.");
        }
        
        
        ResultSet generatedKeys = stmt.getGeneratedKeys();
        if (generatedKeys.next()) {
        	newreport.setid(generatedKeys.getInt(1));
        }
        else {
            throw new SQLException("Creating report failed, no ID obtained.");
        }
        conn.close();
        
		return newreport;
	}
	
	
	public TrafficIncidentReport getindividualReport (int reportId) throws Exception {
		String query = "select * from reports where id=?";
    	try {
    		Connection conn = ds.getConnection();
    		PreparedStatement stmt = conn.prepareStatement(query);  
    	    
    		stmt.setString(1, String.valueOf(reportId)); 
    	    
    		ResultSet queryresult = stmt.executeQuery();
    		if (!queryresult.next()) {
    		    return null;
    		}
    		
    		//set return value
    		TrafficIncidentReport realresult = new TrafficIncidentReport();
    		realresult.setDate(queryresult.getString("published_date"));
    		realresult.setReport(queryresult.getString("issue_reported"));
    		realresult.setAddress(queryresult.getString("address"));
    		realresult.setZipcode(queryresult.getString("zipcode"));
    		realresult.setid(queryresult.getInt("id"));
    		return realresult;
    	} catch (Exception e) {
			return null;
		}
    }
	
	public TrafficIncidentReport updateReport(int reportId, TrafficIncidentReport newreport) throws Exception {
		String query = "UPDATE reports SET published_date=?, issue_reported=?, address=?, zipcode=? WHERE id=?";
		try {
			Connection conn = ds.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
			
			stmt.setString(1, newreport.getDate());
			stmt.setString(2, newreport.getReport());
			stmt.setString(3, newreport.getAddress());
			stmt.setString(4, newreport.getzipcode());
			stmt.setString(5, String.valueOf(reportId));
			
			int affectedRows = stmt.executeUpdate();
			
	        if (affectedRows <= 0) {
	            throw new SQLException("Creating reports failed, no rows affected.");         
	        }
	        conn.close();
	        return newreport;	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
	
	public boolean deleteReport(int reportId) throws Exception{
		String query = "DELETE FROM reports WHERE id=?";
    	try {
    		Connection conn = ds.getConnection();
    		PreparedStatement stmt = conn.prepareStatement(query);
    		stmt.setString(1, String.valueOf(reportId));
    		int affectedRows = stmt.executeUpdate();
    		
            if (affectedRows <= 0) {
                throw new SQLException("Creating reports failed, no rows affected.");         
            }
            conn.close();
            return true;
    	} catch (Exception e) {
    		e.printStackTrace();
		}
    	return false;

    }
	
	public List<TrafficIncidentReport> getallReports()throws Exception{
		String query = "SELECT id FROM reports";

		Connection conn = ds.getConnection();
    	List<TrafficIncidentReport> result = new ArrayList<>();	
		PreparedStatement s = conn.prepareStatement(query);
		ResultSet r = s.executeQuery();

		while (r.next()) {
			TrafficIncidentReport individual = new TrafficIncidentReport();
			individual.setid(r.getInt("id"));
			result.add(individual);
		}
		conn.close();
		return result;
    }
	
	public List<TrafficIncidentReport> getReportByZipCode(int zipcode)throws Exception{
		String query = "SELECT id FROM reports WHERE zipcode=?";
		Connection conn = ds.getConnection();
    	List<TrafficIncidentReport> result = new ArrayList<>();	
		PreparedStatement s = conn.prepareStatement(query);
		s.setString(1, String.valueOf(zipcode));
		ResultSet r = s.executeQuery();
		while (r.next()) {
			TrafficIncidentReport each = new TrafficIncidentReport();
			each.setid(r.getInt("id"));
			result.add(each);
		}		
		return result;
    }
	
	
}
