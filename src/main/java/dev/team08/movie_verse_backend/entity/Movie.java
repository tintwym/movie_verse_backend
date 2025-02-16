package dev.team08.movie_verse_backend.entity;

import jakarta.persistence.*;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "movies")
public class Movie extends CompEntity{

    @Id
    @Column(name = "tmdb_movie_id", nullable = false)
    private Integer tmdbMovieId;  // ✅ Uses TMDB ID instead of UUID
	
    @Column(nullable = false)
    private String title;
    
    @OneToMany(mappedBy = "tmdbMovieId", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserMovieInteraction> userInteractions;  // ✅ Tracks user interactions
    
    @ManyToMany
    @JoinTable(
            name = "movie_genre",
            joinColumns = @JoinColumn(name = "movieId"),
            inverseJoinColumns = @JoinColumn(name = "genreId")
    )
    private List<Genre> genres;

    //Constructor
    // ✅ No-Args Constructor
    public Movie() {}

    public Movie(Integer tmdbMovieId, String title) {
        this.tmdbMovieId = tmdbMovieId;
        this.title = title;
    }
    
    //Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public Integer getTmdbMovieId() {
    	return tmdbMovieId;
    }
    
    public void setTmdbMovieId(Integer tmdbMovieId) {
    	this.tmdbMovieId = tmdbMovieId;
    }
    
    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }
    
    public Set<UserMovieInteraction> getUserInteractions() {
    	return userInteractions;
    }
    
    public void setUserInteractions(Set<UserMovieInteraction> userInteractions) {
    	this.userInteractions = userInteractions;
    }
    
}
