package com.majwic.service;

import com.majwic.exception.UnauthorizedException;
import com.majwic.model.Profile;
import com.majwic.util.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final JwtTokenUtil jwtTokenUtil;
    private final ServiceUtil serviceUtil;

    public AuthService(
        JwtTokenUtil jwtTokenUtil,
        ServiceUtil serviceUtil
    ) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.serviceUtil = serviceUtil;
    }

    public String validateSignIn(Map<String, Object> requestBody) {
        Map<String, Class<?>> requiredFields = new HashMap<>();
        requiredFields.put(FieldName.EMAIL, String.class);
        requiredFields.put(FieldName.PASSWORD, String.class);
        ValidationUtil.validateRequiredFields(requestBody, requiredFields);

        String email = (String) requestBody.get(FieldName.EMAIL);
        String password = (String) requestBody.get(FieldName.PASSWORD);

        Profile profile = serviceUtil.getByEmailOrThrow(email);

        if (!PasswordUtil.verifyPassword(password, profile.getPassword())) {
            throw new UnauthorizedException("Incorrect password");
        }

        return authenticateUser(profile.getId());
    }


    public Long getUserIdFromCookie(HttpServletRequest request) {
        String token = getTokenFromCookie(request);
        if (token == null) throw new UnauthorizedException("Session token is missing");
        return getUserIdFromToken(token);
    }

    public void validateProfileExists(Long profileId) {
        serviceUtil.getProfileByIdOrThrow(profileId);
    }

    private String authenticateUser(Long userId) {
        String userIdString = userId.toString();
        return jwtTokenUtil.generateToken(userIdString);
    }

    private Long getUserIdFromToken(String token) {
        String userIdString = jwtTokenUtil.getUserIdFromToken(token);
        return Long.parseLong(userIdString);
    }

    private String getTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwtTokenNamilx".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }
}
