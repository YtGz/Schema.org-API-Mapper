package core;

import java.util.ArrayList;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class YelpParser {
	public static void parseFromAPIs(JsonObject json_basics, JsonObject json_oh, ArrayList<Restaurant> restaurants) throws Exception{
		RestaurantFactory restaurant_factory = new RestaurantFactory();
		
		//check if yelp api call was successful
		if (json_basics.get("name").asString().contains("yelp_3_basics_part") && json_oh.get("name").asString().contains("yelp_3_opening_hours_part") && json_basics.get("lastrunstatus").asString().contains("success") && json_oh.get("lastrunstatus").asString().contains("success")) {
			//parse yelp response, need 4 result- arrays for the basics- call because we get 4 collections  
			JsonArray json_restaurants_basics = json_basics.get("results").asObject().get("collection1").asArray();
			JsonArray json_restaurants_addresses = json_basics.get("results").asObject().get("collection2").asArray();
			int addressIndex = 0;
			JsonArray json_restaurants_prices = json_basics.get("results").asObject().get("collection3").asArray();
			int priceIndex = 0;
			JsonArray json_restaurants_types = json_basics.get("results").asObject().get("collection4").asArray();
			int typesIndex = 0;
			// need 1 more result array for the opening hours API
			JsonArray json_restaurants_oh = json_oh.get("results").asObject().get("collection1").asArray();
			int ohIndex = 0;
			String helpString;
			for (JsonValue value : json_restaurants_basics) {
				// following needs to be parsed here because it would be incredibly inefficient to do it elsewhere
				// every collection except the first collection of the yelp- basics API needs to e parsed here because one row in the response does not 
				// correspond to one entry since the rows have to be buffered and interpreted we have to do it here
				
				// parse  and compose the address for each restaurant; if the row reads "Inhaltsrichtlinien" that means the address of the current
				// restaurant is complete 
				String address = "";
				String street = null;
				boolean first = true;
 				do{ 
					helpString = json_restaurants_addresses.get(addressIndex).asObject().get("address").asString();
					if (!helpString.equals("Inhaltsrichtlinien")){
						if(first){
							street = helpString;
							first = false;
							if (street.contains("\n")){
								street = street.substring(0,street.indexOf("\n"));
							}
						}
						address =  address + " " + helpString.replace("\n", "; ");
						addressIndex++;
					}else{
						first = true;
					}
				} while (!helpString.equals("Inhaltsrichtlinien"));
				addressIndex++;
				
				// parse the price spread for each restaurant (if given); if the row reads "Datenschutzerklärung" that means the price spread
				// of the current restaurant is complete. this needs to be done here because we have to scroll thru the results until we get the
				// row we are looking for
				String price = "";
				do{
					try {
						helpString = json_restaurants_prices.get(priceIndex).asObject().get("price").asString();
						if (!helpString.equals("Datenschutzerklärung")) {
							if (helpString.contains("€"))
								price = helpString;
							priceIndex++;
						}
					} catch (Exception e) {
						System.out.println("Error parsing Yelp price. Price index: " + priceIndex + "Continue to next ...");
						continue;
					}
				} while (!helpString.equals("Datenschutzerklärung"));
				priceIndex++;
				
				// parse the types/ categories for each restaurant (if given); if the row reads "Entwickler" that means that all types
				// of the current restaurant are parsed. since one restaurant can have more then one type this has to be done here. 
				ArrayList<String> types = new ArrayList<String>();
				do{ 
					helpString = json_restaurants_types.get(typesIndex).asObject().get("type").asString();
					if (!helpString.equals("Entwickler")){
						types.add(new String(helpString));
						typesIndex++;
					}
				} while (!helpString.equals("Entwickler"));
				typesIndex++;
				
				// parse opening hours for the current restaurant. not every restaurant lists the opening hours on yelp
				String current = value.asObject().get("name").asString();
				helpString = json_restaurants_oh.get(ohIndex).asObject().get("openingHours").asString();
				String[] oh = null;
				if (current.equals(helpString)){
					if(ohIndex < json_restaurants_oh.size()-1){
						helpString = json_restaurants_oh.get(++ohIndex).asObject().get("openingHours").asString();
						if (helpString.equals("Mo.")){
							String mon = json_restaurants_oh.get(++ohIndex).asObject().get("openingHours").asString();
							helpString = json_restaurants_oh.get(ohIndex).asObject().get("openingHours").asString();
							while(!helpString.equals("Di."))
								helpString = json_restaurants_oh.get(++ohIndex).asObject().get("openingHours").asString();
							String tue = json_restaurants_oh.get(++ohIndex).asObject().get("openingHours").asString();;
							helpString = json_restaurants_oh.get(ohIndex).asObject().get("openingHours").asString();
							while(!helpString.equals("Mi."))
								helpString = json_restaurants_oh.get(++ohIndex).asObject().get("openingHours").asString();
							String wed = json_restaurants_oh.get(++ohIndex).asObject().get("openingHours").asString();;
							helpString = json_restaurants_oh.get(ohIndex).asObject().get("openingHours").asString();
							while(!helpString.equals("Do."))
								helpString = json_restaurants_oh.get(++ohIndex).asObject().get("openingHours").asString();
							String thu = json_restaurants_oh.get(++ohIndex).asObject().get("openingHours").asString();;
							helpString = json_restaurants_oh.get(ohIndex).asObject().get("openingHours").asString();
							while(!helpString.equals("Fr."))
								helpString = json_restaurants_oh.get(++ohIndex).asObject().get("openingHours").asString();
							String fri = json_restaurants_oh.get(++ohIndex).asObject().get("openingHours").asString();;
							helpString = json_restaurants_oh.get(ohIndex).asObject().get("openingHours").asString();
							while(!helpString.equals("Sa."))
								helpString = json_restaurants_oh.get(++ohIndex).asObject().get("openingHours").asString();
							String sat = json_restaurants_oh.get(++ohIndex).asObject().get("openingHours").asString();;
							helpString = json_restaurants_oh.get(ohIndex).asObject().get("openingHours").asString();
							while(!helpString.equals("So."))
								helpString = json_restaurants_oh.get(++ohIndex).asObject().get("openingHours").asString();
							String sun = json_restaurants_oh.get(++ohIndex).asObject().get("openingHours").asString();;
							
							ohIndex++;
							ohIndex++;
							
							mon = mon.replace("\n", " / ");
							tue = tue.replace("\n", " / ");
							wed = wed.replace("\n", " / ");
							thu = thu.replace("\n", " / ");
							fri = fri.replace("\n", " / ");
							sat = sat.replace("\n", " / ");
							sun = sun.replace("\n", " / ");
							// System.out.println("mon: " + mon + " tue: " + tue + " wed: " + wed + " thu: " + thu + " fri: " + fri + " sat: " + sat + " sun: " + sun);
							oh = new String[]{mon, tue, wed, thu, fri, sat, sun};
						}
					}
				} else
					ohIndex++;
				
				restaurants.add(restaurant_factory.createYelpBasics(value.asObject(), address, street, price, types, oh));
				/*
				Restaurant r = restaurants.get(restaurants.size() -1);
				System.out.println("name: " + r.getName() +  " STREET: " + r.getStreet());
				//*/
			}
			// System.out.println(restaurants.toString());
		} else {
			throw new Exception("API Error with yelp call");
		}
	}
	
}