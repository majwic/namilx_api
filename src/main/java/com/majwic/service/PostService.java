package com.majwic.service;

import com.majwic.exception.FormatException;
import com.majwic.exception.UnauthorizedException;
import com.majwic.model.PostReaction;
import com.majwic.model.Post;
import com.majwic.model.Profile;
import com.majwic.repository.PostReactionRepository;
import com.majwic.repository.PostRepository;
import com.majwic.util.FieldName;
import com.majwic.util.JsonBuilder;
import com.majwic.util.ServiceUtil;
import com.majwic.util.ValidationUtil;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PostService {

    private static final int MAX_CONTENT_LENGTH = 1000;

    private final PostRepository postRepository;
    private final PostReactionRepository postReactionRepository;
    private final ServiceUtil serviceUtil;

    public PostService(
        PostRepository postRepository,
        PostReactionRepository postReactionRepository,
        ServiceUtil serviceUtil
    ) {
        this.postRepository    = postRepository;
        this.postReactionRepository = postReactionRepository;
        this.serviceUtil = serviceUtil;
    }

    @Transactional
    public String create(Map<String, Object> requestBody, Long profileId) {
        createValidation(requestBody);

        String content = (String) requestBody.get(FieldName.CONTENT);
        List<String> tags = (List<String>) requestBody.get(FieldName.TAGS);

        Profile author = serviceUtil.getProfileByIdOrThrow(profileId);

        Post post = new Post(content, tags, author);
        Post savedPost = postRepository.save(post);

        return buildPostResponse(savedPost, profileId);
    }

    public String read(Long postId, Long profileId) {
        Post post = serviceUtil.getPostByIdOrThrow(postId);

        updateReactions(post);

        return buildPostResponse(post, profileId);
    }


    public String getPostsByTag(String tag, String sortBy, String sortDir, int page, int size, Long profileId) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
            Sort.by(sortBy).ascending() :
            Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, Math.min(size, 20), sort);
        Page<Post> postsPage = postRepository.findByTagContaining((tag == null) ? "" : tag, pageable);

        return new JsonBuilder()
            .add(FieldName.POSTS, postsPage.stream()
                .map(post -> {
                    updateReactions(post);
                    return postResponse(post, profileId);
                })
                .toList())
            .build();
    }

    @Transactional
    public String reactToPost(Long postId, Long profileId, Boolean likeVal) {
        Post post = serviceUtil.getPostByIdOrThrow(postId);
        Profile profile = serviceUtil.getProfileByIdOrThrow(profileId);

        Optional<PostReaction> reaction = postReactionRepository.findByPostIdAndProfileId(postId, profileId);

        reaction.ifPresentOrElse(postReaction -> {
            if (likeVal == null) {
                postReactionRepository.delete(postReaction);
            } else {
                postReaction.setIsLike(likeVal);
                postReactionRepository.save(postReaction);
            }
        }, () -> {
            if (likeVal != null) {
                PostReaction postReaction = new PostReaction(post, profile, likeVal);
                postReactionRepository.save(postReaction);
            }
        });

        updateReactions(post);
        Post savedPost = postRepository.save(post);

        return buildPostResponse(savedPost, profileId);
    }

    @Transactional
    public void delete(Long profileId, Long postId) {
        Post post = serviceUtil.getPostByIdOrThrow(postId);

        if (!profileId.equals(post.getAuthor().getId())) {
            throw new UnauthorizedException("Post does not belong to profile.");
        }

        postReactionRepository.findByPostId(postId)
            .forEach(postReactionRepository::delete);

        postRepository.delete(post);
    }

    // === Private Helper Methods === //

    private void createValidation(Map<String, Object> requestBody) {
        Map<String, Class<?>> requiredFields = new HashMap<>();
        requiredFields.put(FieldName.CONTENT, String.class);
        requiredFields.put(FieldName.TAGS, List.class);
        ValidationUtil.validateRequiredFields(requestBody, requiredFields);

        String content = (String) requestBody.get(FieldName.CONTENT);
        if (content.length() > MAX_CONTENT_LENGTH) {
            throw new FormatException("The 'content' field must less than 1000 characters");
        }

        List<?> tagList = (List<?>) requestBody.get(FieldName.TAGS);
        if (tagList.size() > 3) {
            throw new FormatException("The 'tags' field cannot be a list of more than 3 items");
        }

        for (Object tag : tagList) {
            if (!(tag instanceof String)) {
                throw new FormatException("The 'tags' field must be a list of strings");
            }
        }
    }

    private void updateReactions(Post post) {
        long likes = postReactionRepository.countByPostIdAndIsLikeTrue(post.getId());
        long dislikes = postReactionRepository.countByPostIdAndIsLikeFalse(post.getId());
        post.setLikes(likes);
        post.setDislikes(dislikes);
    }

    private JsonBuilder postResponse(Post post, Long profileId) {
        Optional<PostReaction> postReaction = postReactionRepository
            .findByPostIdAndProfileId(post.getId(), profileId);

        JsonBuilder builder = new JsonBuilder()
            .add(FieldName.ID, post.getId())
            .add(FieldName.CONTENT, post.getContent())
            .add(FieldName.LIKES, post.getLikes())
            .add(FieldName.DISLIKES, post.getDislikes())
            .add(FieldName.TAGS, post.getTags());

        if (post.getAuthor() != null) {
            builder.add(FieldName.AUTHOR_ID, post.getAuthor().getId());
        }

        postReaction.ifPresent(reaction -> builder.add(FieldName.IS_LIKED, reaction.getIsLike()));

        return builder;
    }

    private String buildPostResponse(Post post, Long profileId) {
        return postResponse(post, profileId).build();
    }
}
