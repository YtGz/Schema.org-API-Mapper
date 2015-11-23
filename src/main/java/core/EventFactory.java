package core;

import core.Event;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.eclipsesource.json.JsonArray;

import java.net.URL;

public class EventFactory {

	//parse a jsonObject of the 5gig API
	public Event createGigEvent (JsonObject json) {
		Event event = new Event();
		event.setApi("5gig");

		//parse event name
		event.setName(json.get("name").asString());

		//parse event description
		event.setDescription(json.get("description").asString());

		//parse artists
		JsonArray artists = json.get("artists").asArray();
		for (JsonValue value : artists) {
			event.addArtist(value.asObject().get("name").asString());
		}		

		//get image
		String img = json.get("images").asObject().get("small").asString();

		if (!img.equals("")) {
			try {event.setImage(new URL(img));} catch(Exception e){}
		}

		//parse location data
		JsonObject venue = json.get("venue").asObject();
		event.setVenue(venue.get("name").asString());
		venue = venue.get("location").asObject();
		event.setStreet(venue.get("street").asString());
		event.setLatitude(Float.parseFloat(venue.get("latitude").asString()));
		event.setLongitude(Float.parseFloat(venue.get("longitude").asString()));

		//get event time
		event.setStartTime(json.get("startDate").asString());
		event.setEndTime("");

		//return 5gig event
		return event;
	}

}
