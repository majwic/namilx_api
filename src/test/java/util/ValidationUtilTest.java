package util;

import com.majwic.exception.FormatException;
import com.majwic.util.ValidationUtil;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class ValidationUtilTest {

    @Test
    public void testValidateDisplayNameValid() {
        String displayName = "John Doe";
        assertEquals(displayName, ValidationUtil.validateDisplayName(displayName));
    }

    @Test
    public void testValidateDisplayNameInvalidEmpty() {
        Exception exception = assertThrows(FormatException.class, () -> {
            ValidationUtil.validateDisplayName("");
        });

        assertEquals("Profile displayName cannot be empty", exception.getMessage());
    }

    @Test
    public void testValidateDisplayNameInvalidFormat() {
        Exception exception = assertThrows(FormatException.class, () -> {
            ValidationUtil.validateDisplayName("John_Doe123!");
        });

        assertEquals("Profile displayName has invalid formatting", exception.getMessage());
    }

    @Test
    public void testValidateEmailValid() {
        String email = "test@example.com";
        assertEquals(email, ValidationUtil.validateEmail(email));
    }

    @Test
    public void testValidateEmailInvalidEmpty() {
        Exception exception = assertThrows(FormatException.class, () -> {
            ValidationUtil.validateEmail("");
        });

        assertEquals("Profile email cannot be empty", exception.getMessage());
    }

    @Test
    public void testValidateEmailInvalidFormat() {
        Exception exception = assertThrows(FormatException.class, () -> {
            ValidationUtil.validateEmail("invalid-email.com");
        });

        assertEquals("Profile email has invalid formatting", exception.getMessage());
    }

    @Test
    public void testValidatePasswordValid() {
        String password = "Password123!";
        assertEquals(password, ValidationUtil.validatePassword(password));
    }

    @Test
    public void testValidatePasswordInvalidEmpty() {
        Exception exception = assertThrows(FormatException.class, () -> {
            ValidationUtil.validatePassword("");
        });

        assertEquals("Profile password cannot be empty", exception.getMessage());
    }

    @Test
    public void testValidatePasswordInvalidFormat() {
        Exception exception = assertThrows(FormatException.class, () -> {
            ValidationUtil.validatePassword("password");
        });

        assertEquals("Profile password has invalid formatting", exception.getMessage());
    }

    @Test
    public void testValidateRequiredFieldsValid() {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("username", "john_doe");
        requestBody.put("age", 30);

        Map<String, Class<?>> requiredFields = new HashMap<>();
        requiredFields.put("username", String.class);
        requiredFields.put("age", Integer.class);

        assertDoesNotThrow(() -> ValidationUtil.validateRequiredFields(requestBody, requiredFields));
    }

    @Test
    public void testValidateRequiredFieldsMissingField() {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("username", "john_doe");

        Map<String, Class<?>> requiredFields = new HashMap<>();
        requiredFields.put("username", String.class);
        requiredFields.put("age", Integer.class);

        Exception exception = assertThrows(FormatException.class, () -> {
            ValidationUtil.validateRequiredFields(requestBody, requiredFields);
        });

        assertEquals("The 'age' field is required", exception.getMessage());
    }

    @Test
    public void testValidateRequiredFieldsInvalidType() {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("username", "john_doe");
        requestBody.put("age", "thirty");

        Map<String, Class<?>> requiredFields = new HashMap<>();
        requiredFields.put("username", String.class);
        requiredFields.put("age", Integer.class);

        Exception exception = assertThrows(FormatException.class, () -> {
            ValidationUtil.validateRequiredFields(requestBody, requiredFields);
        });

        assertEquals("The 'age' field must be of type Integer", exception.getMessage());
    }
}
