package core;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Embedded;
import javax.persistence.Embeddable;

@Embeddable
@Data
public class Artist {
	private String name;

    public Artist(@JsonProperty("name") String name) {
        this.name = name;
    }
}
