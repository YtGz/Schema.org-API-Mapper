package core;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import lombok.Data;


/*
 * Json to POJO: https://github.com/FasterXML/jackson-databind (see 1 minute tutorial below)
 * 
 * We let Lombok generate our getters/setters.
 */
@Data	//i.a. generate getter/setter
@JsonRootName("results")	//remove unnecessary top-level wrapper
@JsonIgnoreProperties(ignoreUnknown = true)	//ignore JSON properties not used by this class
public class ResultWrapper {
	@JsonProperty("collection1")
	private ArrayList<EventInfo> eventInfos;
}



//TODO: Eliminate the need for this wrapper by using root unwrapping: http://stackoverflow.com/questions/27895376/deserialize-nested-array-as-arraylist-with-jackson