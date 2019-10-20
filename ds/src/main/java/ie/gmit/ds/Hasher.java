package ie.gmit.ds;

import ie.gmit.password.Passwords;

/**
 * Hasher class that contains three methods
 * {@link #actualPassword(HashRequest)}, {@link #salt(HashRequest)},
 * {@link #hashedPassword(char[], byte[])}
 * 
 * @author Faris Nassif
 * 
 */
public class Hasher {

	/**
	 * @param actualPassword the password entered by the user
	 *
	 * @return char array of password
	 */
	public char[] actualPassword(String actualPassword) {
		char[] hashPassword = actualPassword.toCharArray();
		return hashPassword;
	}

	/**
	 * Invokes {@link Passwords#getNextSalt()}
	 * 
	 * @param result the result object of HashResponse<br>
	 *
	 * @return salt
	 */
	public byte[] salt(HashRequest request) {
		byte[] salt = Passwords.getNextSalt();
		return salt;
	}

	/**
	 * Returns a random salt to be used to hash a password.
	 *
	 * @param actualPassword the result of {@link #actualPassword(HashRequest)}
	 * @param salt           the result of {@link #salt(HashRequest)}
	 * @return hashedPassword
	 */
	public byte[] hashedPassword(char[] actualPassword, byte[] salt) {
		byte[] hashedPassword = Passwords.hash(actualPassword, salt);
		return hashedPassword;
	}
}
