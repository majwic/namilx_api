package com.majwic.util;

import com.majwic.exception.FormatException;
import com.majwic.model.Profile;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.regex.Pattern;

@Component
public class ValidationUtil {

    private static final Pattern EMAIL_REGEX = Pattern //
        .compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");

    private static final Pattern PASSWORD_REGEX = Pattern //
        .compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,250}$");

    private static final Pattern DISPLAY_NAME_REGEX = Pattern //
        .compile("^[\\w\\s]{3,50}$");

    public static String validateDisplayName(String displayName) {
        if (displayName == null || displayName.isEmpty()) {
            throw new FormatException("Profile displayName cannot be empty");
        }

        if (!DISPLAY_NAME_REGEX.matcher(displayName).matches()) {
            throw new FormatException("Profile displayName has invalid formatting");
        }

        return displayName;
    }

    public static String validateEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new FormatException("Profile email cannot be empty");
        }

        if (!EMAIL_REGEX.matcher(email).matches()) {
            throw new FormatException("Profile email has invalid formatting");
        }

        return email;
    }

    public static String validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new FormatException("Profile password cannot be empty");
        }

        if (!PASSWORD_REGEX.matcher(password).matches()) {
            throw new FormatException("Profile password has invalid formatting");
        }

        return password;
    }

    public static void validateRequiredFields(Map<String, Object> requestBody, Map<String, Class<?>> requiredFields) {
        for (Map.Entry<String, Class<?>> entry : requiredFields.entrySet()) {
            String field = entry.getKey();
            Class<?> expectedType = entry.getValue();

            if (!requestBody.containsKey(field)) {
                throw new FormatException("The '" + field + "' field is required");
            }

            Object value = requestBody.get(field);
            if (!expectedType.isInstance(value)) {
                throw new FormatException("The '" + field + "' field must be of type " + expectedType.getSimpleName());
            }
        }
    }
}
