package dev.team08.movie_verse_backend.dto.request;

import java.util.UUID;

public class RatingAndReviewRequest {
    private UUID userId;
    private UUID itemId;
    private int rating; // For rating
    private String reviewContent; // For review

    public RatingAndReviewRequest() {
    }

    // Getters and Setters

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getItemId() {
        return itemId;
    }

    public void setItemId(UUID movieId) {
        this.itemId = movieId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }
}
