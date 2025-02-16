package dev.team08.movie_verse_backend.entity;

import dev.team08.movie_verse_backend.entity.ids.UserMovieInteractionId;
import jakarta.persistence.*;

@Entity
@Table(name = "movie_ratings")
@IdClass(UserMovieInteractionId.class)
public class MovieRating extends CompEntity {
	
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Id
    @Column(name = "tmdb_movie_id", nullable = false)
    private Integer tmdbMovieId;
	
    @OneToOne
    @MapsId
    @JoinColumns({
        @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
        @JoinColumn(name = "tmdb_movie_id", referencedColumnName = "tmdb_movie_id")
    })
    private UserMovieInteraction userInteraction;

    @Column(nullable = false)
    private double rating;
    
    // ✅ Constructors
    public MovieRating() {}

    public MovieRating(UserMovieInteraction userInteraction, double rating) {
        this.userInteraction = userInteraction;
        this.rating = rating;
        this.user = userInteraction.getUser();
        this.tmdbMovieId = userInteraction.getTmdbMovieId();
    }
    
    //Getters and Setters
    public void setUserInteraction(UserMovieInteraction userInteraction) {
    	this.userInteraction = userInteraction;
    }
    
    public UserMovieInteraction getUserInteraction () {
    	return this.userInteraction;
    }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Integer getTmdbMovieId() { return tmdbMovieId; }
    public void setTmdbMovieId(Integer tmdbMovieId) { this.tmdbMovieId = tmdbMovieId; }
    
    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
