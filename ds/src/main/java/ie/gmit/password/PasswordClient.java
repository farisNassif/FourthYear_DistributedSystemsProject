package ie.gmit.password;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import ie.gmit.client.Clientable;
import ie.gmit.ds.HashRequest;
import ie.gmit.ds.HashResponse;
import ie.gmit.ds.PasswordServiceGrpc;
import ie.gmit.outputs.RequiredOutputs;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class PasswordClient implements Clientable {

	// For testing purposes
	Scanner scanner = new Scanner(System.in);
	RequiredOutputs output = new RequiredOutputs();
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

		// The first output required for the project
		logger.info(output.FirstOutput(result));

		try {
			shutdown();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
