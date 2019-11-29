package password_client;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.LogRecord;
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
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;

/**
 * Passwords and Password hashing are handled through this class. The class
 * contains the method {@link #Generation()} and
 * {@link #PasswordValidation(String, String, UserLogin)} <br>
 * <br>
 * 
 * @author Faris Nassif
 */
public class PasswordClient {
	Scanner scanner = new Scanner(System.in);

	private static final Logger logger = Logger.getLogger(PasswordClient.class.getName());
	// Result for Validation. This is really bad I know and wouldn't be done in a
	// real world situation, I'll talk a bit about it in my readme since i'm
	// pretty sure it's the reason (or one of the reasons) for the double request
	// validation issue
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

	// Hash generation method - Async
	// Adds the user and generates a hash/salt (If the Server is running)
	public void Generation(final User userv) throws InterruptedException, UnsupportedEncodingException {
		StreamObserver<HashResponse> responseObserver = new StreamObserver<HashResponse>() {
			@Override
			public void onNext(HashResponse value) {

				// Adding a new user with the hashedPW and Salt as additional attributes
				// Issues with UTF-8, had to convert charset to iso-8859-1 since it wasn't
				// allowing logins
				// https://stackoverflow.com/questions/655891/converting-utf-8-to-iso-8859-1-in-java-how-to-keep-it-as-single-byte
				// https://stackoverflow.com/questions/7048745/what-is-the-difference-between-utf-8-and-iso-8859-1
				try {
					User user;
					user = new User(userv.getId(), userv.getName(), userv.getEmail(),
							value.getHashedPassword().toString("ISO-8859-1"), value.getSalt().toString("ISO-8859-1"));

					// Adding it to the Database (hashmap)
					UserDatabase.addUser(userv.getId(), user);

					// Just prints to the Console for testing
					logger.info(value.getSalt().toString("ISO-8859-1"));
					logger.info(value.getHashedPassword().toString("ISO-8859-1"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onError(Throwable t) {
				// logger.log(Level.WARNING, "RPC Error: {0}", t);
			}

			@Override
			public void onCompleted() {
				// logger.info("Finished");
				// try {
				// shutdown();
				// } catch (InterruptedException e) {
				// e.printStackTrace();
				// }
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
	// Async, should be Sync
	public boolean PasswordValidation(String passwordHash, String salt, UserLogin user) {
		// Need initialization on these
		ByteString saltByteString = null;
		ByteString hashByteString = null;
		try {
			// Have to keep the same chartype from Generation()
			saltByteString = ByteString.copyFrom(salt.getBytes("ISO-8859-1"));
			hashByteString = ByteString.copyFrom(passwordHash.getBytes("ISO-8859-1"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		StreamObserver<BoolValue> responseObserver = new StreamObserver<BoolValue>() {
			@Override
			public void onNext(BoolValue value) {
				if (value.getValue()) {
					// Password/ID Match
					res = true;
				} else {
					// Password/ID Don't Match
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

		// I've tried and tried to make the synchronous call work but I'm just going
		// wrong everytime I try to do something, it's hanging even after I implement
		// appropriate req/res
		asyncPasswordService.validate(ValidateRequest.newBuilder().setPassword(user.getPassword())
				.setHashedPassword(hashByteString).setSalt(saltByteString).build(), responseObserver);

		return res;
	}
}
