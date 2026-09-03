package dev.team08.backend.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "movies")
public class Movie extends CompEntity{

    @Id
    @Column(name = "tmdb_movie_id", nullable = false)
    private Integer tmdbMovieId;
	
    @Column(nullable = false)
    private String title;
    
    @ManyToMany
    @JoinTable(
            name = "movie_genre",
            joinColumns = @JoinColumn(name = "movieId"),
            inverseJoinColumns = @JoinColumn(name = "genreId")
    )
    private List<Genre> genres;

    public Movie() {}

    public Movie(Integer tmdbMovieId, String title) {
        this.tmdbMovieId = tmdbMovieId;
        this.title = title;
    }
    
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
}
