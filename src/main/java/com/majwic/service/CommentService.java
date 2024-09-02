package com.majwic.service;

import com.majwic.exception.FormatException;
import com.majwic.exception.UnauthorizedException;
import com.majwic.model.Comment;
import com.majwic.model.CommentReaction;
import com.majwic.model.Post;
import com.majwic.model.Profile;
import com.majwic.repository.CommentReactionRepository;
import com.majwic.repository.CommentRepository;
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
import java.util.Map;
import java.util.Optional;

@Service
public class CommentService {

    private static final int MAX_CONTENT_LENGTH = 1000;

    private final CommentRepository commentRepository;
    private final CommentReactionRepository commentReactionRepository;

    private final ServiceUtil serviceUtil;

    public CommentService(
        CommentRepository commentRepository,
        CommentReactionRepository commentReactionRepository,
        ServiceUtil serviceUtil
    ) {
        this.commentRepository = commentRepository;
        this.commentReactionRepository = commentReactionRepository;
        this.serviceUtil = serviceUtil;
    }

    @Transactional
    public String create(Map<String, Object> requestBody, Long profileId) {
        createValidation(requestBody);

        String content = (String) requestBody.get(FieldName.CONTENT);
        Long postId = ((Number) requestBody.get(FieldName.POST_ID)).longValue();
        Long parentCommentId = requestBody.containsKey(FieldName.PARENT_COMMENT_ID) ?
            ((Number) requestBody.get(FieldName.PARENT_COMMENT_ID)).longValue() : null;

        Post post = serviceUtil.getPostByIdOrThrow(postId);
        Profile author = serviceUtil.getProfileByIdOrThrow(profileId);
        Comment parentComment = (parentCommentId != null) ?
            serviceUtil.getCommentByIdOrThrow(parentCommentId) : null;

        Comment comment = new Comment(content, post, parentComment, author);
        Comment savedComment = commentRepository.save(comment);

        return buildCommentResponse(savedComment, profileId);
    }

    public String read(Long id, Long profileId) {
        Comment comment = serviceUtil.getCommentByIdOrThrow(id);

        updateReactions(comment);

        return buildCommentResponse(comment, profileId);
    }

    public String readAll(Long postId, Long parentCommentId, Long profileId, int page, int size) {
        Sort sort = Sort.by(FieldName.LIKES).descending();
        Pageable pageable = PageRequest.of(page, Math.min(size, 20), sort);
        Page<Comment> commentsPage = commentRepository.findByPostIdAndParentCommentId(postId, parentCommentId, pageable);

        return new JsonBuilder()
            .add(FieldName.COMMENTS, commentsPage.stream()
                .map(comment -> {
                    updateReactions(comment);
                    return commentResponse(comment, profileId);
                })
                .toList())
            .build();
    }

    @Transactional
    public String reactToComment(Long id, Long profileId, Boolean likeVal) {
        Comment comment = serviceUtil.getCommentByIdOrThrow(id);
        Profile profile = serviceUtil.getProfileByIdOrThrow(profileId);

        Optional<CommentReaction> reaction = commentReactionRepository.findByCommentIdAndProfileId(id, profileId);

        reaction.ifPresentOrElse(commentReaction -> {
            if (likeVal == null) {
                commentReactionRepository.delete(commentReaction);
            } else {
                commentReaction.setIsLike(likeVal);
                commentReactionRepository.save(commentReaction);
            }
        }, () -> {
            if (likeVal != null) {
                CommentReaction commentReaction = new CommentReaction(comment, profile, likeVal);
                commentReactionRepository.save(commentReaction);
            }
        });

        updateReactions(comment);
        Comment savedComment = commentRepository.save(comment);

        return buildCommentResponse(savedComment, profileId);
    }

    @Transactional
    public void delete(Long commentId, Long profileId) {
        Comment comment = serviceUtil.getCommentByIdOrThrow(commentId);

        if (!profileId.equals(comment.getAuthor().getId())) {
            throw new UnauthorizedException("Comment does not belong to profile.");
        }

        commentReactionRepository.findByCommentId(commentId)
            .forEach(commentReactionRepository::delete);

        commentRepository.delete(comment);
    }

    // === Private Helper Methods === //

    private void createValidation(Map<String, Object> requestBody) {
        Map<String, Class<?>> requiredFields = new HashMap<>();
        requiredFields.put(FieldName.CONTENT, String.class);
        requiredFields.put(FieldName.POST_ID, Number.class);
        ValidationUtil.validateRequiredFields(requestBody, requiredFields);

        String content = (String) requestBody.get(FieldName.CONTENT);
        if (content.length() > MAX_CONTENT_LENGTH) {
            throw new FormatException("The 'content' field must less than " + MAX_CONTENT_LENGTH + " characters");
        }

        if (requestBody.containsKey(FieldName.PARENT_COMMENT_ID) &&
            !(requestBody.get(FieldName.PARENT_COMMENT_ID) instanceof Number)) {

            throw new FormatException("The 'parentCommentId' field must be a number if provided");
        }
    }

    private void updateReactions(Comment comment) {
        long likes = commentReactionRepository.countByCommentIdAndIsLikeTrue(comment.getId());
        long dislikes = commentReactionRepository.countByCommentIdAndIsLikeFalse(comment.getId());

        comment.setLikes(likes);
        comment.setDislikes(dislikes);
    }

    private JsonBuilder commentResponse(Comment comment, Long profileId) {
        Optional<CommentReaction> commentReaction = commentReactionRepository
            .findByCommentIdAndProfileId(comment.getId(), profileId);

        JsonBuilder builder = new JsonBuilder()
            .add(FieldName.ID, comment.getId())
            .add(FieldName.CONTENT, comment.getContent())
            .add(FieldName.LIKES, comment.getLikes())
            .add(FieldName.DISLIKES, comment.getDislikes());

        if (comment.getPost() != null) {
            builder.add(FieldName.POST_ID, comment.getPost().getId());
        }
        if (comment.getAuthor() != null) {
            builder.add(FieldName.AUTHOR_ID, comment.getAuthor().getId());
        }
        if (comment.getParentComment() != null) {
            builder.add(FieldName.PARENT_COMMENT_ID, comment.getParentComment().getId());
        }

        commentReaction.ifPresent(reaction -> builder.add(FieldName.IS_LIKED, reaction.getIsLike()));

        return builder;
    }

    private String buildCommentResponse(Comment comment, Long profileId) {
        return commentResponse(comment, profileId).build();
    }
}
