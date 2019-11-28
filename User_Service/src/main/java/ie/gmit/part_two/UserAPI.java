package ie.gmit.part_two;

import java.util.HashMap;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.dropwizard.validation.ConstraintViolations;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserAPI {
	private HashMap<Integer, User> userMap = new HashMap<Integer, User>();

	private String HOST = "localhost";
	private int PORT = 9001;
	private Validator validator;

	// Returns all Users in the 'database'
	@GET
	@Path("/users")
	public Response getUsers() {
		return Response.ok(UserDatabase.findUsers()).build();
	}

	// Returns a Single User
	@GET
	@Path("users/{id}")
	public Response getUser(@PathParam("id") int id) {
		// If user doesn't exist send a 404
		if (UserDatabase.findUser(id) == null) {
			return Response.status(404).type(MediaType.APPLICATION_JSON).entity("User Doesn't Exist! :(").build();
		}
		// Otherwise send the User back
		else {
			return Response.status(200).type(MediaType.APPLICATION_JSON).entity(UserDatabase.findUser(id)).build();
		}
	}

	// Adds a new User
	// Adapted from
	// https://www.baeldung.com/java-bean-validation-not-null-empty-blank
	@POST
	@Path("/users")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response add(User newUser) {
		// Set<ConstraintViolation<User>> violations = validator.validate(newUser);

		// If there's no issues
		// if (violations.isEmpty()) {
		UserDatabase.addUser(newUser.getId(), newUser);
		return Response.status(201)
				.entity("User " + newUser.getName() + " with ID " + newUser.getId() + " Added Successfuly.").build();
	}
	// If issues arise with the POST Request
	// else {
	// return Response.status(404).entity("There was an issue with your
	// POST").build();
	// }

	@DELETE
	@Path("/users/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("id") int id) {
		if (UserDatabase.findUser(id) == null) {
			return Response.status(404).type(MediaType.APPLICATION_JSON).entity("There's no User with an ID of " + id + " :(").build();
		} else {
			UserDatabase.removeUser(id);
			return Response.status(200).type(MediaType.APPLICATION_JSON).entity("User with ID " + id + " Removed.")
					.build();
		}

	}

	// Existing User Login
	@Path("/login")
	@POST
	public Response login(UserLogin login) {
		// Pretty long winded, but it does the job for testing at least
		// Basically if the {ID, Password} I post via postman matches what's in the
		// hashmap "log" the person in. TODO - Password service impl
		if ((UserDatabase.findUser(login.getId()) != null)
				&& (UserDatabase.findUser(login.getId()).getPassword().equals(login.getPassword()))) {
			String r = "Login Successful, welcome user with ID: " + login.getId();
			return Response.status(201).entity(r).build();
		}
		// Otherwise not successful
		else {
			String r = "Login not Successful!! lmao";
			return Response.status(201).entity(r).build();
		}
	}
}
