package service;

import com.majwic.exception.FormatException;
import com.majwic.exception.ResourceNotFoundException;
import com.majwic.exception.UnauthorizedException;
import com.majwic.model.Profile;
import com.majwic.service.AuthService;
import com.majwic.util.FieldName;
import com.majwic.util.JwtTokenUtil;
import com.majwic.util.PasswordUtil;
import com.majwic.util.ServiceUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    private JwtTokenUtil jwtTokenUtil;
    private ServiceUtil serviceUtil;
    private AuthService authService;

    @BeforeEach
    public void setUp() {
        jwtTokenUtil = mock(JwtTokenUtil.class);
        serviceUtil = mock(ServiceUtil.class);
        authService = new AuthService(jwtTokenUtil, serviceUtil);
    }

    @Test
    public void testValidateSignInSuccess() {
        String email = "test@example.com";
        String password = "password123";
        String hashedPassword = PasswordUtil.hashPassword(password);
        Long userId = 1L;
        Profile profile = new Profile();
        profile.setEmail(email);
        profile.setPassword(hashedPassword);
        profile.setId(userId);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put(FieldName.EMAIL, email);
        requestBody.put(FieldName.PASSWORD, password);

        when(serviceUtil.getByEmailOrThrow(email)).thenReturn(profile);
        when(jwtTokenUtil.generateToken(userId.toString())).thenReturn("jwtToken");

        String token = authService.validateSignIn(requestBody);

        assertEquals("jwtToken", token);
        verify(serviceUtil).getByEmailOrThrow(email);
        verify(jwtTokenUtil).generateToken(userId.toString());
    }

    @Test
    public void testValidateSignInInvalidPassword() {
        String email = "test@example.com";
        String password = "password123";
        String wrongPassword = "wrongPassword";
        Long userId = 1L;
        Profile profile = new Profile();
        profile.setEmail(email);
        profile.setPassword(PasswordUtil.hashPassword(password));
        profile.setId(userId);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put(FieldName.EMAIL, email);
        requestBody.put(FieldName.PASSWORD, wrongPassword);

        when(serviceUtil.getByEmailOrThrow(email)).thenReturn(profile);

        Exception exception = assertThrows(UnauthorizedException.class, () -> {
            authService.validateSignIn(requestBody);
        });

        assertEquals("Incorrect password", exception.getMessage());
    }

    @Test
    public void testValidateSignInMissingEmail() {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put(FieldName.PASSWORD, "password123");

        Exception exception = assertThrows(FormatException.class, () -> {
            authService.validateSignIn(requestBody);
        });

        assertEquals("The 'email' field is required", exception.getMessage());
    }

    @Test
    public void testGetUserIdFromCookieSuccess() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Cookie cookie = new Cookie("jwtTokenNamilx", "jwtToken");
        when(request.getCookies()).thenReturn(new Cookie[]{cookie});
        when(jwtTokenUtil.getUserIdFromToken("jwtToken")).thenReturn("1");

        Long userId = authService.getUserIdFromCookie(request);

        assertEquals(1L, userId);
    }

    @Test
    public void testGetUserIdFromCookieMissingToken() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getCookies()).thenReturn(null);

        Exception exception = assertThrows(UnauthorizedException.class, () -> {
            authService.getUserIdFromCookie(request);
        });

        assertEquals("Session token is missing", exception.getMessage());
    }

    @Test
    public void testValidateProfileExists() {
        Long profileId = 1L;
        Profile profile = new Profile();
        profile.setId(profileId);

        when(serviceUtil.getProfileByIdOrThrow(profileId)).thenReturn(profile);

        assertDoesNotThrow(() -> authService.validateProfileExists(profileId));
    }

    @Test
    public void testValidateProfileExistsNotFound() {
        Long profileId = 1L;
        when(serviceUtil.getProfileByIdOrThrow(profileId)).thenThrow(new ResourceNotFoundException("Profile not found"));

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            authService.validateProfileExists(profileId);
        });

        assertEquals("Profile not found", exception.getMessage());
    }
}
