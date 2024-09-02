package util;

import com.majwic.util.PasswordUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordUtilTest {

    @Test
    public void testHashPassword() {
        String plainPassword = "mySecretPassword";
        String hashedPassword = PasswordUtil.hashPassword(plainPassword);

        assertNotNull(hashedPassword);
        assertTrue(hashedPassword.startsWith("$2a$")); // BCrypt hashes start with "$2a$"
    }

    @Test
    public void testVerifyPassword() {
        String plainPassword = "mySecretPassword";
        String hashedPassword = PasswordUtil.hashPassword(plainPassword);

        assertTrue(PasswordUtil.verifyPassword(plainPassword, hashedPassword));
    }

    @Test
    public void testVerifyPasswordWithIncorrectPassword() {
        String plainPassword = "mySecretPassword";
        String incorrectPassword = "wrongPassword";
        String hashedPassword = PasswordUtil.hashPassword(plainPassword);

        assertFalse(PasswordUtil.verifyPassword(incorrectPassword, hashedPassword));
    }

    @Test
    public void testVerifyPasswordWithTamperedHash() {
        String plainPassword = "mySecretPassword";
        String hashedPassword = PasswordUtil.hashPassword(plainPassword);
        String tamperedHash = hashedPassword.substring(0, hashedPassword.length() - 1) + "X";

        assertFalse(PasswordUtil.verifyPassword(plainPassword, tamperedHash));
    }

    @Test
    public void testHashingProducesDifferentHashesForSamePassword() {
        String plainPassword = "mySecretPassword";
        String hashedPassword1 = PasswordUtil.hashPassword(plainPassword);
        String hashedPassword2 = PasswordUtil.hashPassword(plainPassword);

        assertNotEquals(hashedPassword1, hashedPassword2);
    }
}
