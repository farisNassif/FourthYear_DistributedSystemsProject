package ie.gmit.server;

/**
 * Interface that is implemented by Servers
 * 
 * @author Faris Nassif
 */
public abstract interface Serverable {

	public abstract void stop();

	public abstract void start(int port) throws Throwable;

}
