package core;

import core.Restaurant;

import com.eclipsesource.json.JsonObject;


import java.util.ArrayList;

public class RestaurantFactory {
	
	
	public Restaurant createYelpBasics (JsonObject json, String address, String street, String price, ArrayList<String> type, String[] oh) {
		Restaurant restaurant = new Restaurant();
		restaurant.setApi("yelp");
		
		//parse restaurant name
		restaurant.setName(json.get("name").asString());
		
		//insert given primary address of the restaurant
		restaurant.setAddress(address);
		
		//insert given street
		restaurant.setStreet(street);
		
		//insert priceclass of the restaurant
		restaurant.setPriceSpan(price);
		
		// insert given type of the restaurant
		restaurant.setType(type);
		
		// insert given opening hours (if any)
				restaurant.setOpeningHours(oh);
		
		// if yelp does not store the phone- number of a given restaurant set it to "" 
		try{
			String phone = json.get("phone").asString();
			restaurant.setPhone(phone);
		} catch (Exception  e){
			restaurant.setPhone("");
		}
		
		// try to parse the URL and the corresponding text of the current restaurant, if yelp does not store this information set it to ""  
		try{
			JsonObject link = json.get("link").asObject();
			restaurant.setLink(link.get("href").asString());
			restaurant.setLinkText(link.get("text").asString());
		} catch (Exception  e){
			restaurant.setLinkText("");
			restaurant.setLink("");
		}
		
		
		// parse image. if no image stored set null
				try{
					String image = json.get("image").asString();
					restaurant.setImage(image);
				} catch (Exception  e){
					restaurant.setImage(null);
				}
		
		return restaurant;
	}
	
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
