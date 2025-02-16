package dev.team08.movie_verse_backend.dto.request;

public class MLReviewRequest {
    private String review;

    // ✅ Constructor
    public MLReviewRequest(String review) {
        this.review = review;
    }

    // ✅ Default constructor (required for JSON serialization)
    public MLReviewRequest() {}

    // ✅ Getter & Setter
    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
