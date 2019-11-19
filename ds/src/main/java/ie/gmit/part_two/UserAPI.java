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
	
	@GET
	public List<User> getUsers() {

		return new ArrayList<User>(userMap.values());
	}
	
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(User newUser) {

        //System.out.println("NEW" + newUser.getUserId());
        userMap.put(newUser.getId(), newUser);

        String r = "user id: " + newUser.getId();
        return Response.status(201).entity(r).build();
    }
}
