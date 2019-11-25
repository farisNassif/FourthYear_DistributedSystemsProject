package ie.gmit.part_two;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class UserMain extends Application<UserConfiguration> {

	public static void main(String[] args) throws Exception {
		new UserMain().run(args);
	}

	public void run(UserConfiguration userConfiguration, Environment environment) throws Exception {
		final UserAPI resource = new UserAPI();		
		environment.jersey().register(resource); 
		
		// Add the health check at some point 
	}
}
