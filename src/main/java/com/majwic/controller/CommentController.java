package com.majwic.controller;

import com.majwic.service.AuthService;
import com.majwic.service.CommentService;
import com.majwic.swagger.CommentDocumentation;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class CommentController implements CommentDocumentation {

    private final CommentService commentService;
    private final AuthService authService;

    CommentController(
        CommentService commentService,
        AuthService authService
    ) {
        this.commentService = commentService;
        this.authService = authService;
    }

    @PostMapping(value = "/comment", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> create(
        @RequestBody Map<String, Object> requestBody,
        HttpServletRequest request
    ) {
        Long profileId = authService.getUserIdFromCookie(request);
        String response = commentService.create(requestBody, profileId);

        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/comment/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> reactToComment(
        @PathVariable Long id,
        @RequestParam(required = false) Boolean likeVal,
        HttpServletRequest request
    ) {
        Long profileId = authService.getUserIdFromCookie(request);
        String response = commentService.reactToComment(id, profileId, likeVal);

        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/comment/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
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
        String response = commentService.read(id, profileId);

        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/comment/from-post/{postId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> readAll(
        @PathVariable Long postId,
        @RequestParam(required = false) Long parentCommentId,
        @RequestParam int page,
        @RequestParam int size,
        HttpServletRequest request
    ) {
        Long profileId;
        try {
            profileId = authService.getUserIdFromCookie(request);
        } catch (Exception _) {
            profileId = null;
        }

        String response = commentService.readAll(postId, parentCommentId, profileId, page, size);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/comment/{id}")
    public ResponseEntity<Void> delete(
        @PathVariable Long id,
        HttpServletRequest request
    ) {
        Long profileId = authService.getUserIdFromCookie(request);
        commentService.delete(id, profileId);

        return ResponseEntity.ok().build();
    }
}
