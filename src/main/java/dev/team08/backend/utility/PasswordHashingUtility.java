package dev.team08.backend.utility;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHashingUtility {
    // Hash a password using BCrypt
    public static String hashPassword(String plainTextPassword) {
        if (plainTextPassword == null || plainTextPassword.isBlank()) {
            throw new IllegalArgumentException("Password must not be empty");
        }
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt(12));
    }

    // Check that an unhashed password matches one that has been hashed
    public static boolean verifyPassword(String plainTextPassword, String hashedPassword) {
        if (plainTextPassword == null || hashedPassword == null) {
            return false;
        }
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }
}
