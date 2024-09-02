package com.majwic.controller;

import com.majwic.service.AuthService;
import com.majwic.swagger.AuthDocumentation;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class AuthController implements AuthDocumentation {

    private final AuthService authService;

    public AuthController(
        AuthService authService
    ) {
        this.authService = authService;
    }

    // ======================================== Get Validate Session ======================================== //

    @Override
    @GetMapping("/auth/validate")
    public ResponseEntity<Void> validateSession(
        HttpServletRequest request
    ) {
        // === Validate session by checking profile ID from cookie === //
        Long profileId = authService.getUserIdFromCookie(request);
        authService.validateProfileExists(profileId);

        return ResponseEntity.ok().build();
    }

    // ======================================== POST Sign-in ======================================== //

    @Override
    @PostMapping("/auth/signin")
    public ResponseEntity<Void> authProfile(
            @RequestBody Map<String, Object> requestBody,
            HttpServletResponse response
    ) {
        // === Validate sign in credentials and generate token === //
        String token = authService.validateSignIn(requestBody);

        // === Create and configure the cookie === //
        Cookie cookie = new Cookie("jwtTokenNamilx", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(28800);

        // === Add the cookie to the response === //
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    // ======================================== DELETE Sign-out ======================================== //

    @Override
    @DeleteMapping("/auth/signout")
    public ResponseEntity<Void> signout(
        HttpServletResponse response
    ) {
        // === Create and configure a null and expired cookie === //
        Cookie cookie = new Cookie("jwtTokenNamilx", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);

        // === Add the cookie to the response === //
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }
}
