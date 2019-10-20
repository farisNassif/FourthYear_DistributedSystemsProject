package ie.gmit.ds;

import ie.gmit.password.PasswordClient;

/**
 * Runner class for the Client
 * 
 * @author Faris Nassif
 */
public class ClientRunner {

	public static void main(String[] args) throws Exception {
		PasswordClient client = new PasswordClient("localhost", 50551);
		try {
			// Client Connection
			client.Login();
		} finally {
			// Wait for the thread to die
			Thread.currentThread().join();
		}
	}
}
