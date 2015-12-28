package core;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.ArrayList;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Data // i.a. generate getter/setter
@JsonIgnoreProperties(ignoreUnknown = true) // ignore JSON properties not used by this class
public class Restaurant {
    @Id @GeneratedValue
    Long id;

	@JsonIgnore
	private String api;

	private String name;
	private String registrar;
	private String phone;
	private String venue;//private String address; renamed for now
	private String address;
	private String link;
	private String linkText;
	private String priceSpan;
	private ArrayList<String> type;
	private String[] openingHours = new String[7]; 
	private String status;
	private String description;
	private String image;
	private String street;
	private float latitude;
	private float longitude;
	
	public String toString(){
		/* 
		 	System.out.println("api: " + api + " Name: " + name + " Phone: " + ((phone == null) ? "" : phone) + " Address: " + address + " linkText: " + ((linkText == null) ? "" : linkText) + " priceSpan: " + priceSpan + 
				" Type: " + type.toString() + " opening Hours: [" + ((openingHours == null) ? "" : openingHours.toString()) + "] Image: " + ((image == null) ? "" : image)); //*/
		return "api: " + api + " Name: " + name + " Phone: " + ((phone == null) ? "" : phone) + " Address: " + address + " linkText: " + ((linkText == null) ? "" : linkText) + " priceSpan: " + priceSpan + 
				" Type: " + type.toString() + " opening Hours: [" + ((openingHours == null) ? "" : ("mon: " + openingHours[0] + " tue: " + openingHours[1] + " wed: " + openingHours[2] + " thu: " + openingHours[3] + " fri: " + openingHours[4] + " sat: " + openingHours[5] + " sun: " + openingHours[6])) + "] Image: " + ((image == null) ? "" : image) + "\n\n";
		
	}
}
