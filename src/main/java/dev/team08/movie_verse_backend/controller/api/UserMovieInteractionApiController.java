package dev.team08.movie_verse_backend.controller.api;

import dev.team08.movie_verse_backend.entity.UserMovieInteraction;
import dev.team08.movie_verse_backend.enums.LikeStatus;
import dev.team08.movie_verse_backend.enums.WatchStatus;
import dev.team08.movie_verse_backend.interfaces.IUserMovieInteractionService;
import dev.team08.movie_verse_backend.interfaces.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

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

    @GetMapping("/{tmdb_movie_id}")
    public ResponseEntity<Optional<UserMovieInteraction>> getUserMovieInteraction(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer tmdb_movie_id) {

        UUID userId = extractUserIdFromToken(token);
        Optional<UserMovieInteraction> interaction = userMovieInteractionService.getUserMovieInteraction(userId, tmdb_movie_id);
        return ResponseEntity.ok(interaction);
    }
    
    @GetMapping("/recommend")
    //@CrossOrigin(origins = "http://localhost:3000")
    //@RequestMapping(method = RequestMethod.OPTIONS, value = "/api/user-interactions/recommend")
    public Object getUserInteractions(@RequestHeader("Authorization") String token){
        try {
            // 获取用户交互数据
            List<Map<String, Object>> userInteractions = userMovieInteractionService.getUserInteractions(token);

            // 调用 Python 推荐服务
            List<String> recommendations = userMovieInteractionService.callPythonRecommendApi(userInteractions);
            List<Integer> integerList = recommendations.stream()
                    .map(obj -> Integer.parseInt(obj.toString())) // Convert Object → String → Integer
                    .collect(Collectors.toList());
            return ResponseEntity.ok(integerList);

        } catch (Exception e) {
            // 捕获错误并返回 HTTP 500 响应
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonList(Map.of("error", e.getMessage())));
        }
    }

    private UUID extractUserIdFromToken(String token) {
        return userService.getUserFromToken(token).getId();
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

        // Fetch user interaction data from the service layer
        List<Map<String, Object>> interactions = userMovieInteractionService.getUserInteractions(token);

        if (interactions.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(interactions);
    }

}
