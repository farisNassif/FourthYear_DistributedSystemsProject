package ie.gmit.password;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import ie.gmit.client.Clientable;
import ie.gmit.ds.HashRequest;
import ie.gmit.ds.HashResponse;
import ie.gmit.ds.PasswordServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class PasswordClient implements Clientable {

	// For testing purposes
	Scanner scanner = new Scanner(System.in);

	private static final Logger logger = Logger.getLogger(PasswordClient.class.getName());
	private final ManagedChannel channel;
	private final PasswordServiceGrpc.PasswordServiceStub asyncPasswordService;
	private final PasswordServiceGrpc.PasswordServiceBlockingStub syncPasswordService;

	public PasswordClient(String host, int port) {
		channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
		syncPasswordService = PasswordServiceGrpc.newBlockingStub(channel);
		asyncPasswordService = PasswordServiceGrpc.newStub(channel);
	}

	public void shutdown() throws InterruptedException {
		channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
	}

	// Prompts client input, serves synchPwService, hashes input password, returns
	// user details
	// [For testing purposes only]
	public void Login() {
		System.out.print("[Testing Purposes] Enter ID: ");
		int id = scanner.nextInt();
		System.out.print("[Testing Purposes] Enter Password: ");
		String pw = scanner.next();
		// To Server
		HashRequest h = HashRequest.newBuilder().setUserId(id).setPassword(pw).build();
		// From Server
		HashResponse result = HashResponse.newBuilder().getDefaultInstanceForType();

		// result = asyncPasswordService.hash(h);
		result = syncPasswordService.hash(h);

		logger.info("*Your ID = " + result.getUserId());
		logger.info("*Your Password = " + pw);
		logger.info("*Your password hashcode = " + result.getHashedPassword().hashCode());
		try {
			shutdown();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
