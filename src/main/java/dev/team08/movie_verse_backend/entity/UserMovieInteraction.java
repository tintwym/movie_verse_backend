package dev.team08.movie_verse_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.team08.movie_verse_backend.entity.ids.UserMovieInteractionId;
import dev.team08.movie_verse_backend.enums.LikeStatus;
import dev.team08.movie_verse_backend.enums.WatchStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "user_movie_interactions")
@IdClass(UserMovieInteractionId.class) 
public class UserMovieInteraction extends CompEntity {
	
    @Id
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Id
    @Column(name = "tmdb_movie_id", nullable = false)
    private Integer tmdbMovieId;  // ✅ TMDB ID instead of Movie entity

    @Column(name = "views", nullable = false)
    private int views = 0; // Default 0

    @Enumerated(EnumType.STRING)
    @Column(name = "like_status", nullable = false)
    private LikeStatus likeStatus = LikeStatus.NEUTRAL; 

    @Column(name = "favorite", nullable = false)
    private boolean favorite = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "watch_status", nullable = false)
    private WatchStatus watchStatus = WatchStatus.NO_PLANS;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "userInteraction")
    private MovieRating rating;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "userInteraction")
    private MovieReview review;

    // ✅ Constructors
    public UserMovieInteraction() {}

    public UserMovieInteraction(User user, Integer tmdbMovieId) {
        this.user = user;
        this.tmdbMovieId = tmdbMovieId;
    }

	// ✅ Getters & Setters
    public User getUser() { return user; }
    public Integer getTmdbMovieId() { return tmdbMovieId; }
    public int getViews() { return views; }
    public LikeStatus getLikeStatus() { return likeStatus; }
    public boolean isFavorite() { return favorite; }
    public WatchStatus getWatchStatus() { return watchStatus; }
    public MovieRating getRating() { return rating; }
    public MovieReview getReview() { return review; }

    public void setViews(int views) { this.views = views; }
    public void setLikeStatus(LikeStatus likeStatus) { this.likeStatus = likeStatus; }
    public void setFavorite(boolean favorite) { this.favorite = favorite; }
    public void setWatchStatus(WatchStatus watchStatus) { this.watchStatus = watchStatus; }
    public void setRating(MovieRating rating) { this.rating = rating; }
    public void setReview(MovieReview review) { this.review = review; }
}

