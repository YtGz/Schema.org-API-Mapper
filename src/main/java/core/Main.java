package core;

import static spark.Spark.*;

import java.net.URL;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.StringJoiner;
import java.lang.Thread;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ObjectBuffer;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.io.IOUtils;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.eclipsesource.json.JsonArray;

import core.Database;
import core.Event;
import core.EventFactory;
import core.Restaurant;
import core.RestaurantFactory;
import core.Endpoints;

import javax.persistence.*;
import java.lang.Math;

public class Main {

    public static void main(String[] args) {
        port(getHerokuAssignedPort());

		//Tell spark where our static files are
		staticFileLocation("/public");
		
		//get events/restaurants at server start
		Thread t1 = new Thread(new Runnable() {
			public void run() {
				updateDatabase();
			}
		});  
		t1.start();

		//--- ROUTES

        get("/hello", (req, res) -> "https://www.youtube.com/watch?v=Am4oKAmc2To");

		/* POST - /find
		 * returns all events/restaurants in the circumference of a specific restaurant/event
		 * (depending on post param) as json
		*/
		post("/find", "application/json",(req, res) -> {
			JsonObject request = Json.parse(req.body()).asObject();

			//get the request type (1 = Events, 2 = restaurants, Default = 1)
			Integer type = Integer.parseInt(request.getString("type","1"));

			//get the id of the specific event/restaurant (-x = error)
			Long id = Long.parseLong(request.getString("id","-1"));
			if (id < 0) {
				return ""; //replace with badrequest or something
			}

			//get the search radius (Default = 1.0)
			Float radius = Float.parseFloat(request.getString("radius","1.0"));

			//return events or restaurants depending on type/id and search radius
			StringJoiner sj = new StringJoiner(",", "[", "]");
			ObjectMapper mapper = new ObjectMapper();

			//find nearby restaurants
			if (type == 1) {
				Event event = Database.getAllEvents("WHERE e.id IN ("+id+")").get(0); // write an extra method for that in database class
				List<Restaurant> restaurants;
				restaurants = Database.getAllRestaurants();

				for (Restaurant r : restaurants) {
					if (inCircumference(event.getLatitude(),event.getLongitude(), r.getLatitude(), r.getLongitude(),radius)) {
						sj.add(mapper.writeValueAsString(r));
					}
				}
			}
			//find nearby events
			else if (type == 2) {
				Restaurant restaurant = Database.getAllRestaurants("WHERE r.id IN ("+id+")").get(0); // write an extra method for that in database class
				List<Event> events;
				events = Database.getAllEvents();

				for (Event e : events) {
					if (inCircumference(e.getLatitude(),e.getLongitude(), restaurant.getLatitude(), restaurant.getLongitude(),radius)) {
						sj.add(mapper.writeValueAsString(e));
					}
				}
			}

			return sj.toString();
		});

		/* POST - /search
		 * returns all events/restaurants (depending on post param) as json
		*/
		post("/search", "application/json",(req, res) -> {
			JsonObject request = Json.parse(req.body()).asObject();

			//get the request type (1 = Events, 2 = restaurants, Default = 1)
			Integer type = Integer.parseInt(request.getString("type","1"));

			//get the search term or "" if empty
			String text = request.getString("text","");

			//return events or restaurants depending on type and search text
			StringJoiner sj = new StringJoiner(",", "[", "]");
			ObjectMapper mapper = new ObjectMapper();

			if (type == 1) {
				List<Event> events;
				if (text.isEmpty()) {
					events = Database.getAllEvents();
				}
				else {
					events = Database.getAllEvents("WHERE LOWER(e.name) LIKE LOWER('%"+text+"%')");
				}
				for (Event e : events) {
					sj.add(mapper.writeValueAsString(e));
				}
			}
			else if (type == 2) {
				List<Restaurant> restaurants;
				if (text.isEmpty()) {
					restaurants = Database.getAllRestaurants();
				}
				else {
					restaurants = Database.getAllRestaurants("WHERE LOWER(r.name) LIKE LOWER('%"+text+"%')");
				}
				for (Restaurant r : restaurants) {
					sj.add(mapper.writeValueAsString(r));
				}
			}

			return sj.toString();
		});

        
        /* Example webhook integrating with kimono */
        post("/webhook", (req, res) -> {	//we specify 'ibkscraper.herokuapp.com/webhook' as the url to post to (in kimono)
        	try {
	        	ObjectMapper mapper = new ObjectMapper();
	        	WebhookData newData = mapper.readValue(req.body(), WebhookData.class);
	        	for(EventInfo elem : newData.getResults().getEventInfos()) {
		        	if (!elem.validate()) {
		                res.status(400);
		                return "";
		            }
	        	}
	        	for(EventInfo elem : newData.getResults().getEventInfos()) {
	        		System.out.println(elem.getPrice());
	        	}
	        	return "OK";
        	}
        	catch (JsonParseException e) {
	            res.status(400);
	            return "";
        	}
        });
    }

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }

	//updates database with api results
	static void updateDatabase() {

		//--- Events ---
		EventFactory event_factory = new EventFactory();
		ArrayList<Event> events = new ArrayList<>();

		//-- 5gig API --
		try {
			//create json object from url
			URL endpoint = new URL(Endpoints.fivegig);
			String endpoint_content = IOUtils.toString(endpoint, "UTF-8");
			JsonObject json = Json.parse(endpoint_content).asObject();
			
			//check if 5gig api call was successful
			if (json.get("status").asString().equals("success")) {
				//parse 5gig response
				JsonArray json_events = json.get("response").asObject().get("gigs").asArray();
	
				for (JsonValue value : json_events) {
					events.add(event_factory.createGigEvent(value.asObject()));
				}
			}
			else {
				System.out.println("API Error with 5gig call");
			}
		}
		catch (Exception e) {
			System.out.println("Exception: API Error with 5gig call");
			return;
		}
		//-- X API --

		//--- Restaurants ---
		RestaurantFactory restaurant_factory = new RestaurantFactory();
		ArrayList<Restaurant> restaurants = new ArrayList<>();

		//-- yelp API --
		try {
			//create json object from url
			URL endpoint = new URL(Endpoints.yelp);
			String endpoint_content = IOUtils.toString(endpoint, "UTF-8");
			JsonObject json = Json.parse(endpoint_content).asObject();
			
			//check if yelp api call was successful
			if (json.get("name").asString().equals("yelp_all")) {
				//parse yelp response
				JsonArray json_restaurants = json.get("results").asObject().get("collection1").asArray();
	
				for (JsonValue value : json_restaurants) {
					restaurants.add(restaurant_factory.createYelpRestaurant(value.asObject()));
				}
			}
			else {
				System.out.println("API Error with yelp call");
			}
		}
		catch (Exception e) {
			System.out.println("Exception: API Error with yelp call");
			return;
		}
		
		//-- X API --

		//-- get restaurant latitude/longitude
		try{
			for (Restaurant r : restaurants) {
				//only query if necessary
				if (r.getLatitude() < 0.01) {
					String street = r.getStreet().replace(' ','+');
					URL endpoint = new URL(Endpoints.geocode + street + ",+6020+Innsbruck");
					String endpoint_content = IOUtils.toString(endpoint, "UTF-8");
					JsonObject json = Json.parse(endpoint_content).asObject();
					String status = json.get("status").asString();

					//geocode call successful, parse latitude/longitude
					if (status.equals("OK")) {
						JsonObject json_location = json.get("results").asArray().get(0).
									               asObject().get("geometry").asObject().get("location").asObject();
						r.setLatitude(json_location.get("lat").asFloat());
						r.setLongitude(json_location.get("lng").asFloat());
					}
					//hit query limit of 2500/day, can only query 10/s now
					else if (status.equals("OVER_QUERY_LIMIT")) {
						Thread.sleep(12000);
						endpoint = new URL(Endpoints.geocode + street + ",+6020+Innsbruck");
						endpoint_content = IOUtils.toString(endpoint, "UTF-8");
						json = Json.parse(endpoint_content).asObject();
						status = json.get("status").asString();
						if (status.equals("OK")) {
							JsonObject json_location = json.get("results").asArray().get(0).
												       asObject().get("geometry").asObject().get("location").asObject();
							r.setLatitude(json_location.get("lat").asFloat());
							r.setLongitude(json_location.get("lng").asFloat());
						}
						else continue;
					}
					else continue;
				}
			}
			
		}
		catch(Exception e){
			System.out.println("Exception: API Error with google geocoding");
		}

		//--- delete old database
		Database.wipeDatabase();

		//--- Add Events/Restaurants to Database ---
		Database.addAllEvents(events);
		Database.addAllRestaurants(restaurants);
	}

	/** check if x2,y2 are in circumference of x1,y1 */
	static boolean inCircumference(Float x1, Float y1, Float x2, Float y2, Float radius) {
		float x = x2 - x1;
		float y = y2 - y1;

		float length = (float)Math.sqrt(x*x + y*y);
		if (length > radius) {
			return false;
		}
		return true;
	}
}
