package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class PasswordUtils {
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Gagal mengenkripsi password: " + e.getMessage());
            return null;
        }
    }

    public static boolean verifyPassword(String inputPassword, String storedHash) {
        String hashedInput = hashPassword(inputPassword);
        return hashedInput != null && hashedInput.equals(storedHash);
    }
}
