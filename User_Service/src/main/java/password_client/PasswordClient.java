package password_client;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.io.UnsupportedEncodingException;
import java.util.Map.Entry;

import com.google.protobuf.BoolValue;
import com.google.protobuf.ByteString;

import ie.gmit.part_two.HashRequest;
import ie.gmit.part_two.HashResponse;
import ie.gmit.part_two.PasswordServiceGrpc;
import ie.gmit.part_two.User;
import ie.gmit.part_two.UserDatabase;
import ie.gmit.part_two.UserLogin;
import ie.gmit.part_two.ValidateRequest;
import ie.gmit.part_two.PasswordServiceGrpc.PasswordServiceStub;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;

/**
 * Passwords and Password hashing are handled through this class. The class is
 * also home to the method {@link #Login()} which runs the program and is called
 * from {@link ClientRunner#main()} <br>
 * <br>
 * Implements {@link Clientable}
 * 
 * @author Faris Nassif
 */
public class PasswordClient {
	Scanner scanner = new Scanner(System.in);
	RequiredOutputs output = new RequiredOutputs();

	private static final Logger logger = Logger.getLogger(PasswordClient.class.getName());
	// Result for Validation (Change this from static!)
	static boolean res;
	private String pwHash;
	private String salt;

	private ManagedChannel channel;
	private PasswordServiceGrpc.PasswordServiceStub asyncPasswordService;
	private PasswordServiceGrpc.PasswordServiceBlockingStub syncPasswordService;

	// Client connection will be initiated here
	public PasswordClient(String host, int port) {
		channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();

		syncPasswordService = PasswordServiceGrpc.newBlockingStub(channel);
		asyncPasswordService = PasswordServiceGrpc.newStub(channel);
	}

	public void shutdown() throws InterruptedException {
		channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
	}

	// Hash generation method - Async
	// Adds the user and generates a hash/salt (If the Server is running)
	public void Generation(final User userv) throws InterruptedException, UnsupportedEncodingException {
		StreamObserver<HashResponse> responseObserver = new StreamObserver<HashResponse>() {
			@Override
			public void onNext(HashResponse value) {

				// Adding a new user with the hashedPW and Salt as additional attributes
				// Issues with UTF-8, had to convert charset to iso-8859-1
				// https://stackoverflow.com/questions/655891/converting-utf-8-to-iso-8859-1-in-java-how-to-keep-it-as-single-byte
				User user;
				try {
					user = new User(userv.getId(), userv.getName(), userv.getEmail(), userv.getPassword(),
							value.getHashedPassword().toString("ISO-8859-1"), value.getSalt().toString("ISO-8859-1"));

					// Adding it to the Database (hashmap)
					UserDatabase.addUser(userv.getId(), user);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// Just prints to the Console for testing
				try {
					logger.info(value.getSalt().toString("ISO-8859-1"));
					logger.info(value.getHashedPassword().toString("ISO-8859-1"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void onError(Throwable t) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onCompleted() {
				// TODO Auto-generated method stub
			}

		};
		try {
			asyncPasswordService.hash(
					HashRequest.newBuilder().setUserId(userv.getId()).setPassword(userv.getPassword()).build(),
					responseObserver);
		} catch (StatusRuntimeException ex) {
			// TODO Auto-generated method stub
		}
	}

	// Logging a User in and checking if the ID/Password match
	public boolean PasswordValidation(String passwordHash, String salt, UserLogin user) {
		// Just a class for outputting within the onNext() method
		final RequiredOutputs output = new RequiredOutputs();

		ByteString saltBs = null;
		ByteString hashBs = null;
		try {
			// Have to keep the same chartype from Generation()
			saltBs = ByteString.copyFrom(salt.getBytes("ISO-8859-1"));
			hashBs = ByteString.copyFrom(passwordHash.getBytes("ISO-8859-1"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		StreamObserver<BoolValue> responseObserver = new StreamObserver<BoolValue>() {
			@Override
			public void onNext(BoolValue value) {
				if (value.getValue()) {
					output.CheckMatch(1);
					res = true;
				} else {
					output.CheckMatch(2);
					res = false;
				}
			}

			@Override
			public void onError(Throwable t) {
				t.printStackTrace();
			}

			@Override
			public void onCompleted() {
				// channel.shutdown();
			}
		};
		asyncPasswordService.validate(ValidateRequest.newBuilder().setPassword(user.getPassword())
				.setHashedPassword(hashBs).setSalt(saltBs).build(), responseObserver);
		return res;
	}
}
