package ie.gmit.server;

import ie.gmit.password.PasswordServer;

public class ServerRunner {
	public static void main(String[] args) throws Throwable {
		final PasswordServer passwordServer = new PasswordServer();
		passwordServer.start();
		passwordServer.blockUntilShutdown();
	}
}
