package com.majwic.repository;

import com.majwic.model.CommentReaction;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentReactionRepository extends JpaRepository<CommentReaction, Long> {
    Optional<CommentReaction> findByCommentIdAndProfileId(Long commentId, Long profileId);

    List<CommentReaction> findByCommentId(Long commentId);

    @Cacheable(value = "commentLikesCountCache")
    long countByCommentIdAndIsLikeTrue(Long commentId);

    @Cacheable(value = "commentDislikesCountCache")
    long countByCommentIdAndIsLikeFalse(Long commentId);
}
