package ie.gmit.outputs;

import ie.gmit.ds.HashResponse;

public class RequiredOutputs {

	// Just the output for the first part of the project
	public String FirstOutput(HashResponse result) {
		String firstOutput = "\n*Your ID = " + result.getUserId() + "\n*Your Hashed Password = "
				+ result.getHashedPassword() + "\n*Password Salt = " + result.getSalt();
		return firstOutput;
	}

	// Outputs for the second part of the project
	public String SecondOutput(HashResponse result) {
		String secondOutput = "*Your ID = " + result.getUserId() + "\n*Your Hashed Password = "
				+ result.getHashedPassword() + "\n*Password Salt = " + result.getSalt();
		return secondOutput;
	}
}
