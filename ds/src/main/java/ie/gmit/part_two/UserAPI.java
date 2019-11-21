package ie.gmit.part_two;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserAPI {
	
	private HashMap<Integer, User> userMap = new HashMap<>();
	
    public UserAPI() {     
    	User plsWork = new User(1, "joe", "bigjoe@gmail.com", "secretpw");
    	userMap.put(plsWork.getId(), plsWork);
    }
	
    // Gets current users
	@GET
	public List<User> getUsers() {

		return new ArrayList<User>(userMap.values());
	}
	
	// Adds new user
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(User newUser) {
        userMap.put(newUser.getId(), newUser);

        String r = "user id: " + newUser.getId();
        return Response.status(201).entity(r).build();
    }
    
    // Existing user login
	@Path("/login")
	@POST
	public Response login(UserLogin login)
	{
		  // Pretty long winded, but it does the job for testing at least
		  // Basically if the {ID, Password} I post via postman matches what's in the hashmap "log" the person in
		  if((userMap.get(login.getId()) != null) && (userMap.get(login.getId()).getPassword().equals(login.getPassword())))
		  {
		        String r = "Login Successful, welcome user with ID: " + login.getId();
		        return Response.status(201).entity(r).build();
		  }
		  // Otherwise not successful
		  else
		  {
		        String r = "Login not Successful!! lmao";
		        return Response.status(201).entity(r).build();
		  }
	}
}
