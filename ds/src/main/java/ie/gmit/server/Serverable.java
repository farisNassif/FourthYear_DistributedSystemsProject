package ie.gmit.server;

public abstract interface Serverable {

	public abstract void stop();

	public abstract void start() throws Throwable;

}
