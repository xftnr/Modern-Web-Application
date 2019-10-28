package domain;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlElement;


@XmlRootElement(name = "output")
@XmlAccessorType(XmlAccessType.FIELD)
public class Error {
	private String error = null;
	
	public String getName() {
		return error;
	}
	public void setName(String name) {
		this.error = name;
	}
}

