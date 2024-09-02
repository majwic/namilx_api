package com.majwic.repository;

import com.majwic.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c WHERE c.post.id = :postId AND " +
            "(c.parentComment.id = :parentCommentId OR (:parentCommentId IS NULL AND c.parentComment IS NULL))")
    Page<Comment> findByPostIdAndParentCommentId(
        @Param("postId") Long postId,
        @Param("parentCommentId") Long parentCommentId,
        Pageable pageable);
}
