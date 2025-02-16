package dev.team08.movie_verse_backend.dto.request;

import java.util.UUID;

public class UserInteractionDTO {
    private UUID userId;
    private String username;
    private String movieName;
    private Double rating;
    private int favorite;
    private int search;
    private int watched;
    private int planned;

    public UserInteractionDTO(UUID userId, String username, String movieName, Double rating, int favorite, int search, int watched, int planned) {
        this.userId = userId;
        this.username = username;
        this.movieName = movieName;
        this.rating = rating;
        this.favorite = favorite;
        this.search = search;
        this.watched = watched;
        this.planned = planned;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public int getSearch() {
        return search;
    }

    public void setSearch(int search) {
        this.search = search;
    }

    public int getWatched() {
        return watched;
    }

    public void setWatched(int watched) {
        this.watched = watched;
    }

    public int getPlanned() {
        return planned;
    }

    public void setPlanned(int planned) {
        this.planned = planned;
    }
}
