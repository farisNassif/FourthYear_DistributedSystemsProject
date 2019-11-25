package ie.gmit.outputs;

import java.util.logging.Logger;
import ie.gmit.ds.HashRequest;
import ie.gmit.ds.HashResponse;

/**
 * Utility class that just has outputs for key parts of the project.
 * Contains the methods {@link #FirstOutput(HashRequest)} and {@link #SecondOutput(int)}
 * @author Faris Nassif
 */
public class RequiredOutputs {
	private static final Logger logger = Logger.getLogger(RequiredOutputs.class.getName());
	/**
	 * Returns a compiled String that has all relevant information to the user.
	 *
	 * @param result the result object of HashResponse
	 * @return a String that contains relevant information for the user
	 */
	public String FirstOutput(HashResponse result) {
		// String firstOutput = "\n*Your ID = " + result.getUserId() + "\n*Your Hashed Password = "
		// 		+ result.getHashedPassword().hashCode() + "\n*Password Salt = " + result.getSalt().hashCode();
		String firstOutput = "";
		return firstOutput;
	}

	/**
	 * @param match conditional int for switch (Switches don't work with booleans)
	 * @return a String that contains relevant information for the user
	 */
	public void SecondOutput(int match) {
		String SecondOutput = "";
		switch (match) {
		case 1:
			SecondOutput = "Password is correct!";
			logger.info(SecondOutput);
			break;
		case 2:
			SecondOutput = "Does not match!";
			logger.info(SecondOutput);
		}
	}
}
