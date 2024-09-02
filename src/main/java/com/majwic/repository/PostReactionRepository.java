package com.majwic.repository;

import com.majwic.model.PostReaction;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostReactionRepository extends JpaRepository<PostReaction, Long> {
    Optional<PostReaction> findByPostIdAndProfileId(Long postId, Long profileId);

    List<PostReaction> findByPostId(Long postId);

    @Cacheable(value = "postLikesCountCache")
    long countByPostIdAndIsLikeTrue(Long postId);

    @Cacheable(value = "postDislikesCountCache")
    long countByPostIdAndIsLikeFalse(Long postId);
}
