package security;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility class for hashing passwords using SHA-512.
 * Converts a plain text string into a 128-character hexadecimal hash.
 */
public class PasswordHash {

    /**
     * Hashes the input string using SHA-512 and returns a 128-character hex string.
     * @param input the plain text string to encrypt
     * @return the SHA-512 hashed value as a hexadecimal string
     */
    public static String encrypt(String input) {
        try {
            // Create MessageDigest instance for SHA-512
            MessageDigest md = MessageDigest.getInstance("SHA-512");

            // Digest the input string into a byte array
            byte[] messageD = md.digest(input.getBytes());

            // Convert the byte array to a BigInteger
            BigInteger num = new BigInteger(1, messageD);

            // Convert BigInteger to hexadecimal string
            String hashtext = num.toString(16);

            // Ensure the hash is exactly 128 characters by padding with leading zeros
            while (hashtext.length() < 128) {
				hashtext = "0" + hashtext;
			}

            return hashtext;

        } catch (NoSuchAlgorithmException e) {
            // Wrap checked exception in unchecked RuntimeException
            throw new RuntimeException(e);
        }
    }
}
