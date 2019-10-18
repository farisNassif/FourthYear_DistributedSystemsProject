package ie.gmit.ds;

import ie.gmit.password.Passwords;

public class Hasher {

	public char[] hashPassword(HashRequest request) {
		char[] hashPassword = request.getPassword().toCharArray();
		return hashPassword;
	}

	public byte[] salt(HashRequest request) {
		byte[] salt = Passwords.getNextSalt();
		return salt;
	}

	public byte[] hashedPassword(char[] hashPassword, byte[] salt) {
		byte[] hashedPassword = Passwords.hash(hashPassword, salt);
		return hashedPassword;
	}
}
