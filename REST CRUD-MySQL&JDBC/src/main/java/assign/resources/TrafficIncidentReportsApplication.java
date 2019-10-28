package resources;
import java.util.HashSet;


import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/trafficincidentreports")

public class TrafficIncidentReportsApplication extends Application{
	
	private Set<Object> singletons = new HashSet<Object>();
	private Set<Class<?>> classes = new HashSet<Class<?>>();
	
	public TrafficIncidentReportsApplication() {		
	}
	
	@Override
	public Set<Class<?>> getClasses() {
		classes.add(TrafficIncidentReportsResource.class);
		return classes;
	}
	
	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}

}
