package ie.gmit.runners;

import java.util.Scanner;

import ie.gmit.password.PasswordServer;

/**
 * Runner class for the Server
 * For Part1 this should be ran, and a client then connected to it
 * 
 * @throws Throwable
 * @author Faris Nassif
 */
public class ServerRunner {
	public static void main(String[] args) throws Throwable {
    	@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
    	System.out.print("Enter port number to listen for client on: ");
    	int port = scanner.nextInt();
    	
		final PasswordServer passwordServer = new PasswordServer();
		passwordServer.start(port);
		passwordServer.blockUntilShutdown();
		
	}
}
