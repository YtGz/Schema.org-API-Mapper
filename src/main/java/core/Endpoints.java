package core;

/** Stores all the endpoint urls for use */
public class Endpoints {

	public static final String fivegig = 
		"http://www.5gig.at/api/request.php?api_key=90c164a1d82540d7be50d54f4e887cb2&method=city.getEvents&city=Innsbruck&format=json";
	
	
	/* apis created with kimono */
	
	// event apis
	public static final String treibhaus = "https://www.kimonolabs.com/api/66cddkci?apikey=IlvCEiXaS7oojojyeLv58qCnTES0xwqR&authorization=h7RtvNgQnUeFQYKenRFBWL0MXJizJ55w";
	public static final String hafen = "https://www.kimonolabs.com/api/7rqf2im8?apikey=IlvCEiXaS7oojojyeLv58qCnTES0xwqR&authorization=h7RtvNgQnUeFQYKenRFBWL0MXJizJ55w";
	// weekender api will only find events before 01.01.2017
	public static final String weekender = "https://www.kimonolabs.com/api/3svf6dz4?apikey=IlvCEiXaS7oojojyeLv58qCnTES0xwqR&authorization=h7RtvNgQnUeFQYKenRFBWL0MXJizJ55w";
	
	// restaurant apis
	public static final String gutekueche = "https://www.kimonolabs.com/api/5pdrp9hy?apikey=IlvCEiXaS7oojojyeLv58qCnTES0xwqR&authorization=h7RtvNgQnUeFQYKenRFBWL0MXJizJ55w";
	public static final String lieferservice = "https://www.kimonolabs.com/api/bk436f42?apikey=IlvCEiXaS7oojojyeLv58qCnTES0xwqR&authorization=h7RtvNgQnUeFQYKenRFBWL0MXJizJ55w";
	// i added yelp because tiroler-wirtshaus, urlauburlaub.at and lokaltipp provide less information and are way harder to scrape
	// we get moore than 500 restaurants form yelp, do we even need the other sides?
	public static final String yelp = "https://www.kimonolabs.com/api/clo7bsse?apikey=IlvCEiXaS7oojojyeLv58qCnTES0xwqR&authorization=h7RtvNgQnUeFQYKenRFBWL0MXJizJ55w";
	

}
