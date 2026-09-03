package dev.team08.backend.dto.response;

public class CommunityReviewResponse {
    private final String username;
    private final String reviewText;
    private final boolean edited;
    private final Double rating;

    public CommunityReviewResponse(String username, String reviewText, boolean edited, Double rating) {
        this.username = username;
        this.reviewText = reviewText;
        this.edited = edited;
        this.rating = rating;
    }

    public String getUsername() {
        return username;
    }

    public String getReviewText() {
        return reviewText;
    }

    public boolean isEdited() {
        return edited;
    }

    public Double getRating() {
        return rating;
    }
}
