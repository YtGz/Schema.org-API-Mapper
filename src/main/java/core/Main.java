package core;

import static spark.Spark.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ObjectBuffer;


public class Main {

    public static void main(String[] args) {
        port(getHerokuAssignedPort());
		//Tell spark where our static files are
		staticFileLocation("/public");

        get("/hello", (req, res) -> "https://www.youtube.com/watch?v=Am4oKAmc2To");

		post("/search", "application/json",(req, res) -> {
			return "[{\"name\":\"Jabba\"},{\"name\":\"Wabba\"}]";
		});



        /* Example for the 5gig API */
        get("/5gigtest", (req, res) -> {
        	try {
        		/* Jackson ObjectMapper maps JSON to an arbitrary Java class. */
	        	ObjectMapper mapper = new ObjectMapper();
	        	// Got a Java class that data maps to nicely? If so:
	        	//		MyFancyClass mfc = mapper.readValue(url, MyFancyClass.class);
	        	//
	        	// Or: if no class (and don't need one), just map to Map.class:
	        	//		Map<String,Object> map = mapper.readValue(url, new TypeReference<Map<String, Object>>(){});
	        	
	        	URL endpoint = new URL("http://www.nvivo.es/api/request.php?api_key=90c164a1d82540d7be50d54f4e887cb2&method=user.getEvents&user=hermzz&format=json");
	        	Map<String,Object> map = mapper.readValue(endpoint, new TypeReference<Map<String, Object>>(){}); 	//TypeReference is only needed to pass generic type definition
	        	/* Display part of the parsed JSON */
	        	return map.get("status");
        	}
        	catch (JsonParseException e) {
	            res.status(400);
	            return "";
        	}
        });
        
        /* Example for using the kimono endpoint URL (webhook is better) */
        get("/kimono_no_webhook", (req, res) -> {
        	try {
	        	/* Jackson ObjectMapper maps JSON to an arbitrary Java class. */
	        	ObjectMapper mapper = new ObjectMapper();
	        	URL endpoint = new URL("https://www.kimonolabs.com/api/1x4z55uw?apikey=cZIBnjuI5UUN5K420SiYnxeEci5GjKHs");
	        	WebhookData newData = mapper.readValue(endpoint, WebhookData.class);
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

}
