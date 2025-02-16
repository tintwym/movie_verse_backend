package dev.team08.movie_verse_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.team08.movie_verse_backend.entity.ids.UserMovieInteractionId;
import jakarta.persistence.*;


@Entity
@Table(name = "movie_reviews")
@IdClass(UserMovieInteractionId.class)
public class MovieReview extends CompEntity {

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Id
    @Column(name = "tmdb_movie_id", nullable = false)
    private Integer tmdbMovieId;
    
    @OneToOne
    @MapsId
    @JsonIgnore
    @JoinColumns({
        @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
        @JoinColumn(name = "tmdb_movie_id", referencedColumnName = "tmdb_movie_id")
    })
    private UserMovieInteraction userInteraction;

    @Column(name = "is_edited", nullable = false)
    private boolean isEdited = false;  // ✅ Tracks if the review was edited

    @Column(name = "original_review_text", nullable = false, length = 1000)
    private String originalReviewText;  // ✅ Stores the first review

    @Column(name = "edited_review_text", length = 1000)
    private String editedReviewText;  // ✅ Stores the updated review (nullable)

    @Column(name = "review_sentiment", length = 20)
    private String reviewSentiment;  // ✅ Stores sentiment analysis result

    // ✅ Constructors
    public MovieReview() {}

    public MovieReview(UserMovieInteraction userInteraction, String originalReviewText) {
        this.user = userInteraction.getUser();
    	this.tmdbMovieId = userInteraction.getTmdbMovieId();
    	this.userInteraction = userInteraction;
        this.originalReviewText = originalReviewText;
        this.isEdited = false;
    }
    
    // ✅ Getters & Setters
    public UserMovieInteraction getUserInteraction() {
    	return userInteraction;
    }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Integer getTmdbMovieId() { return tmdbMovieId; }
    public void setTmdbMovieId(Integer tmdbMovieId) { this.tmdbMovieId = tmdbMovieId; }
    
    public boolean isEdited() {
    	return isEdited;
    }
    
    public String getOriginalReviewText() {
    	return originalReviewText;
    }
    
    public String getEditedReviewText() {
    	return editedReviewText;
    }
    
    public String getReviewSentiment() {
    	return reviewSentiment;
    } 

    public void setUserInteraction(UserMovieInteraction userInteraction) {
    	this.userInteraction = userInteraction;
    }

    public void setOriginalReviewText(String originalReviewText) {
        if (!this.isEdited) {  // ✅ Prevent overwriting original text
            this.originalReviewText = originalReviewText;
        }
    }

    public void editReview(String newReviewText) {
        this.editedReviewText = newReviewText;
        this.isEdited = true;
    }

    public void setReviewSentiment(String sentiment) {
        this.reviewSentiment = sentiment;  // ✅ Set sentiment after analysis
    }
    
}

