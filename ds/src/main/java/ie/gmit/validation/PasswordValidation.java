package ie.gmit.validation;

import com.google.protobuf.BoolValue;
import com.google.protobuf.ByteString;

import ie.gmit.ds.PasswordServiceGrpc.PasswordServiceStub;
import ie.gmit.ds.ValidatorRequest;
import ie.gmit.outputs.RequiredOutputs;
import io.grpc.stub.StreamObserver;

public class PasswordValidation {
	public PasswordValidation(ByteString passwordHash, ByteString salt, String pw,
			PasswordServiceStub asyncPasswordService) {
		// Just a class for outputting within the onNext() method
		final RequiredOutputs output = new RequiredOutputs();

		// Prints the hash of the password and the salt
		System.out.println("*Hashed Password = " + passwordHash.toByteArray() + "\n*Password Salt = " + salt.toByteArray());

		StreamObserver<BoolValue> responseObserver = new StreamObserver<BoolValue>() {
			@Override
			public void onNext(BoolValue value) {
				if (value.getValue()) {
					output.SecondOutput(1);
				} else {
					output.SecondOutput(2);
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
		asyncPasswordService.validate(
				ValidatorRequest.newBuilder().setPassword(pw).setHashedPassword(passwordHash).setSalt(salt).build(),
				responseObserver);
	}
}
