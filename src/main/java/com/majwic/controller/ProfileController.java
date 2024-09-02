package com.majwic.controller;

import com.majwic.service.AuthService;
import com.majwic.service.ProfileService;
import com.majwic.swagger.ProfileDocumentation;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class ProfileController implements ProfileDocumentation {

    private final ProfileService profileService;
    private final AuthService authService;

    public ProfileController(
        ProfileService profileService,
        AuthService authService
    ) {
        this.profileService = profileService;
        this.authService = authService;
    }

    // ======================================== GET Profile by Cookie ======================================== //

    @Override
    @GetMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> readByCookie(
        HttpServletRequest request)
    {
        // === Retrieve profile using profile ID from cookie === //
        Long profileId = authService.getUserIdFromCookie(request);
        String response =  profileService.read(profileId, true);

        return ResponseEntity.ok(response);
    }

    // ======================================== GET Profile by ID ======================================== //

    @Override
    @GetMapping(value = "/profile/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> readById(
        @PathVariable Long id
    ) {
        // === Retrieve profile using profile ID from path === //
        String response = profileService.read(id, false);

        return ResponseEntity.ok(response);
    }

    // ======================================== POST Create Profile ======================================== //

    @Override
    @PostMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> create(
        @RequestBody Map<String, Object> requestBody
    ) {
        // === Create profile using request body === //
        String response = profileService.create(requestBody);

        return ResponseEntity.ok(response);
    }

    // ======================================== PUT Update Profile ======================================== //

    @Override
    @PutMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> update(
            @RequestBody Map<String, Object> requestBody,
            HttpServletRequest request)
    {
        // === Update profile using profile ID from cookie and request body === //
        Long profileId = authService.getUserIdFromCookie(request);
        String response = profileService.update(profileId, requestBody);

        return ResponseEntity.ok(response);
    }

    // ======================================== DELETE Profile ======================================== //

    @Override
    @DeleteMapping("/profile")
    public ResponseEntity<Void> delete(
        @RequestParam String password,
        HttpServletRequest request
    ) {
        // === Delete profile using profile ID from cookie and profile password === //
        Long profileId = authService.getUserIdFromCookie(request);
        profileService.delete(profileId, password);

        return ResponseEntity.ok().build();
    }
}