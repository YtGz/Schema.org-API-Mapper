package core;

import static spark.Spark.*;

public class Main {

	//To access the application, browse to http://localhost:4567
    public static void main(String[] args) {
    	get("/", (req, res) -> "Hello World");
    }
}
