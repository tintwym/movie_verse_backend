package dev.team08.movie_verse_backend.interfaces;

import dev.team08.movie_verse_backend.entity.MovieRating;

import java.util.Optional;
import java.util.UUID;

public interface IMovieRatingService {
//    public List<MovieRating> getAllRatings();
//    public MovieRating getRatingById(UUID id);
//    public MovieRating createRating(UUID movieId, UUID userId, int rating);
//    public MovieRating updateRating(UUID id, int rating);
//    public void deleteRating(UUID id);
//    public MovieRating getRatingByUserAndMovie(UUID userId, UUID movieId);
//    public List<MovieRating> getRatingsByMovieId(UUID movieId);
//    public Double getAverageRatingByMovie(UUID movieId);
//    public List<MovieRating> getRatingsByUserId(UUID userId);
    
    void addOrUpdateRating(UUID userId, Integer tmdbMovieId, Double rating);
    void deleteRating(UUID userId, Integer tmdbMovieId);
    Optional<MovieRating> getUserRating(UUID userId, Integer tmdbMovieId);
    Double getAverageMovieRating(Integer tmdbMovieId);

}

