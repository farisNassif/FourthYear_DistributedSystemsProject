package ie.gmit.server;

import ie.gmit.password.PasswordServer;

public class ServerRunner {
	public static void main(String[] args) throws Throwable {
		final PasswordServer inventoryServer = new PasswordServer();
		inventoryServer.start();
		inventoryServer.blockUntilShutdown();
	}
}
