package ie.gmit.part_two;

import org.hibernate.validator.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
	@NotBlank
	private String password;
	@NotBlank
	private String email;
	@NotBlank
	private int id;
	@NotBlank
	private String name;

	public User(int id, String name, String email, String password) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
	}

	// Deserialization
	public User() {
	}

	// JsonProperty required for serialization/deserialization by Jackson

	@JsonProperty
	public String getPassword() {
		return password;
	}

	@JsonProperty
	public String getEmail() {
		return email;
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
