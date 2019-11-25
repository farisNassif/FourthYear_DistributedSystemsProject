package ie.gmit.part_two;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.validation.Validator;
import ie.gmit.runners.ClientRunner;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserAPI {
	private HashMap<Integer, User> userMap = new HashMap<>();
	private final Validator validator;
	private ClientRunner client;

    private String HOST = "localhost";
    private int PORT = 9001;
	
	public UserAPI(Validator validator) throws InterruptedException {
		this.validator = validator;
		Scanner console = new Scanner(System.in);
        //System.out.println("Enter the Port you want to connect to: ");
        //int port = console.nextInt();
        //String host = console.next();

        // this.client.ClientRun(HOST, PORT);
     
	}

	// Gets current users
	@GET
	public Response getUsers() {
		return Response.ok(UserDatabase.findUsers()).build();
		// return new ArrayList<User>(userMap.values());
	}

	// Adds new user
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response add(User newUser) {
		UserDatabase.addUser(newUser.getId(), newUser);

		String r = "user id: " + newUser.getId();
		return Response.status(201).entity(r).build();
	}

	// Existing user login
	@Path("/login")
	@POST
	public Response login(UserLogin login) {
		// Pretty long winded, but it does the job for testing at least
		// Basically if the {ID, Password} I post via postman matches what's in the
		// hashmap "log" the person in
		if ((userMap.get(login.getId()) != null)
				&& (userMap.get(login.getId()).getPassword().equals(login.getPassword()))) {
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
