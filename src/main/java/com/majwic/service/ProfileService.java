package com.majwic.service;

import com.majwic.exception.ConflictException;
import com.majwic.exception.FormatException;
import com.majwic.exception.UnauthorizedException;
import com.majwic.model.Role;
import com.majwic.util.*;
import com.majwic.model.Profile;
import com.majwic.repository.ProfileRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final ServiceUtil serviceUtil;

    public ProfileService(
        ProfileRepository profileRepository,
        ServiceUtil serviceUtil
    ) {
        this.profileRepository = profileRepository;
        this.serviceUtil = serviceUtil;
    }

    public String read(Long id, boolean hasCred) {
        Profile profile = serviceUtil.getProfileByIdOrThrow(id);
        return buildProfileResponse(profile, hasCred);
    }

    @Transactional
    public String create(Map<String, Object> requestBody) {
        Map<String, Class<?>> requiredFields = new HashMap<>();
        requiredFields.put(FieldName.EMAIL, String.class);
        requiredFields.put(FieldName.PASSWORD, String.class);
        ValidationUtil.validateRequiredFields(requestBody, requiredFields);

        String email = ValidationUtil.validateEmail((String) requestBody.get(FieldName.EMAIL));
        String password = ValidationUtil.validatePassword((String) requestBody.get(FieldName.PASSWORD));

        if (profileRepository.existsByEmail(email))
            throw new ConflictException("Profile already exists with email");

        String hashedPassword = PasswordUtil.hashPassword(password);
        Role userRole = serviceUtil.getRoleByNameOrThrow("USER");

        Profile profile = new Profile(email, hashedPassword, "not named", List.of(userRole));
        Profile savedProfile = profileRepository.save(profile);

        return buildProfileResponse(savedProfile, true);
    }

    @Transactional
    public String update(Long id, Map<String, Object> requestBody) {
        Map<String, Class<?>> requiredFields = new HashMap<>();
        requiredFields.put(FieldName.CURRENT_PASSWORD, String.class);
        ValidationUtil.validateRequiredFields(requestBody, requiredFields);

        Profile profile = serviceUtil.getProfileByIdOrThrow(id);

        String currentPassword = (String) requestBody.get(FieldName.CURRENT_PASSWORD);
        if (!PasswordUtil.verifyPassword(currentPassword, profile.getPassword())) {
            throw new UnauthorizedException("The 'currentPassword' field is incorrect");
        }

        handleFieldUpdate(requestBody, FieldName.DISPLAY_NAME, profile);
        handleFieldUpdate(requestBody, FieldName.EMAIL, profile);
        handleFieldUpdate(requestBody, FieldName.NEW_PASSWORD, profile);

        profileRepository.save(profile);

        return buildProfileResponse(profile, true);
    }

    @Transactional
    public void delete(Long id, String password) {
        Profile profile = serviceUtil.getProfileByIdOrThrow(id);

        if (!PasswordUtil.verifyPassword(password, profile.getPassword())) {
            throw new UnauthorizedException("Incorrect password");
        }

        profileRepository.delete(profile);
    }

    // === Private Helper Methods === //

    private void handleFieldUpdate(Map<String, Object> requestBody, String field, Profile profile) {
        if (!requestBody.containsKey(field)) {
            return;
        }

        Object value = requestBody.get(field);
        if (!(value instanceof String fieldValue)) {
            throw new FormatException("The '" + field + "' field must be a string");
        }

        switch (field) {
            case FieldName.EMAIL:
                if (profileRepository.existsByEmail(fieldValue)) {
                    throw new ConflictException("Profile already exists with email");
                }
                profile.setEmail(ValidationUtil.validateEmail(fieldValue));
                break;

            case FieldName.DISPLAY_NAME:
                profile.setDisplayName(ValidationUtil.validateDisplayName(fieldValue));
                break;

            case FieldName.NEW_PASSWORD:
                profile.setPassword(PasswordUtil.hashPassword(ValidationUtil.validatePassword(fieldValue)));
                break;

            default:
                throw new IllegalArgumentException("Unhandled field: " + field);
        }
    }

    private String buildProfileResponse(Profile profile, boolean hasCred) {
        JsonBuilder builder = new JsonBuilder()
            .add(FieldName.ID, profile.getId())
            .add(FieldName.DISPLAY_NAME, profile.getDisplayName());

        if (hasCred) {
            builder
                .add(FieldName.EMAIL, profile.getEmail())
                .add(FieldName.ROLES, profile.getRoles().stream()
                    .map(role -> new JsonBuilder()
                        .add(FieldName.ID, role.getId())
                        .add(FieldName.NAME, role.getName()))
                    .toList());
        }

        return builder.build();
    }
}
