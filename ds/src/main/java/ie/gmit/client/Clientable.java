package ie.gmit.client;

/**
 * Interface that is implemented by Clients
 * 
 * @author Faris Nassif
 */
public interface Clientable {

	public abstract void shutdown() throws InterruptedException;

}
