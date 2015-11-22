package core;

import java.net.URL;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data // i.a. generate getter/setter
@JsonIgnoreProperties(ignoreUnknown = true) // ignore JSON properties not used by this class
public class Event {
	@JsonIgnore
	private String api;

	private String name;
	private String artist; //for now only 1 artist is stored, because jackson is ugly with arraylists
	private Date startTime;
	private Date endTime;
	private String description;
	private URL image;
	private String venue; //the name of the venue
	private String street;
	/*-------------------------------------------------------------*/
	/* not sure if we need these, as it's always Innsbruck/Austria */
	//private String city;
	//private String country;
	/*-------------------------------------------------------------*/
	private float latitude;
	private float longitude;
}
