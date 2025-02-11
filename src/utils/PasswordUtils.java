package utils;

public class PasswordUtils {
    public static String hashPassword(String password) {
        // Kembalikan password asli tanpa enkripsi
        return password;
    }

    public static boolean verifyPassword(String inputPassword, String storedPassword) {
        // Bandingkan password secara langsung
        return inputPassword.equals(storedPassword);
    }
}
