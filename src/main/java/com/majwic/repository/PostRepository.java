package com.majwic.repository;

import com.majwic.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p WHERE p.tags LIKE %:tag%")
    Page<Post> findByTagContaining(@Param("tag") String tag, Pageable pageable);
}
