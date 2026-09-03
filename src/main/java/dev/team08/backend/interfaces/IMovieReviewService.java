package dev.team08.backend.interfaces;

import dev.team08.backend.dto.response.CommunityReviewResponse;
import dev.team08.backend.entity.MovieReview;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IMovieReviewService {
    void addOrUpdateReview(UUID userId, Integer tmdbMovieId, String reviewText, boolean isEdit);
    void deleteReview(UUID userId, Integer tmdbMovieId);
    List<MovieReview> getReviewsByMovieId(Integer tmdbMovieId);
    List<CommunityReviewResponse> getCommunityReviews(Integer tmdbMovieId);
    Optional<MovieReview> getUserReview(UUID userId, Integer tmdbMovieId);
    int getReviewCountByUserId(UUID userId);
}
