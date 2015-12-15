package core;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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
	private String status;
	private String description;
	private String image;
	private String street;
	private float latitude;
	private float longitude;
}
