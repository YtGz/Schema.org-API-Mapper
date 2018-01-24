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
