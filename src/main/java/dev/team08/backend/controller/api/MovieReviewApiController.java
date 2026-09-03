package dev.team08.backend.controller.api;

import dev.team08.backend.interfaces.IMovieReviewService;
import dev.team08.backend.interfaces.IUserService;
import dev.team08.backend.utility.AuthHelper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/reviews")
public class MovieReviewApiController {
	
	private final IMovieReviewService movieReviewService;
	private final IUserService userService;
	
	public MovieReviewApiController (IMovieReviewService movieReviewService, IUserService userService) {
		this.movieReviewService = movieReviewService;
		this.userService = userService;
	}
	
    @PostMapping("/{tmdb_movie_id}")
    public ResponseEntity<String> addOrUpdateReview(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer tmdb_movie_id,
            @RequestParam String reviewText,
            @RequestParam(defaultValue = "false") boolean isEdit) {

        UUID userId = extractUserIdFromToken(token);
        movieReviewService.addOrUpdateReview(userId, tmdb_movie_id, reviewText, isEdit);
        return ResponseEntity.ok(isEdit ? "Review updated successfully." : "Review added successfully.");
    }

    /**
     * ✅ Delete a review.
     */
    @DeleteMapping("/{tmdb_movie_id}")
    public ResponseEntity<String> deleteReview(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer tmdb_movie_id) {

        UUID userId = extractUserIdFromToken(token);
        movieReviewService.deleteReview(userId, tmdb_movie_id);
        return ResponseEntity.ok("Review deleted successfully.");
    }

    /**
     * ✅ Get all reviews for a movie.
     */
    @GetMapping("/{tmdb_movie_id}")
    public ResponseEntity<?> getReviewsForMovie(@PathVariable Integer tmdb_movie_id) {
        return ResponseEntity.ok(movieReviewService.getReviewsByMovieId(tmdb_movie_id));
    }

    /**
     * ✅ Get a user's review for a specific movie.
     */
    @GetMapping("/{tmdb_movie_id}/user")
    public ResponseEntity<?> getUserReview(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer tmdb_movie_id) {

        UUID userId = extractUserIdFromToken(token);
        return ResponseEntity.ok(movieReviewService.getUserReview(userId, tmdb_movie_id));
    }
    
    private UUID extractUserIdFromToken(String token) {
        return AuthHelper.requireUserId(userService, token);
    }

    @GetMapping("/user/review-count")
    public ResponseEntity<?> getReviewCountByUser(@RequestHeader("Authorization") String token) {
        UUID userId = extractUserIdFromToken(token);
        int reviewCount = movieReviewService.getReviewCountByUserId(userId);
        return ResponseEntity.ok(Map.of("reviewCount", reviewCount));
    }


}
