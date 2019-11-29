package password_client;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
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
	// Result for Validationl
	static boolean res;

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

	// Adds the user and generates a hash/salt (If the Server is running)
	public void Generation(final User userv) {
		StreamObserver<HashResponse> responseObserver = new StreamObserver<HashResponse>() {
			// Most of this code was copied from my part 1, will have to change it as I go
			@Override
			public void onNext(HashResponse value) {
				// Adding a new user with the hashedPW and Salt as additional attributes
				User user = new User(userv.getId(), userv.getName(), userv.getEmail(), userv.getPassword(),
						value.getHashedPassword(), value.getSalt());

				// Adding it to the Database (hashmap)
				UserDatabase.addUser(userv.getId(), user);
				
				// Just prints to the Console for testing
				logger.info(value.getHashedPassword().toByteArray().toString());
				logger.info(value.getSalt().toByteArray().toString());
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
	public boolean PasswordValidation(ByteString passwordHash, ByteString salt, UserLogin user) {
		// Just a class for outputting within the onNext() method
		final RequiredOutputs output = new RequiredOutputs();

		// Prints the hash of the password and the salt (For testing!)
		System.out.println(
				"*Hashed Password = " + passwordHash.toByteArray() + "\n*Password Salt = " + salt.toByteArray());

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
				.setHashedPassword(passwordHash).setSalt(salt).build(), responseObserver);
		return res;
	}
}