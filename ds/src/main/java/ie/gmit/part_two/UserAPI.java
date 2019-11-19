package ie.gmit.part_two;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserAPI {

	List<User> users = Arrays.asList(new User(1, "joe", "joe@gmail.com", "pw"));

	@GET
	public List<User> getUsers() {

		return users;
	}
}
