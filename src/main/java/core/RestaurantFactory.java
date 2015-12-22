package core;

import core.Restaurant;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.eclipsesource.json.JsonArray;

import java.net.URL;

public class RestaurantFactory {
	
	public Restaurant createYelpRestaurant (JsonObject json) {
		Restaurant restaurant = new Restaurant();
		restaurant.setApi("yelp");
		
		//parse event name
		restaurant.setName(json.get("name").asString());
		
		//parse primary address of the restaurant
		String venue = json.get("address").asString();
		restaurant.setVenue(venue);

		//parse street
		restaurant.setStreet(venue.split("\n",2)[0]);
		
		return restaurant;
	}

	//parse a jsonObject of the OpenMenu.org API into a restaurant
	public Restaurant createOMRestaurant (JsonObject json) {
		Restaurant restaurant = new Restaurant();
		restaurant.setApi("openMenu");
		
		//parse event name
		restaurant.setName(json.get("restaurant_name").asString());
		
		//parse registrar of restaurant
		restaurant.setRegistrar(json.get("registrar").asString());

		//parse phone number
		restaurant.setPhone(json.get("phone").asString());
		
		//parse primary address of the restaurant
		restaurant.setVenue(json.get("address_1").asString());
		
		//parse latitude
		restaurant.setLatitude(Float.parseFloat(json.get("latitude").asString()));
		
		//parse longitude
		restaurant.setLongitude(Float.parseFloat(json.get("longitude").asString()));
		
		// parse status
		restaurant.setStatus(json.get("status").asString());

		return restaurant;
	}

}
