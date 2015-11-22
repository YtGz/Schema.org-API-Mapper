package core;

import core.Event;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.eclipsesource.json.JsonArray;

public class EventFactory {

	//parse a jsonObject of the 5gig API
	public Event createGigEvent (JsonObject json) {
		Event event = new Event();
		event.setApi("5gig");

		//parse event name
		event.setName(json.get("name").asString());

		//parse artists
		JsonArray artists = json.get("artists").asArray();
		for (JsonValue value : artists) {
			event.setArtist(value.asObject().get("name").asString());
		}		

		//parse location data
		JsonObject venue = json.get("venue").asObject();

		//return 5gig event
		return event;
	}

}
