package core;

/** Stores all the endpoint urls for use */
public class Endpoints {

	public static final String fivegig = 
		"http://www.5gig.at/api/request.php?api_key=90c164a1d82540d7be50d54f4e887cb2&method=city.getEvents&city=Innsbruck&format=json";
	
	//add street name to the endpoint to get results
	public static final String geocode =
		"https://maps.googleapis.com/maps/api/geocode/json?key=AIzaSyC_5PeR1iVujSMWx5JzmMuhgdWq850pp00&region=AT&address=";
	
	/* apis created with kimono */
	
	// event apis
	public static final String treibhaus = 
		"https://www.kimonolabs.com/api/66cddkci?apikey=IlvCEiXaS7oojojyeLv58qCnTES0xwqR&authorization=h7RtvNgQnUeFQYKenRFBWL0MXJizJ55w";
	public static final String hafen = 
		"https://www.kimonolabs.com/api/7rqf2im8?apikey=IlvCEiXaS7oojojyeLv58qCnTES0xwqR&authorization=h7RtvNgQnUeFQYKenRFBWL0MXJizJ55w";
	// weekender api will only find events before 01.01.2017
	public static final String weekender = 
		"https://www.kimonolabs.com/api/3svf6dz4?apikey=IlvCEiXaS7oojojyeLv58qCnTES0xwqR&authorization=h7RtvNgQnUeFQYKenRFBWL0MXJizJ55w";
	
	// restaurant apis
	public static final String gutekueche = 
		"https://www.kimonolabs.com/api/5pdrp9hy?apikey=IlvCEiXaS7oojojyeLv58qCnTES0xwqR&authorization=h7RtvNgQnUeFQYKenRFBWL0MXJizJ55w";
	public static final String lieferservice = 
		"https://www.kimonolabs.com/api/bk436f42?apikey=IlvCEiXaS7oojojyeLv58qCnTES0xwqR&authorization=h7RtvNgQnUeFQYKenRFBWL0MXJizJ55w";
	// i added yelp because tiroler-wirtshaus, urlauburlaub.at and lokaltipp provide less information and are way harder to scrape
	// we get more than 500 restaurants form yelp, do we even need the other sides?
	public static final String yelp = 
		"https://www.kimonolabs.com/api/clo7bsse?apikey=IlvCEiXaS7oojojyeLv58qCnTES0xwqR&authorization=h7RtvNgQnUeFQYKenRFBWL0MXJizJ55w";

	// had to split the results into 3 parts because kimono can return only 2500 lines per scrape 
	public static final String yelpBasics1 = "http://www.kimonolabs.com/api/6gt16dhc?apikey=IlvCEiXaS7oojojyeLv58qCnTES0xwqR";
	public static final String yelpBasics2 = "http://www.kimonolabs.com/api/7j31y03i?apikey=IlvCEiXaS7oojojyeLv58qCnTES0xwqR";
	public static final String yelpBasics3 = "http://www.kimonolabs.com/api/cha0w3tc?apikey=IlvCEiXaS7oojojyeLv58qCnTES0xwqR";
	public static final String yelpOpeningHours1 = "http://www.kimonolabs.com/api/706o0x50?apikey=IlvCEiXaS7oojojyeLv58qCnTES0xwqR";
	public static final String yelpOpeningHours2 = "http://www.kimonolabs.com/api/cqeg2yla?apikey=IlvCEiXaS7oojojyeLv58qCnTES0xwqR";
	public static final String yelpOpeningHours3 = "http://www.kimonolabs.com/api/2ij63isq?apikey=IlvCEiXaS7oojojyeLv58qCnTES0xwqR";
}
