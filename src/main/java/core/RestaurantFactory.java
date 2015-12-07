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
		restaurant.setName(json.get("restaurant_name").asString());
		
		//parse registrar of restaurant
		restaurant.setRegistrar(json.get("registrar").asString());

		//parse phone number
		restaurant.setPhone(json.get("phone").asString());
		
		//parse primary address of the restaurant
		restaurant.setAddress(json.get("address_1").asString());
		
		//parse latitude
		restaurant.setLatitude(Float.parseFloat(json.get("latitude").asString()));
		
		//parse longitude
		restaurant.setLongitude(Float.parseFloat(json.get("longitude").asString()));
		
		// parse status
		restaurant.setStatus(json.get("status").asString());

		return restaurant;
	}

}
