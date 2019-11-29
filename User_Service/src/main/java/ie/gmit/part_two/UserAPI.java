package ie.gmit.part_two;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import password_client.PasswordClient;

import java.io.UnsupportedEncodingException;

import javax.validation.Validator;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserAPI {

	private int PORT = 50000;
	private Validator validator;

	PasswordClient client = new PasswordClient("localhost", PORT);

	// Returns all Users in the database
	@GET
	@Path("/users")
	public Response getUsers() {
		return Response.ok(UserDatabase.findUsers()).build();
	}

	// Returns a Single User.
	// After browsing through
	// https://docs.oracle.com/javaee/7/api/javax/ws/rs/core/Response.Status.html
	// decided to change statuses from "404/200" to their String form. For me at
	// least it looks nicer having
	// 'Status.NOT_FOUND' instead of just '404'
	@GET
	@Path("users/{id}")
	public Response getUser(@PathParam("id") int id) {
		// If user doesn't exist send a 404
		if (UserDatabase.findUser(id) == null) {
			return Response.status(Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity("User Doesn't Exist! :(")
					.build();
		}
		// Otherwise send the User back
		else {
			return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(UserDatabase.findUser(id))
					.build();
		}
	}

	// Adds a new User
	// Adapted from
	// https://www.baeldung.com/java-bean-validation-not-null-empty-blank
	@POST
	@Path("/users")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response add(User newUser) throws UnsupportedEncodingException, InterruptedException {
		// If theres not already a User with that ID
		if (UserDatabase.findUser(newUser.getId()) == null) {
			client.Generation(newUser);
			// UserDatabase.addUser(newUser.getId(), newUser);
			return Response.status(Status.CREATED).entity("User " + newUser.getName() + " with ID " + newUser.getId()
					+ " Added Successfuly. (Hash & Salt Logged in the console)").build();
		} else {
			return Response.status(Status.CONFLICT).type(MediaType.APPLICATION_JSON)
					.entity("Rejected! Theres already a User with that ID!").build();
		}

	}

	// Delete a User by ID
	@DELETE
	@Path("/users/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("id") int id) {
		// If the User to be deleted doesn't exist, throw a 404
		if (UserDatabase.findUser(id) == null) {
			return Response.status(Status.NOT_FOUND).type(MediaType.APPLICATION_JSON)
					.entity("There's no User with an ID of " + id + " :(").build();
		} else {
			// Otherwise send a 200 request and remove the User
			UserDatabase.removeUser(id);
			return Response.status(Status.OK).type(MediaType.APPLICATION_JSON)
					.entity("User with ID " + id + " Removed.").build();
		}
	}

	// Update a User by ID
	// Had a bit of a crisis trying to solve the issue of having duplicate keys in
	// the HashMap since for example if userA has ID 1 and userB has ID 2 and you
	// update userB to have ID 1 then userA won't be able to be retrieved. If I have
	// time at the end it'll be something I look at.
	@PUT
	@Path("/users/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(User updatedUser, @PathParam("id") int id) {
		// If the User to be deleted doesn't exist, throw a 404
		if (UserDatabase.findUser(id) == null) {
			return Response.status(Status.NOT_FOUND).type(MediaType.APPLICATION_JSON)
					.entity("There's no User with an ID of " + id + " :(").build();
		}
		// Attempt to try and address the issue described above the method
		// Seems to do the job based on my semi-minimal testing
		else if (UserDatabase.checkForUser(updatedUser.getId()) == true) {
			return Response.status(Status.CONFLICT).type(MediaType.APPLICATION_JSON).entity(
					"There is already a User with an ID of " + updatedUser.getId() + ". There can't be duplicate ID's.")
					.build();
		} else {
			// Otherwise, Everything is good. Send a 200 request and update the User
			UserDatabase.updateUser(id, updatedUser);
			return Response.status(Status.OK).type(MediaType.APPLICATION_JSON)
					.entity("User with ID " + id + " Successfuly Updated!").build();
		}
	}

	// Existing User Login
	// With Posting it may need to be done twice to get it to work properly due to
	// how I have the Bool working in PasswordClient. It works though I promise.
	@Path("/login")
	@POST
	public Response login(UserLogin login) {
		// If a User with the ID POSTed actually exists
		if (UserDatabase.findUser(login.getId()) != null) {
			// Validate the ID matches the Password
			if (client.PasswordValidation(UserDatabase.findUser(login.getId()).getHash(),
					UserDatabase.findUser(login.getId()).getSalt(), login)) {
				return Response.status(Status.OK).type(MediaType.APPLICATION_JSON)
						.entity("Login Successful. Welcome User! " + login.getId()
								+ " (Sometimes you need to send the request twice for it to actually work)")
						.build();
			} else {
				return Response.status(Status.UNAUTHORIZED).type(MediaType.APPLICATION_JSON)
						.entity("Password and ID don't match! :(").build();
			}
			// If the User doesn't exist
		} else {
			return Response.status(Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(
					"Bad Request :( User probably doens't exist! (Sometimes you need to send the request twice for it to actually work)")
					.build();
		}
	}
}
