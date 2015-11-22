package core;

import core.Restaurant;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.eclipsesource.json.JsonArray;

import java.net.URL;

public class RestaurantFactory {

	//parse a jsonObject of the X API into a restaurant
	public Restaurant createXRestaurant (JsonObject json) {
		Restaurant restaurant = new Restaurant();
		restaurant.setApi("");

		return restaurant;
	}

}
