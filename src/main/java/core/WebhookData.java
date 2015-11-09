package core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/*
 * Json to POJO: https://github.com/FasterXML/jackson-databind (see 1 minute tutorial below)
 * 
 * We let Lombok generate our getters/setters.
 */
@Data	//i.a. generate getter/setter
@JsonIgnoreProperties(ignoreUnknown = true)	//ignore JSON properties not used by this class
public class WebhookData {
	private ResultWrapper results;
	
}
