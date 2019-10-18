package ie.gmit.server;

public interface Serverable {

	public abstract void stop();

	public abstract void start() throws Throwable;

}
