package ie.gmit.password;

import java.util.logging.Logger;

import ie.gmit.interfaces.Serverable;
import ie.gmit.runners.ServerRunner;
import io.grpc.Server;
import io.grpc.ServerBuilder;

/**
 * Body for the ServerRunner. Extends {@link ServerRunner} implements
 * {@link Serverable}
 * 
 * @author Faris Nassif
 */
public class PasswordServer extends ServerRunner implements Serverable {
	private Server grpcServer;
	private static final Logger logger = Logger.getLogger(PasswordServer.class.getName());
	// private static final int PORT = 50000;

	/**
	 * Starts the Server on the defined port
	 * 
	 */
	@Override
	public void start(int port) throws Throwable {
		grpcServer = ServerBuilder.forPort(port).addService(new PasswordServiceImpl()).build().start();
		logger.info("Server started, listening on " + port);
	}

	/**
	 * Stops the Server
	 * 
	 */
	@Override
	public void stop() {
		if (grpcServer != null) {
			grpcServer.shutdown();
		}
	}

	/**
	 * Await termination on the main thread since the grpc library uses daemon
	 * threads
	 */
	public void blockUntilShutdown() throws InterruptedException {
		if (grpcServer != null) {
			grpcServer.awaitTermination();
		}
	}
}
