package core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/*
 * Json to POJO: https://github.com/FasterXML/jackson-databind (see 1 minute tutorial below)
 * 
 * We let Lombok generate our getters/setters.
 */
@Data // i.a. generate getter/setter
@JsonIgnoreProperties(ignoreUnknown = true) // ignore JSON properties not used by this class
public class EventInfo {
	// private String title;
	private String details;
	// private String[] description; //TODO: sometimes single string, sometimes multiple
	private String date;
	private String price;
	private String location;

	public boolean validate() {
		return details != null && date != null && !date.isEmpty() && price != null && location != null
				&& !location.isEmpty();
	}
}