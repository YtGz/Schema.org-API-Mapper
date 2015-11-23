package core;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class Artist {
	private String name;

    public Artist(@JsonProperty("name") String name) {
        this.name = name;
    }
}
