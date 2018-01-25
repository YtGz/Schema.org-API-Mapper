package core;

import static spark.Spark.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;

/*
 * API calls that modify the data on Eventful (e.g. .../add) do not currently work as they require oAuth.
 * We don't want to modify the data on Eventful anyway; to see that our application is working use the other API endpoints for testing
 */
public class Main {

	//To access the application, browse to http://localhost:4567
    public static void main(String[] args) {
    	before((req, res) -> {
    		if(!req.contentType().equalsIgnoreCase("application/json")) {
    			halt("Content type does not match \"application/json\"!");
    		}
    	});
    	
    	
    	
    	post("/events/search", (req, res) -> {
    		//parse the JSON from the request
    		Any request = JsonIterator.deserialize(req.body());
    		String keywords = request.get("query").toString();
    		String location = request.get("object").get("location").toString();
    		String date = request.get("object").get("startDate").toString();
    		String category = request.get("object").get("about").toString();
    		String api_key = null;
    		String ex_category = null;
    		for(Any instrument : request.get("instrument").get("itemListElement")) {
    			switch(instrument.get("name").toString()) {
    				case "api-key":
    					api_key = instrument.get("identifier").toString();
    					break;
    				case "excluded_categories":
    					ex_category = instrument.get("identifier").toString();
    					break;
    			}
    		}
    		String sort_order = request.get("result").get("itemListOrder").get(0).toString();
    		String sort_direction = request.get("result").get("itemListOrder").get(1).toString();		
    		
    		//call the eventful API endpoint
    		URL url = new URL("http://api.eventful.com/json/events/search?app_key=" + api_key + "&keywords=" + keywords + "&location=" + location + "&date=" + date + "&category=" + category + "&ex_category=" + ex_category + "&sort_order=" + sort_order + "&sort_direction=" + sort_direction);
    		URLConnection urlConnection = url.openConnection();
    		String responseString;
    		try (BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8))) {
    		    responseString = reader.lines().collect(Collectors.joining("\n"));
    		}
    		Any response = JsonIterator.deserialize(responseString);
    		
    		//process the response of the eventful API call
    		if(response.get("error").toString().equalsIgnoreCase("1")) {
    			halt("API call not successful!");
    		}
    		// TODO: response json
    		res.type("application/json"); 
    		System.out.println(response.get("events").get(0).toString());
    		return "{\"@context\": \"http://schema.org/\",\"@type\": \"SearchAction\",\"actionStatus\": \"CompletedActionStatus\",\"result\": {\"@type\": [\"ItemList\"], \"ItemListElement\": [\"" + response.get("events").get(0).get("id").toString() + "\"]}}";
    	});
    	
    	post("/events/new", (req, res) -> {
    		//parse the JSON from the request
    		Any request = JsonIterator.deserialize(req.body());
    		String event_title = request.get("object").get("name").toString();
    		String start_time = request.get("object").get("startDate").toString();
    		String stop_time = request.get("object").get("endDate").toString();
    		String description = request.get("description").get("description").toString();
    		String free = request.get("object").get("isAccessibleForFree").toString();
    		for(Any instrument : request.get("instrument").get("itemListElement")) {
    			switch(instrument.get("name").toString()) {
    				case "api-key":
    					api_key = instrument.get("identifier").toString();
    					break;
    				case "event_venue":
    					event_venue = instrument.get("identifier").toString();
    					break;
    			}
    		}
    		
    		
    		//call the eventful API endpoint
    		URL url = new URL("http://api.eventful.com/json/events/new?app_key=" + api_key + "&title=" + event_title + "&start_time=" + start_time + "&stop_time=" + stop_time + "&description=" + description + "&price=" + price + "&venue_id=" + venue_id + "&parent_id=" + parent_id);
    		URLConnection urlConnection = url.openConnection();
    		String responseString;
    		try (BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8))) {
    		    responseString = reader.lines().collect(Collectors.joining("\n"));
    		}
    		Any response = JsonIterator.deserialize(responseString);
    		
    		//process the response of the eventful API call
    		if(!response.get("status").toString().equalsIgnoreCase("ok")) {
    			halt("API call not successful!");
    		}
    		
    		res.type("application/json"); 
    		String res_eventid = response.get("id").toString();
			return "{\"@context\": \"http://schema.org/\",\"@type\": \"AddAction\",\"actionStatus\": \"CompletedActionStatus\", \"result\": {\"@type\": \"Text\", \"id-output\": "+res_eventid+"}}";
    	});
    	
    	
    	
    	post("/events/categories/add", (req, res) -> {
    		//parse the JSON from the request
    		Any request = JsonIterator.deserialize(req.body());
    		String api_key = request.get("instrument").get("identifier").toString();
    		String event_id = request.get("object").get("identifier").toString();
    		String category_id = request.get("targetCollection").get("identifier").toString();
    		
    		//call the eventful API endpoint
    		URL url = new URL("http://api.eventful.com/json/events/categories/add?app_key=" + api_key + "&id=" + event_id + "&category_id=" + category_id);
    		URLConnection urlConnection = url.openConnection();
    		String responseString;
    		try (BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8))) {
    		    responseString = reader.lines().collect(Collectors.joining("\n"));
    		}
    		Any response = JsonIterator.deserialize(responseString);
    		
    		//process the response of the eventful API call
    		if(!response.get("status").toString().equalsIgnoreCase("ok")) {
    			halt("API call not successful!");
    		}
    		res.type("application/json"); 
			return "{\"@context\": \"http://schema.org/\",\"@type\": \"AddAction\",\"actionStatus\": \"CompletedActionStatus\"}";
    	});
    }
}
