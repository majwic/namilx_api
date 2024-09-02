package com.majwic.controller;

import com.majwic.service.AuthService;
import com.majwic.service.PostService;
import com.majwic.swagger.PostDocumentation;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class PostController implements PostDocumentation {

    private final PostService postService;
    private final AuthService authService;

    public PostController(
        PostService postService,
        AuthService authService
    ) {
        this.postService = postService;
        this.authService = authService;
    }

    // ======================================== POST Create Post ========================================

    @Override
    @PostMapping("/post")
    public ResponseEntity<String> create(
        @RequestBody Map<String, Object> requestBody,
        HttpServletRequest request
    ) {
        Long profileId = authService.getUserIdFromCookie(request);
        String response = postService.create(requestBody, profileId);

        return ResponseEntity.ok(response);
    }

    // ======================================== POST Reaction To Post ========================================

    @Override
    @PostMapping("/post/{id}")
    public ResponseEntity<String> reactToPost(
        @PathVariable Long id,
        @RequestParam(required = false) Boolean isLiked,
        HttpServletRequest request
    ) {
        Long profileId = authService.getUserIdFromCookie(request);
        String response = postService.reactToPost(id, profileId, isLiked);

        return ResponseEntity.ok(response);
    }

    // ======================================== GET Post ========================================

    @Override
    @GetMapping("/post/{id}")
    public ResponseEntity<String> read(
        @PathVariable Long id,
        HttpServletRequest request
    ) {
        Long profileId;
        try {
            profileId = authService.getUserIdFromCookie(request);
        } catch (Exception _) {
            profileId = null;
        }
        String response = postService.read(id, profileId);

        return ResponseEntity.ok(response);
    }

    // ======================================== GET Posts by Tag ========================================

    @GetMapping("/post/tag")
    public ResponseEntity<String> readAllByTag(
        @RequestParam(required = false) String tag,
        @RequestParam(defaultValue = "likes") String sortBy,
        @RequestParam(defaultValue = "desc") String sortDir,
        @RequestParam int page,
        @RequestParam int size,
        HttpServletRequest request
    ) {
        Long profileId = null;
        try {
            profileId = authService.getUserIdFromCookie(request);
        } catch (Exception _) {}

        String response = postService.getPostsByTag(tag, sortBy, sortDir, page, size, profileId);

        return ResponseEntity.ok(response);
    }

    // ======================================== DELETE Post ========================================

    @Override
    @DeleteMapping("/post/{id}")
    public ResponseEntity<Void> delete(
        @PathVariable Long id,
        HttpServletRequest request
    ) {
        Long userId = authService.getUserIdFromCookie(request);
        postService.delete(userId, id);

        return ResponseEntity.ok().build();
    }
}
