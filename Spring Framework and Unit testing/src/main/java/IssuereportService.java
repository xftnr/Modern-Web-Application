import java.net.MalformedURLException;
import java.text.ParseException;

public interface IssuereportService {
	
	public int counting(String issueType) throws MalformedURLException, ParseException;
	
	public String risklevel(int count);

}
