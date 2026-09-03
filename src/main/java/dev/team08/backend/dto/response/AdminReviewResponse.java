package dev.team08.backend.dto.response;

public class AdminReviewResponse {
    private String userId;
    private String username;
    private Integer tmdbMovieId;
    private String reviewText;
    private boolean edited;
    private String sentiment;

    public AdminReviewResponse() {}

    public AdminReviewResponse(
            String userId, String username, Integer tmdbMovieId, String reviewText,
            boolean edited, String sentiment) {
        this.userId = userId;
        this.username = username;
        this.tmdbMovieId = tmdbMovieId;
        this.reviewText = reviewText;
        this.edited = edited;
        this.sentiment = sentiment;
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public Integer getTmdbMovieId() { return tmdbMovieId; }
    public void setTmdbMovieId(Integer tmdbMovieId) { this.tmdbMovieId = tmdbMovieId; }
    public String getReviewText() { return reviewText; }
    public void setReviewText(String reviewText) { this.reviewText = reviewText; }
    public boolean isEdited() { return edited; }
    public void setEdited(boolean edited) { this.edited = edited; }
    public String getSentiment() { return sentiment; }
    public void setSentiment(String sentiment) { this.sentiment = sentiment; }
}
