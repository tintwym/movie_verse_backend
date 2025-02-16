package dev.team08.movie_verse_backend.interfaces;

import dev.team08.movie_verse_backend.entity.MovieReview;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IMovieReviewService {
//    public List<MovieReview> getAllReviews();
//    public MovieReview getReviewById(UUID id);
//    public MovieReview createReview(UUID movieId, UUID userId, String content);
//    public MovieReview updateReview(UUID id, String content);
//    public void deleteReview(UUID id);
//    public MovieReview getReviewByUserAndMovie(UUID userId, UUID movieId);
//    public List<MovieReview> getReviewsByMovieId(UUID movieId);
//    public List<MovieReview> getReviewsByUserId(UUID userId);
	
    void addOrUpdateReview(UUID userId, Integer tmdbMovieId, String reviewText, boolean isEdit);
    void deleteReview(UUID userId, Integer tmdbMovieId);
    List<MovieReview> getReviewsByMovieId(Integer tmdbMovieId);
    Optional<MovieReview> getUserReview(UUID userId, Integer tmdbMovieId);
    public String callPythonReviewSentimentApi(String review);
    int getReviewCountByUserId(UUID userId);
}
