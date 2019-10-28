package runners;

import java.util.Scanner;

import ie.gmit.password.PasswordClient;

/**
 * Runner class for Running the Client
 * 
 * @author Faris Nassif
 */
public class ClientRunner {

	public static void main(String[] args) throws Exception {
    	@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
    	System.out.print("Enter port number: ");
    	int port = scanner.nextInt();
		PasswordClient client = new PasswordClient("localhost", port);
		try {
			System.out.println("Connection to Server established ...");
			// Client Connection
			client.Login();
		} finally {
			// Wait for the thread to die
			Thread.currentThread().join();
		}
	}
}
