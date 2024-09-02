package com.majwic.model;

import jakarta.persistence.*;

@Entity
public class PostReaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private Profile profile;

    private boolean isLike;

    public PostReaction() {}

    public PostReaction(Post post, Profile profile, boolean isLike) {
        this.post = post;
        this.profile = profile;
        this.isLike = isLike;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public boolean getIsLike() {
        return isLike;
    }

    public void setIsLike(boolean islike) {
        isLike = islike;
    }
}
