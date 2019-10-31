package ie.gmit.password;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import com.google.protobuf.ByteString;

import ie.gmit.ds.HashRequest;
import ie.gmit.ds.HashResponse;
import ie.gmit.ds.PasswordServiceGrpc;
import ie.gmit.outputs.RequiredOutputs;
import ie.gmit.validation.PasswordValidation;
import ie.gmit.interfaces.Clientable;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

/**
 * Passwords and Password hashing are handled through this class. The class is
 * also home to the method {@link #Login()} which runs the program and is called
 * from {@link ClientRunner#main()} <br>
 * <br>
 * Implements {@link Clientable}
 * 
 * @author Faris Nassif
 */
public class PasswordClient implements Clientable {
	private ByteString hashedPassword;
	private ByteString salt;
	private int id = -1;
	private String enteredPassword;

	Scanner scanner = new Scanner(System.in);
	RequiredOutputs output = new RequiredOutputs();
	private static final Logger logger = Logger.getLogger(PasswordClient.class.getName());
	private ManagedChannel channel;
	private PasswordServiceGrpc.PasswordServiceStub asyncPasswordService;
	private PasswordServiceGrpc.PasswordServiceBlockingStub syncPasswordService;

	public PasswordClient(String host, int port) {
		channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
		syncPasswordService = PasswordServiceGrpc.newBlockingStub(channel);
		asyncPasswordService = PasswordServiceGrpc.newStub(channel);
	}

	public PasswordClient() {
		
	}

	public void shutdown() throws InterruptedException {
		channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
	}

	/**
	 * Sequential call of password validation at the end of the method
	 * 
	 */
	public void Login() {
		System.out.print("[Testing Purposes] Enter ID: ");
		id = scanner.nextInt();
		
		while (id <= 0)
		{	
			System.out.print("[Testing Purposes] ID must be greater than 0: ");
			id = scanner.nextInt();
		}
		
		System.out.print("[Testing Purposes] Enter Password: ");
		enteredPassword = scanner.next();

		try {
			// Hash method taking userId and password
			HashRequest hashRequest = HashRequest.newBuilder().setUserId(id).setPassword(enteredPassword).build();
			// The result of the method above
			HashResponse result = HashResponse.newBuilder().getDefaultInstanceForType();
			
			result = syncPasswordService.hash(hashRequest);

			hashedPassword = result.getHashedPassword();
			salt = result.getSalt();

			// The first part required for the project [Part 1]
			logger.info(output.FirstOutput(result));

			// The second part required for the project [Part 2]
			@SuppressWarnings("unused")
			PasswordValidation validator = new PasswordValidation(result.getHashedPassword(), result.getSalt(),
					enteredPassword, asyncPasswordService);

		} catch (RuntimeException runtimeException){
			logger.info(runtimeException.getLocalizedMessage());
		}
	}
}
