package core;

import core.Event;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.eclipsesource.json.JsonArray;

import java.net.MalformedURLException;
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

		//parse date and time to format DAY NAME | DATE | TIME
		String t = json.get("date_time").asString();
		String[] s = t.split(" ");
		s[0] = s[0].replace("MO", "Monday");
		s[0] = s[0].replace("DI", "Tuesday");
		s[0] = s[0].replace("MI", "Wednesday");
		s[0] = s[0].replace("DO", "Thursday");
		s[0] = s[0].replace("FR", "Friday");
		s[0] = s[0].replace("SA", "Saturday");
		s[0] = s[0].replace("SO", "Sunday");
		s[1] = s[1] + "2016";
		String rt = s[0] + " | " + s[1] + " | " + s[s.length-2];
		event.setStartTime(rt);
		//event.setStartTime(json.get("date_time").asString()); // old version

		//parse not existing end time
		event.setEndTime("");

		//parse location (all events are at the Treibhaus, so hardcode it)
		//latitude and longitude found here: http://www.sunny.at/main/remoteGetTips?id=9232
		event.setStreet("Angerzellgasse 8");
		event.setVenue("Treibhaus Innsbruck");
		event.setLatitude((float) 47.2692124);
		event.setLongitude((float) 11.4041024);

		//return treibhaus event
		return event;
	}

	// parse a jsonObject of the Hafen (provided by Kimono API)
	public Event createHafenEvent(JsonObject json) {
		Event event = new Event();
		event.setApi("hafen");

		//parse event name
		//name is not a regular string, it looks like {"href":"http://www.hafen.cc/index.php/veranstaltungen/128-halbzeitfete/event_details","text":"Halbzeitfete"}
		//workaround: split the result by : into array of strings and replace unnecessary characters like } and 2 times " and return it afterwards
		String s = json.get("name").asObject().toString();
		String[] split = s.split(":");
		String r = split[3];
		r = r.replace("}", "");
		r = r.replace("\"", "");
		event.setName(r);

		//parse event description
		event.setDescription(json.get("description").asString());

		//parse date and time
		// Replace Month name by its value, to create the correct date/time output format
		try {
			String d = json.get("date").asString();
			d = d.replace(" Jänner", "01.");
			d = d.replace(" Januar", "01.");
			d = d.replace(" Februar", "02.");
			d = d.replace(" März", "03.");
			d = d.replace(" April", "04.");
			d = d.replace(" Mai", "05.");
			d = d.replace(" Juni", "06.");
			d = d.replace(" Juli", "07.");
			d = d.replace(" August", "08.");
			d = d.replace(" September", "09.");
			d = d.replace(" Oktober", "10.");
			d = d.replace(" November", "11.");
			d = d.replace(" Dezember", "12.");
			d = d.replace(" 2016", "2016"); // eleminate white space

			SimpleDateFormat f1 = new SimpleDateFormat("d.MM.yyyy");
			Date date = f1.parse(d);
			SimpleDateFormat f2 = new SimpleDateFormat("EEEE | dd.MM.yyyy");
			event.setStartTime(f2.format(date) + " | " + json.get("startTime").asString());
		} catch (ParseException e) {
			event.setStartTime("invalid");
		}
		//event.setStartTime(json.get("date").asString() + ", " + json.get("startTime").asString());

		//parse not existing end time
		event.setEndTime("");

		//parse location (all events are at the Hafen, so hardcode it)
		//latitude and longitude found here: http://mondeca.com/index.php/en/any-place-en
		event.setStreet("Innrain 149");
		event.setVenue("Hafen");
		event.setLatitude((float) 47.25605);
		event.setLongitude((float) 11.37694);

		//return hafen event
		return event;
	}

	// parse a jsonObject of the Weekender (provided by Kimono API)
	public Event createWeekenderEvent(JsonObject json) {
		Event event = new Event();
		event.setApi("weekender");

		//parse event name
		//name is not a regular string, it looks like {"href":"http://www.weekender.at/index.php?day=05&month=12&year=2015","text":"CHAD VALLEY (UK)"}
		//workaround: split the result by : into array of strings and replace unnecessary characters like } and 2 times " and return it afterwards
		String s = json.get("name").asObject().toString();
		String[] split = s.split(":");
		String r = split[3];
		r = r.replace("}", "");
		r = r.replace("\"", "");
		event.setName(r);

		//parse event description
		event.setDescription(json.get("description").asString());

		//parse date and time
		String w = json.get("startTime").asString();
		w = w.replace("Einlass", "");
		w = w.replace("Uhr", "");
		try {
			SimpleDateFormat f1 = new SimpleDateFormat("dd.MM.yyyy, hh:mm");
			Date date = f1.parse(w);
			SimpleDateFormat f2 = new SimpleDateFormat("EEEE | dd.MM.yyyy | HH:mm");
			event.setStartTime(f2.format(date));
		} catch (ParseException e) {
			event.setStartTime("invalid");
		}
		//event.setStartTime(json.get("startTime").asString());

		//parse not existing end time
		event.setEndTime("");

		//parse location (all events are at the Weekender Club, so hardcode it)
		//latitude and longitude found here: http://mondeca.com/index.php/en/any-place-en
		event.setStreet("Tschamlerstraße 3");
		event.setVenue("Weekender Club");
		event.setLatitude((float) 47.25782);
		event.setLongitude((float) 11.39619);

		//return weekender event
		return event;
	}

	// parse a jsonObject of the Events.at (provided by Kimono API)
	// 2016-02-01: Martin: Results are very bad and also displayed very bad -> ignore events.at ...
	/*public Event createEventsAtEvent(JsonObject json) {
		Event event = new Event();
		event.setApi("EventsAt");

		//parse event name
		//name is not a regular string, it looks like {"href":"https://www.events.at/e/simon-kostner-unvertraeglichkeiten-und-andere-haustiere#st-241521877","text":"Simon Kostner - Unverträglichkeiten und andere Haustiere"}
		//workaround: split the result by : into array of strings and replace unnecessary characters like } and 2 times " and return it afterwards
		String s = json.get("name").asObject().toString();
		String[] split = s.split(":");
		String r = split[split.length-1];
		r = r.replace("}", "");
		r = r.replace("\"", "");
		if (r == "") { r = "No name given"; }
		event.setName(r);

		//parse event description
		event.setDescription(json.get("description").asString());

		//parse date and time
		//times are only displayed on the detail pages. So return a "<Date> - For detailed Starting/Ending Times, look at <URL>"
		r = split[split.length-2];
		r.replace("\",\"text", "");
		r.replaceAll("\"", "");
		r.replaceAll("text", "");
		r.replaceAll(",", "");
		r = "https:" + r;
		try {
			event.setStartTime(json.get("date").asString() + " - For detailed starting/ending time, click " + new URL(r)); // if r is not in here, you get no result on 3/4 of all searches, WTF !?
		} catch (MalformedURLException e) { // design needs improvement ..!
			e.printStackTrace();
		}

		//parse not existing end time
		event.setEndTime("");

		//Unfortionatly, you don't get the street on the index page
		//here too you would have to switch to detail page ...
		//we only get the location's name

		//++++++++++++ TODO !!! ++++++++++++
		// is there a possibility to get the address + geolocation via Google API only by location name?
		// Please feel free to extend here if you can do so
		/*event.setStreet("Angerzellgasse 8");
		event.setVenue("6020 Innsbruck");
		event.setLatitude((float) 47.2692124);
		event.setLongitude((float) 11.4041024);
		event.setStreet("Ingenieur-Etzel-Straße");
		event.setVenue("6020 Innsbruck");
		event.setLatitude((float) 47.2712546); // Coordinates & address from Congress Ibk temporary
		event.setLongitude((float) 11.4011425);

		//return events.at event
		return event;
	}*/
}
