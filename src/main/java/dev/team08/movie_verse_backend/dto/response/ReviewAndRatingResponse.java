package dev.team08.movie_verse_backend.dto.response;

import java.util.UUID;

public class ReviewAndRatingResponse {

    private UUID userId;
    private UUID itemId;
    private String reviewContent;
    private double rating;

    // Constructors, Getters, and Setters
    public ReviewAndRatingResponse() {
    }

    public ReviewAndRatingResponse(UUID userId, UUID itemId, String reviewContent, double rating) {
        this.userId = userId;
        this.itemId = itemId;
        this.reviewContent = reviewContent;
        this.rating = rating;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getMovieId() {
        return itemId;
    }

    public void setMovieId(UUID movieId) {
        this.itemId = itemId;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}

