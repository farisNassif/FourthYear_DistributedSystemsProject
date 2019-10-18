package ie.gmit.password;

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

		// Delegation call to Hasher
		char[] hashPassword = hasher.hashPassword(request);
		byte[] salt = hasher.salt(request);
		byte[] hashedPassword = hasher.hashedPassword(hashPassword, salt);

		// Bytestrings are required params of .setHashedPassword and .setSalt
		ByteString hashedPasswordByteString = ByteString.copyFrom(hashedPassword);
		ByteString saltByteString = ByteString.copyFrom(hashedPassword);
		// Invoke hashing methods and pass the appropriate ByteStrings 
		responseObserver.onNext(HashResponse.newBuilder().setUserId(request.getUserId())
				.setHashedPassword(hashedPasswordByteString).setSalt(saltByteString).build());

		responseObserver.onCompleted();
	}
}
