package dev.team08.movie_verse_backend.repository;

import dev.team08.movie_verse_backend.entity.MovieRating;
import dev.team08.movie_verse_backend.entity.User;
import dev.team08.movie_verse_backend.entity.ids.UserMovieInteractionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface MovieRatingRepository extends JpaRepository<MovieRating, UserMovieInteractionId> {
//    // Find ratings by user ID and movie ID
//    MovieRating findByUserIdAndMovieId(UUID userId, UUID movieId);
//
//    // Find all ratings for a specific movie
//    List<MovieRating> findByMovieId(UUID movieId);
//
//    // Find all ratings by a specific user
//    List<MovieRating> findByUserId(UUID userId);
//
//    @Query("SELECT AVG(r.rating) FROM MovieRating r WHERE r.movie.id = :movieId")
//    Double findAverageRatingByMovieId(UUID movieId);
//
//    @Query("SELECT AVG(mr.rating) FROM MovieRating mr WHERE mr.movie.id = :movieId")
//    Double calculateAverageRatingByMovieId(@Param("movieId") UUID movieId);

	Optional<MovieRating> findByUserInteraction_UserAndUserInteraction_TmdbMovieId(User user, Integer tmdbMovieId);

	@Query("SELECT AVG(mr.rating) FROM MovieRating mr WHERE mr.tmdbMovieId = :tmdbMovieId")
	Optional<Double> getAverageRatingByTmdbMovieId(@Param("tmdbMovieId") Integer tmdbMovieId);

}
