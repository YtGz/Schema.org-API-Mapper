package core;

import static spark.Spark.*;

public class Main {

	//To access the application, browse to http://localhost:4567
    public static void main(String[] args) {
    	before((req, res) -> {
    		if(!req.contentType().equalsIgnoreCase("application/json")) {
    			halt("Content type does not match \"application/json\"!");
    		}
    	});
    	
    	post("/events/categories/add", (req, res) -> {
    		//TODO: parse the JSON from the request
    		
    		//TODO: call the eventful API endpoint
    		
    		//TODO: modify the response of the eventful API call
    		
    		
    		res.type("application/json"); 
    		return req.body();
    	});
    }
}
