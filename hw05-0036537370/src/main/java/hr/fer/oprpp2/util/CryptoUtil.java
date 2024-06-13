package hr.fer.oprpp2.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Util class for cryptography.
 */
public class CryptoUtil {

    /**
     * Method for encrypting a single string value.
     * @param value Value to be encrypted
     * @return Encrypted value
     * @throws NoSuchAlgorithmException Encryption exception
     */
    public static String encrypt(String value) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");

        return Base64.getEncoder().encodeToString(md.digest(value.getBytes()));
    }

}
