package dev.team08.movie_verse_backend.repository;

import dev.team08.movie_verse_backend.entity.MovieReview;
import dev.team08.movie_verse_backend.entity.ids.UserMovieInteractionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MovieReviewRepository extends JpaRepository<MovieReview, UserMovieInteractionId> {
    //MovieReview findByUserIdAndMovieId(UUID userId, UUID movieId);
    //List<MovieReview> findByMovieId(UUID movieId);
    List<MovieReview> findByUserId(UUID userId);
    List<MovieReview> findByUserInteraction_TmdbMovieId(Integer tmdbMovieId);
    //Optional<MovieReview> findByUserInteraction_User_IdAndUserInteraction_TmdbMovieId(UUID userId, Integer tmdbMovieId);
    @Query("SELECT mr FROM MovieReview mr WHERE mr.userInteraction.user.id = :userId AND mr.userInteraction.tmdbMovieId = :tmdbMovieId")
    Optional<MovieReview> findByUserIdAndTmdbMovieId(@Param("userId") UUID userId, @Param("tmdbMovieId") Integer tmdbMovieId);
    int countByUserId(UUID userId);
}
