package security;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHash {

	// Returns the hashed value of a given String
	public static String encrypt(String input) {
		try {

			MessageDigest md = MessageDigest.getInstance("SHA-512");
			byte[] messageD = md.digest(input.getBytes());
			BigInteger num = new BigInteger(1, messageD);
			String hashtext = num.toString(16);

			// Hash must be 128 characters long in hex
			while(hashtext.length() < 128) {
				hashtext = "0" + hashtext;
			}


			return hashtext;

		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
}
