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
		
		//final UserHealthCheck healthCheck = new UserHealthCheck();	
		//environment.healthChecks().register("i_hate_java_sometimes", healthCheck);
	}
}
