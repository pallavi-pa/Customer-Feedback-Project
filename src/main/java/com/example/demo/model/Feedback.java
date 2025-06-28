package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entity class representing feedback submitted by a user.
 */
@Entity
@Table(name = "feedbacks")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Username of the user who submitted the feedback */
    @Column(nullable = false)
    private String username;

    /** Feedback message text (max 1000 characters) */
    @Column(length = 1000, nullable = false)
    private String message;

    /** Type of feedback: Complaint, Suggestion, Praise */
    @Column(nullable = false)
    private String feedbackType;

    /** Rating from 1 (worst) to 5 (best) */
    @Column(nullable = false)
    private Integer rating;

    /** Timestamp of feedback creation */
    @Column(nullable = false)
    private LocalDateTime createdAt;

    /** Admin reply to the feedback (optional) */
    @Column(length = 1000)
    private String reply;

    /** Default constructor (required by JPA) */
    public Feedback() {}

    /**
     * Constructor to create a Feedback without reply or date.
     */
    public Feedback(String username, String message, String feedbackType, Integer rating) {
        this.username = username;
        this.message = message;
        this.feedbackType = feedbackType;
        this.rating = rating;
    }

    /**
     * Automatically sets the creation time before saving to the database.
     */
    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    // --- Getters and Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFeedbackType() {
        return feedbackType;
    }

    public void setFeedbackType(String feedbackType) {
        this.feedbackType = feedbackType;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    // --- equals and hashCode ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Feedback)) return false;
        Feedback feedback = (Feedback) o;
        return Objects.equals(id, feedback.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // --- toString for debugging ---

    @Override
    public String toString() {
        return "Feedback{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", message='" + message + '\'' +
                ", feedbackType='" + feedbackType + '\'' +
                ", rating=" + rating +
                ", createdAt=" + createdAt +
                ", reply='" + reply + '\'' +
                '}';
    }
}
