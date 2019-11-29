package ie.gmit.part_two;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.protobuf.ByteString;

// Refs
// https://www.dropwizard.io/en/stable/getting-started.html
// https://stackoverflow.com/questions/38153880/dropwizard-custom-validation-annotation-not-working
public class User {
	@NotBlank
	private String password;
	@NotBlank
	// @Pattern(regexp = ".+@.+\\.[a-z].+\\.com")
	private String email;
	@NotNull
	private int id;
	@NotBlank
	private String name;
	private ByteString passwordHashed;
	private ByteString salt;

	public User(int id, String name, String email, String password, ByteString passwordHashed, ByteString salt) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.passwordHashed = passwordHashed;
		this.salt = salt;
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

	@JsonProperty
	public ByteString getHash() {
		return passwordHashed;
	}

	@JsonProperty
	public ByteString getSalt() {
		return salt;
	}

}
