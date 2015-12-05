package core;

import java.net.URL;
import java.util.List;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
@Data // i.a. generate getter/setter
@JsonIgnoreProperties(ignoreUnknown = true) // ignore JSON properties not used by this class
public class Event {
    @Id @GeneratedValue @JsonIgnore
    Long id;

	@JsonIgnore
	private String api;

	private String name;
	@OneToMany(mappedBy = "event") 
	private List<Artist> artists = new ArrayList<>();
	private String startTime; //date gets too ugly with jackson, so we use string
	private String endTime;
	private String description;
	private String image;
	private String venue; //the name of the venue
	private String street;
	/*-------------------------------------------------------------*/
	/* not sure if we need these, as it's always Innsbruck/Austria */
	//private String city;
	//private String country;
	/*-------------------------------------------------------------*/
	private float latitude;
	private float longitude;

	public void addArtist(String name) {
		artists.add(new Artist(name));
	}
}
