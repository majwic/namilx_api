package util;

import com.majwic.exception.ResourceNotFoundException;
import com.majwic.model.Comment;
import com.majwic.model.Post;
import com.majwic.model.Profile;
import com.majwic.model.Role;
import com.majwic.repository.CommentRepository;
import com.majwic.repository.PostRepository;
import com.majwic.repository.ProfileRepository;
import com.majwic.repository.RoleRepository;
import com.majwic.util.ServiceUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServiceUtilTest {

    private ProfileRepository profileRepository;
    private PostRepository postRepository;
    private CommentRepository commentRepository;
    private RoleRepository roleRepository;
    private ServiceUtil serviceUtil;

    @BeforeEach
    public void setUp() {
        profileRepository = mock(ProfileRepository.class);
        postRepository = mock(PostRepository.class);
        commentRepository = mock(CommentRepository.class);
        roleRepository = mock(RoleRepository.class);
        serviceUtil = new ServiceUtil(profileRepository, postRepository, commentRepository, roleRepository);
    }

    @Test
    public void testGetProfileByIdOrThrowWhenFound() {
        Profile profile = new Profile();
        profile.setId(1L);
        when(profileRepository.findById(1L)).thenReturn(Optional.of(profile));

        Profile result = serviceUtil.getProfileByIdOrThrow(1L);
        assertEquals(profile, result);
    }

    @Test
    public void testGetProfileByIdOrThrowWhenNotFound() {
        when(profileRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            serviceUtil.getProfileByIdOrThrow(1L);
        });

        assertEquals("Profile not found", exception.getMessage());
    }

    @Test
    public void testGetPostByIdOrThrowWhenFound() {
        Post post = new Post();
        post.setId(1L);
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        Post result = serviceUtil.getPostByIdOrThrow(1L);
        assertEquals(post, result);
    }

    @Test
    public void testGetPostByIdOrThrowWhenNotFound() {
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            serviceUtil.getPostByIdOrThrow(1L);
        });

        assertEquals("Post not found", exception.getMessage());
    }

    @Test
    public void testGetCommentByIdOrThrowWhenFound() {
        Comment comment = new Comment();
        comment.setId(1L);
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        Comment result = serviceUtil.getCommentByIdOrThrow(1L);
        assertEquals(comment, result);
    }

    @Test
    public void testGetCommentByIdOrThrowWhenNotFound() {
        when(commentRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            serviceUtil.getCommentByIdOrThrow(1L);
        });

        assertEquals("Comment not found", exception.getMessage());
    }

    @Test
    public void testGetRoleByNameOrThrowWhenFound() {
        Role role = new Role();
        role.setName("USER");
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(role));

        Role result = serviceUtil.getRoleByNameOrThrow("USER");
        assertEquals(role, result);
    }

    @Test
    public void testGetRoleByNameOrThrowWhenNotFound() {
        when(roleRepository.findByName("USER")).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            serviceUtil.getRoleByNameOrThrow("USER");
        });

        assertEquals("Role not found", exception.getMessage());
    }

    @Test
    public void testGetByEmailOrThrowWhenFound() {
        Profile profile = new Profile();
        profile.setEmail("test@example.com");
        when(profileRepository.findByEmail("test@example.com")).thenReturn(Optional.of(profile));

        Profile result = serviceUtil.getByEmailOrThrow("test@example.com");
        assertEquals(profile, result);
    }

    @Test
    public void testGetByEmailOrThrowWhenNotFound() {
        when(profileRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            serviceUtil.getByEmailOrThrow("test@example.com");
        });

        assertEquals("Profile not found", exception.getMessage());
    }
}
