package core;

import core.Restaurant;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.eclipsesource.json.JsonArray;

import java.net.URL;

public class RestaurantFactory {

	//parse a jsonObject of the OpenMenu.org API into a restaurant
	public Restaurant createOMRestaurant (JsonObject json) {
		Restaurant restaurant = new Restaurant();
		restaurant.setApi("openMenu");
		
		//parse event name
		event.setName(json.get("restaurant_name").asString());
		
		//parse registrar of restaurant
		event.setRegistrar(json.get("registrar").asString());

		//parse phone number
		event.setPhone(json.get("phone").asString());
		
		//parse primary address of the restaurant
		event.setAddress(json.get("address_1").asString());
		
		//parse latitude
		event.setLatitude(json.get("latitude").asString());
		
		//parse longitude
		event.setLongitude(json.get("longitude").asString());
		
		// parse status
		event.setStatus(json.get("status").asString());

		//parse artists
		JsonArray artists = json.get("artists").asArray();
		for (JsonValue value : artists) {
			event.addArtist(value.asObject().get("name").asString());
		}		

		//parse location data
		JsonObject venue = json.get("venue").asObject();
		event.setVenue(venue.get("name").asString());
		venue = venue.get("zipcode").asObject();

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

		//return jambase event
		return event;

		return restaurant;
	}

}
