package core;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Data
public class Artist {
    @Id @GeneratedValue @JsonIgnore
    Long id;

	private String name;

	@ManyToOne @JsonIgnore
	private Event event;

    public Artist(@JsonProperty("name") String name) {
        this.name = name;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getName() {
        return name;
    }
}
