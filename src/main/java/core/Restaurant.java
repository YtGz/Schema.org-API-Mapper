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
	private String priceSpread;
	private ArrayList<String> type;
	private String[] openingHours = new String[7]; 
	private String status;
	private String description;
	private String image;
	private String street;
	private float latitude;
	private float longitude;
	
	@Override
	public String toString(){
		return "api: " + api + " Name: " + name + " Phone: " + ((phone == null) ? "" : phone) + " Address: " + address + " linkText: " + ((linkText == null) ? "" : linkText) + " priceSpread: " + priceSpread + 
				" Type: " + type.toString() + " opening Hours: [" + ((openingHours == null) ? "" : ("mon: " + openingHours[0] + " tue: " + openingHours[1] + " wed: " + openingHours[2] + " thu: " + openingHours[3] + " fri: " + openingHours[4] + " sat: " + openingHours[5] + " sun: " + openingHours[6])) + "] Image: " + ((image == null) ? "" : image) + "\n\n";
		
	}

	public float getLatitude() {
		return latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public String getStreet() {
		return street;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public void setApi(String api) {
		this.api = api;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public void setPriceSpread(String priceSpread) {
		this.priceSpread = priceSpread;
	}

	public void setType(ArrayList<String> type) {
		this.type = type;
	}

	public void setOpeningHours(String[] openingHours) {
		this.openingHours = openingHours;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public void setLinkText(String linkText) {
		this.linkText = linkText;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public void setVenue(String venue) {
		this.venue = venue;
	}

	public void setRegistrar(String registrar) {
		this.registrar = registrar;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
