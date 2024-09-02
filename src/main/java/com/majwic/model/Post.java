package com.majwic.model;

import com.majwic.exception.FormatException;
import jakarta.persistence.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000)
    private String content;

    private long likes;

    private long dislikes;

    private String tags;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private Profile author;

    public Post() {}

    public Post(String content, List<String> tags, Profile author) {
        this.content = content;
        this.likes = 0;
        this.dislikes = 0;
        this.setTags(tags);
        this.comments = new HashSet<>();
        this.author = author;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getLikes() {
        return likes;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }

    public long getDislikes() {
        return dislikes;
    }

    public void setDislikes(long dislikes) {
        this.dislikes = dislikes;
    }

    public List<String> getTags() {
        return tags != null ? Arrays.asList(tags.split(",")) : List.of();
    }

    public void setTags(List<String> tags) {
        if (tags == null) {
            this.tags = "";
            return;
        }
        if (tags.size() > 3) {
            throw new FormatException("A post can have a maximum of 3 tags");
        }
        this.tags = String.join(",", tags);
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Profile getAuthor() {
        return author;
    }

    public void setAuthor(Profile author) {
        this.author = author;
    }
}
