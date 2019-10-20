package ie.gmit.password;

import com.google.protobuf.BoolValue;
import com.google.protobuf.ByteString;

import ie.gmit.ds.HashRequest;
import ie.gmit.ds.HashResponse;
import ie.gmit.ds.Hasher;
import ie.gmit.ds.PasswordServiceGrpc;
import io.grpc.stub.StreamObserver;

public class PasswordService extends PasswordServiceGrpc.PasswordServiceImplBase {

	Hasher hasher = new Hasher();

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

	@Override
	public void validate(ie.gmit.ds.ValidatorRequest request,
			io.grpc.stub.StreamObserver<com.google.protobuf.BoolValue> responseObserver) {
		//super.validate(request, responseObserver)
		//get the info from the request
		try
		{
			ByteString hashedPasswordByteArray = request.getHashedPassword();
			ByteString hashedSalt = request.getSalt();

			//everything here to return boolean
			//need to get the string here before turning it into a char array
			String getPassword = request.getPassword();
			
			//three values for checking if the password is correct
			char[] actualPassword = getPassword.toCharArray();
			byte[] hashedPassword = hashedPasswordByteArray.toByteArray();
			byte[] salt = hashedSalt.toByteArray();
			
		    //call method, check, return true if applicable
			if(Passwords.isExpectedPassword(actualPassword, salt, hashedPassword))
			{
				responseObserver.onNext(BoolValue.newBuilder().setValue(true).build());
			}
			//method return false if salt does not confirm hashed password
			else
			{	
				responseObserver.onNext(BoolValue.newBuilder().setValue(false).build());
			}
		}
		//if there is a problem return false
		catch(RuntimeException ex)
		{
			responseObserver.onNext(BoolValue.newBuilder().setValue(false).build());
		}
	}
}
