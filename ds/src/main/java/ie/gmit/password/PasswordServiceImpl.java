package ie.gmit.password;

import com.google.protobuf.BoolValue;
import com.google.protobuf.ByteString;

import ie.gmit.ds.HashRequest;
import ie.gmit.ds.HashResponse;
import ie.gmit.ds.Hasher;
import ie.gmit.ds.PasswordServiceGrpc;
import io.grpc.stub.StreamObserver;

public class PasswordServiceImpl extends PasswordServiceGrpc.PasswordServiceImplBase {

	Hasher hasher = new Hasher();

	/**
	 * Override the hash function from PasswordServiceGrpc
	 * 
	 */
	@Override
	public void hash(HashRequest request, StreamObserver<HashResponse> responseObserver) {

		String getPassword = request.getPassword();
		// Delegation call to Hasher
		char[] actualPassword = hasher.actualPassword(getPassword);
		byte[] salt = hasher.salt(request);
		byte[] hashedPassword = hasher.hashedPassword(actualPassword, salt);

		// Bytestrings are required params of .setHashedPassword and .setSalt
		ByteString hashedPasswordByteString = ByteString.copyFrom(hashedPassword);
		ByteString saltByteString = ByteString.copyFrom(salt);
		// Invoke hashing methods and pass the appropriate ByteStrings
		responseObserver.onNext(HashResponse.newBuilder().setUserId(request.getUserId())
				.setHashedPassword(hashedPasswordByteString).setSalt(saltByteString).build());

		responseObserver.onCompleted();
	}
	
	/**
	 * Override the validate function from PasswordServiceGrpc
	 * 
	 */
	@Override
	public void validate(ie.gmit.ds.ValidatorRequest request,
			io.grpc.stub.StreamObserver<com.google.protobuf.BoolValue> responseObserver) {
		try
		{
			ByteString hashedPasswordByteArray = request.getHashedPassword();
			ByteString hashedSalt = request.getSalt();

			String getPassword = request.getPassword();
			
			char[] actualPassword = getPassword.toCharArray();
			byte[] hashedPassword = hashedPasswordByteArray.toByteArray();
			byte[] salt = hashedSalt.toByteArray();
			
		    // Return true if everything is right
			if(Passwords.isExpectedPassword(actualPassword, salt, hashedPassword))
			{
				responseObserver.onNext(BoolValue.newBuilder().setValue(true).build());
			}
			// Return false if doesn't match
			else
			{	
				responseObserver.onNext(BoolValue.newBuilder().setValue(false).build());
			}
		}
		catch(RuntimeException ex)
		{
			responseObserver.onNext(BoolValue.newBuilder().setValue(false).build());
		}
	}
}
