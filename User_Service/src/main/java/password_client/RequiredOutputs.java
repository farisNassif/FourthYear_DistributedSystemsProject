package password_client;

import java.util.logging.Logger;

import ie.gmit.part_two.HashRequest;
import ie.gmit.part_two.HashResponse;

/**
 * Utility class that just has outputs for key parts of the project. Contains
 * the methods {@link #FirstOutput(HashRequest)} and {@link #SecondOutput(int)}
 * 
 * @author Faris Nassif
 */
public class RequiredOutputs {
	private static final Logger logger = Logger.getLogger(RequiredOutputs.class.getName());

	/**
	 * @param match conditional int for switch (Switches apparently don't work with
	 *              booleans)
	 * @return a String that contains relevant information for the user
	 */
	public void CheckMatch(int match) {
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
