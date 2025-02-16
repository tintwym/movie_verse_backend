package dev.team08.movie_verse_backend.interfaces;

import dev.team08.movie_verse_backend.entity.UserMovieInteraction;
import dev.team08.movie_verse_backend.enums.LikeStatus;
import dev.team08.movie_verse_backend.enums.WatchStatus;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface IUserMovieInteractionService {
    void logMovieView(UUID userId, Integer tmdbMovieId);
    void markMovieAsWatched(UUID userId, Integer tmdbMovieId);
    Optional<UserMovieInteraction> getUserMovieInteraction(UUID userId, Integer tmdbMovieId);
	void toggleFavorite(UUID userId, Integer tmdbMovieId);
	void toggleWatchlist(UUID userId, Integer tmdbMovieId);
	void likeOrDislikeMovie(UUID userId, Integer tmdbMovieId, LikeStatus likeStatus);
	void addOrUpdateReview(UUID userId, Integer tmdbMovieId, String reviewText, boolean isEdit);

	List<UserMovieInteraction> getAllUserMovieInteractionsByUser(UUID userID);
	List<Map<String, Object>> getUserInteractions(String token);
	List<String> callPythonRecommendApi(List<Map<String, Object>> userInteractions);
	
	// MNP update
	List<Integer> getWatchedMovieIds(UUID userId);
	void updateWatchStatus(UUID userId, Integer tmdbMovieId, WatchStatus watchStatus);
	List<Integer> getFavoriteMovieIds(UUID userId);
}
