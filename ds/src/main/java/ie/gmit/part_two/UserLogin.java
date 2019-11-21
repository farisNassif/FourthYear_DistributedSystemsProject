package ie.gmit.part_two;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

// Much like the User object there must also be an object for the UserLogin
public class UserLogin {
	@NotBlank
	private int id;
	@NotBlank
	private String name;
	
	public UserLogin(int id, String name) {
		this.id = id;
		this.name = name;
	}

	// Deserialization
	public UserLogin() {
	}
	
	@JsonProperty
	public int getId() {
		return id;
	}

	@JsonProperty
	public String getName() {
		return name;
	}
}
