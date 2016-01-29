package core;

import core.Event;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.eclipsesource.json.JsonArray;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
			try {event.setImage(img);} catch(Exception e){}
		}

		//parse location data
		JsonObject venue = json.get("venue").asObject();
		event.setVenue(venue.get("name").asString());
		venue = venue.get("location").asObject();
		event.setStreet(venue.get("street").asString());
		event.setLatitude(Float.parseFloat(venue.get("latitude").asString()));
		event.setLongitude(Float.parseFloat(venue.get("longitude").asString()));

		//get event time
		try {
			SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Date date = f1.parse(json.get("startDate").asString());
			SimpleDateFormat f2 = new SimpleDateFormat("EEEE | dd.MM.yyyy | HH:mm");
			event.setStartTime(f2.format(date));
		} catch (ParseException e) {
			event.setStartTime("invalid");
		}
		event.setEndTime("");

		//return 5gig event
		return event;
	}

	// parse a jsonObject of the Treibhaus (provided by Kimono API)
	public Event createTreibhausEvent(JsonObject json) {
		Event event = new Event();
		event.setApi("treibhaus");

		//parse event name
		event.setName(json.get("name").asString());

		//parse event description
		event.setDescription(json.get("description").asString());

		//parse date and time
		event.setStartTime(json.get("date_time").asString());

		//parse not existing end time
		event.setEndTime("");

		//parse location (all events are at the Treibhaus, so hardcode it)
		//latitude and longitude found here: http://www.sunny.at/main/remoteGetTips?id=9232
		event.setStreet("Angerzellgasse 8");
		event.setVenue("Innsbruck");
		event.setLatitude((float) 47.2692124);
		event.setLongitude((float) 11.4041024);

		//return treibhaus event
		return event;
	}
}
