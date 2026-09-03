package dev.team08.backend.controller.api;

import dev.team08.backend.entity.UserMovieInteraction;
import dev.team08.backend.enums.LikeStatus;
import dev.team08.backend.enums.WatchStatus;
import dev.team08.backend.interfaces.IUserMovieInteractionService;
import dev.team08.backend.interfaces.IUserService;
import dev.team08.backend.utility.AuthHelper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/user-interactions")
public class UserMovieInteractionApiController {

    private final IUserMovieInteractionService userMovieInteractionService;
    private final IUserService userService;

    public UserMovieInteractionApiController(IUserMovieInteractionService userMovieInteractionService, IUserService userService) {
        this.userMovieInteractionService = userMovieInteractionService;
        this.userService = userService;
    }

    @PostMapping("/view/{tmdb_movie_id}")
    public ResponseEntity<String> logMovieView(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer tmdb_movie_id) {

        UUID userId = extractUserIdFromToken(token);
        userMovieInteractionService.logMovieView(userId, tmdb_movie_id);
        return ResponseEntity.ok("Movie view logged successfully.");
    }

    @PutMapping("/watched/{tmdb_movie_id}")
    public ResponseEntity<String> markMovieAsWatched(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer tmdb_movie_id) {

        UUID userId = extractUserIdFromToken(token);
        userMovieInteractionService.markMovieAsWatched(userId, tmdb_movie_id);
        return ResponseEntity.ok("Movie marked as watched.");
    }
    
    @PostMapping("/like/{tmdb_movie_id}")
    public ResponseEntity<String> likeOrDislikeMovie(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer tmdb_movie_id,
            @RequestParam LikeStatus likeStatus) {

        UUID userId = extractUserIdFromToken(token);
        userMovieInteractionService.likeOrDislikeMovie(userId, tmdb_movie_id, likeStatus);
        return ResponseEntity.ok("Movie interaction updated.");
    }

    @PostMapping("/favorite/{tmdb_movie_id}")
    public ResponseEntity<String> toggleFavorite(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer tmdb_movie_id) {

        UUID userId = extractUserIdFromToken(token);
        userMovieInteractionService.toggleFavorite(userId, tmdb_movie_id);
        return ResponseEntity.ok("Favorite status updated.");
    }

    @PostMapping("/watchlist/{tmdb_movie_id}")
    public ResponseEntity<String> toggleWatchlist(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer tmdb_movie_id) {

        UUID userId = extractUserIdFromToken(token);
        userMovieInteractionService.toggleWatchlist(userId, tmdb_movie_id);
        return ResponseEntity.ok("Watchlist status updated.");
    }

    @GetMapping("/{tmdb_movie_id:\\d+}")
    public ResponseEntity<UserMovieInteraction> getUserMovieInteraction(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer tmdb_movie_id) {

        UUID userId = extractUserIdFromToken(token);
        return userMovieInteractionService.getUserMovieInteraction(userId, tmdb_movie_id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @GetMapping("/recommend")
    public ResponseEntity<?> getRecommendedGenres(@RequestHeader("Authorization") String token) {
        extractUserIdFromToken(token);
        List<Map<String, Object>> userInteractions =
                userMovieInteractionService.getUserInteractions(token);
        List<String> genreNames =
                userMovieInteractionService.getRecommendedMovieIds(userInteractions);
        return ResponseEntity.ok(genreNames);
    }

    private UUID extractUserIdFromToken(String token) {
        return AuthHelper.requireUserId(userService, token);
    }

    // MNP update
    @GetMapping("/watched")
    public ResponseEntity<List<Integer>> getWatchedMovieIds(@RequestHeader("Authorization") String token) {
        UUID userId = extractUserIdFromToken(token);
        List<Integer> watchedMovieIds = userMovieInteractionService.getWatchedMovieIds(userId);
        return ResponseEntity.ok(watchedMovieIds);
    }

    @PutMapping("/unwatched/{tmdb_movie_id}")
    public ResponseEntity<String> unmarkMovieAsWatched(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer tmdb_movie_id) {

        UUID userId = extractUserIdFromToken(token);
        userMovieInteractionService.updateWatchStatus(userId, tmdb_movie_id, WatchStatus.NO_PLANS);
        return ResponseEntity.ok("Movie removed from watched.");
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<Integer>> getFavoriteMovieIds(@RequestHeader("Authorization") String token) {
        UUID userId = extractUserIdFromToken(token);
        List<Integer> favoriteMovieIds = userMovieInteractionService.getFavoriteMovieIds(userId);
        return ResponseEntity.ok(favoriteMovieIds);
    }

    @GetMapping("/watchlist")
    public ResponseEntity<List<Integer>> getWatchlistMovieIds(@RequestHeader("Authorization") String token) {
        UUID userId = extractUserIdFromToken(token);
        List<Integer> watchlistMovieIds = userMovieInteractionService.getWatchlistMovieIds(userId);
        return ResponseEntity.ok(watchlistMovieIds);
    }

    // Endpoint to get the count of watched movies
    @GetMapping("/watched-count")
    public ResponseEntity<Map<String, Integer>> getWatchedMoviesCount(@RequestHeader("Authorization") String token) {
        UUID userId = extractUserIdFromToken(token);
        List<Integer> watchedMovieIds = userMovieInteractionService.getWatchedMovieIds(userId);
        return ResponseEntity.ok(Map.of("count", watchedMovieIds.size()));
    }

    // Endpoint to get the count of favorite movies
    @GetMapping("/favorite-count")
    public ResponseEntity<Map<String, Integer>> getFavoriteMoviesCount(@RequestHeader("Authorization") String token) {
        UUID userId = extractUserIdFromToken(token);
        List<Integer> favoriteMovieIds = userMovieInteractionService.getFavoriteMovieIds(userId);
        return ResponseEntity.ok(Map.of("count", favoriteMovieIds.size()));
    }

    @GetMapping("/getuserinteractions")
    public ResponseEntity<List<Map<String, Object>>> getAllUserInteractions(
            @RequestHeader("Authorization") String token) {

        extractUserIdFromToken(token);
        List<Map<String, Object>> interactions = userMovieInteractionService.getUserInteractions(token);

        if (interactions.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(interactions);
    }

}
