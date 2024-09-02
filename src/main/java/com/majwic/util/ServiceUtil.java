package com.majwic.util;

import com.majwic.exception.ResourceNotFoundException;
import com.majwic.model.*;
import com.majwic.repository.*;
import org.springframework.stereotype.Component;

@Component
public class ServiceUtil {

    private final ProfileRepository profileRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final RoleRepository roleRepository;

    public ServiceUtil(
        ProfileRepository profileRepository,
        PostRepository postRepository,
        CommentRepository commentRepository,
        RoleRepository roleRepository
    ) {
        this.profileRepository = profileRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.roleRepository = roleRepository;
    }

    public Profile getProfileByIdOrThrow(Long profileId) {
        return profileRepository.findById(profileId)
            .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));
    }

    public Post getPostByIdOrThrow(Long postId) {
        return postRepository.findById(postId)
            .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
    }

    public Comment getCommentByIdOrThrow(Long commentId) {
        return commentRepository.findById(commentId)
            .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
    }

    public Role getRoleByNameOrThrow(String roleName) {
        return roleRepository.findByName(roleName)
            .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
    }

    public Profile getByEmailOrThrow(String email) {
        return profileRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));
    }
}
