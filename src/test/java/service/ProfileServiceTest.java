package service;

import com.majwic.exception.ConflictException;
import com.majwic.exception.UnauthorizedException;
import com.majwic.model.Profile;
import com.majwic.model.Role;
import com.majwic.repository.ProfileRepository;
import com.majwic.service.ProfileService;
import com.majwic.util.FieldName;
import com.majwic.util.PasswordUtil;
import com.majwic.util.ServiceUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProfileServiceTest {

    private ProfileRepository profileRepository;
    private ServiceUtil serviceUtil;
    private ProfileService profileService;

    @BeforeEach
    public void setUp() {
        profileRepository = mock(ProfileRepository.class);
        serviceUtil = mock(ServiceUtil.class);
        profileService = new ProfileService(profileRepository, serviceUtil);
    }

    @Test
    public void testRead() {
        Long profileId = 1L;
        Profile profile = new Profile();
        profile.setId(profileId);
        profile.setDisplayName("John Doe");
        profile.setEmail("test@example.com");

        Role role = new Role();
        role.setId(1L);
        role.setName("USER");
        profile.setRoles(List.of(role));

        when(serviceUtil.getProfileByIdOrThrow(profileId)).thenReturn(profile);

        String response = profileService.read(profileId, true);

        String expectedResponse = "{\"id\":1,\"displayName\":\"John Doe\",\"email\":\"test@example.com\",\"roles\":" +
            "[{\"id\":1,\"name\":\"USER\"}]}";
        assertEquals(expectedResponse, response);
    }

    @Test
    public void testCreateSuccess() {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put(FieldName.EMAIL, "test@example.com");
        requestBody.put(FieldName.PASSWORD, "Password1");

        Role userRole = new Role();
        userRole.setName("USER");
        userRole.setId(1L);
        Profile savedProfile = new Profile();
        savedProfile.setEmail("test@example.com");
        savedProfile.setPassword(PasswordUtil.hashPassword("Password1"));
        savedProfile.setDisplayName("not named");
        savedProfile.setRoles(List.of(userRole));

        when(profileRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(serviceUtil.getRoleByNameOrThrow("USER")).thenReturn(userRole);
        when(profileRepository.save(any(Profile.class))).thenReturn(savedProfile);

        String response = profileService.create(requestBody);

        String expectedResponse = "{\"id\":null,\"displayName\":\"not named\",\"email\":\"test@example.com\",\"roles\":[{\"id\":1,\"name\":\"USER\"}]}";
        assertEquals(expectedResponse, response);
    }

    @Test
    public void testCreateProfileExists() {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put(FieldName.EMAIL, "test@example.com");
        requestBody.put(FieldName.PASSWORD, "Password1");

        when(profileRepository.existsByEmail("test@example.com")).thenReturn(true);

        Exception exception = assertThrows(ConflictException.class, () -> {
            profileService.create(requestBody);
        });

        assertEquals("Profile already exists with email", exception.getMessage());
    }

    @Test
    public void testUpdateSuccess() {
        Long profileId = 1L;
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put(FieldName.CURRENT_PASSWORD, "currentPassword1");
        requestBody.put(FieldName.DISPLAY_NAME, "New Display Name");
        requestBody.put(FieldName.NEW_PASSWORD, "newPassword1");

        Profile profile = new Profile();
        profile.setId(profileId);
        profile.setPassword(PasswordUtil.hashPassword("currentPassword1"));
        profile.setDisplayName("Old Display Name");
        profile.setEmail("old@example.com");

        Role role = new Role();
        role.setId(1L);
        role.setName("USER");
        profile.setRoles(List.of(role));

        when(serviceUtil.getProfileByIdOrThrow(profileId)).thenReturn(profile);
        when(profileRepository.save(any(Profile.class))).thenReturn(profile);

        String response = profileService.update(profileId, requestBody);

        assertEquals("{\"id\":1,\"displayName\":\"New Display Name\",\"email\":\"old@example.com\",\"roles\":" +
            "[{\"id\":1,\"name\":\"USER\"}]}", response);
    }

    @Test
    public void testUpdateIncorrectPassword() {
        Long profileId = 1L;
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put(FieldName.CURRENT_PASSWORD, "wrongPassword");

        Profile profile = new Profile();
        profile.setId(profileId);
        profile.setPassword(PasswordUtil.hashPassword("correctPassword"));

        when(serviceUtil.getProfileByIdOrThrow(profileId)).thenReturn(profile);

        Exception exception = assertThrows(UnauthorizedException.class, () -> {
            profileService.update(profileId, requestBody);
        });

        assertEquals("The 'currentPassword' field is incorrect", exception.getMessage());
    }

    @Test
    public void testDeleteSuccess() {
        Long profileId = 1L;
        String password = "password123";

        Profile profile = new Profile();
        profile.setId(profileId);
        profile.setPassword(PasswordUtil.hashPassword(password));

        when(serviceUtil.getProfileByIdOrThrow(profileId)).thenReturn(profile);

        profileService.delete(profileId, password);

        verify(profileRepository).delete(profile);
    }

    @Test
    public void testDeleteIncorrectPassword() {
        Long profileId = 1L;
        String password = "wrongPassword";

        Profile profile = new Profile();
        profile.setId(profileId);
        profile.setPassword(PasswordUtil.hashPassword("correctPassword"));

        when(serviceUtil.getProfileByIdOrThrow(profileId)).thenReturn(profile);

        Exception exception = assertThrows(UnauthorizedException.class, () -> {
            profileService.delete(profileId, password);
        });

        assertEquals("Incorrect password", exception.getMessage());
    }
}
