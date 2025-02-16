package dev.team08.movie_verse_backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "favorites")
public class Favorite extends BaseEntity{
    @ManyToOne
    @JoinColumn(name = "tmdb_movie_id", nullable = false)
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private FavoriteCategory category;


    // Constructors
    public Favorite() {}

    public Favorite(User user, Movie movie, FavoriteCategory category) {
        this.user = user;
        this.movie = movie;
        this.category = category;
    }

    //Getters and Setters
    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public FavoriteCategory getCategory() {
        return category;
    }

    public void setCategory(FavoriteCategory category) {
        this.category = category;
    }

}
