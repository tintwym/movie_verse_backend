package dev.team08.movie_verse_backend.controller.api;

import dev.team08.movie_verse_backend.entity.MovieRating;
import dev.team08.movie_verse_backend.interfaces.IMovieRatingService;
import dev.team08.movie_verse_backend.interfaces.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/ratings")
public class MovieRatingApiController {
	
	private final IMovieRatingService movieRatingService;
	private final IUserService userService;
	
	public MovieRatingApiController (IMovieRatingService movieRatingService, IUserService userService) {
		this.movieRatingService = movieRatingService;
		this.userService = userService;
	}

    @PostMapping("/{tmdb_movie_id}")
    public ResponseEntity<String> rateMovie(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer tmdb_movie_id,
            @RequestParam Double rating) {

        UUID userId = extractUserIdFromToken(token);

        movieRatingService.addOrUpdateRating(userId, tmdb_movie_id, rating);
        return ResponseEntity.ok("Rating submitted successfully.");
    }

    /**
     * ✅ Delete a rating.
     */
    @DeleteMapping("/{tmdb_movie_id}")
    public ResponseEntity<String> deleteRating(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer tmdb_movie_id) {

        UUID userId = extractUserIdFromToken(token);
        movieRatingService.deleteRating(userId, tmdb_movie_id);
        return ResponseEntity.ok("Rating deleted successfully.");
    }

    /**
     * ✅ Get a user's rating for a movie.
     */
    @GetMapping("/{tmdb_movie_id}/user")
    public ResponseEntity<?> getUserRating(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer tmdb_movie_id) {

        UUID userId = extractUserIdFromToken(token);
        Optional<MovieRating> rating = movieRatingService.getUserRating(userId, tmdb_movie_id);
        return rating.isPresent() ? ResponseEntity.ok(rating) : ResponseEntity.notFound().build();
    }

    /**
     * ✅ Get average rating of a movie.
     */
    @GetMapping("/{tmdb_movie_id}/average")
    public ResponseEntity<Double> getAverageRating(@PathVariable Integer tmdb_movie_id) {
        return ResponseEntity.ok(movieRatingService.getAverageMovieRating(tmdb_movie_id));
    }
	
    private UUID extractUserIdFromToken(String token) {
        return userService.getUserFromToken(token).getId();
    }
    
}
