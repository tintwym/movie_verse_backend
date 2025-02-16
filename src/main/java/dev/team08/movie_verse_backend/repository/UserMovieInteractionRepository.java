package dev.team08.movie_verse_backend.repository;

import dev.team08.movie_verse_backend.entity.UserMovieInteraction;
import dev.team08.movie_verse_backend.entity.ids.UserMovieInteractionId;
import dev.team08.movie_verse_backend.enums.WatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserMovieInteractionRepository extends JpaRepository<UserMovieInteraction, UserMovieInteractionId> {

    Optional<UserMovieInteraction> findByUser_IdAndTmdbMovieId(UUID userId, Integer tmdbMovieId);

    List<UserMovieInteraction> findByUser_Id(UUID userId);

	List<UserMovieInteraction> findAllByUser_Id(UUID userId);
    
    // MNP update
    @Query("SELECT m.tmdbMovieId FROM UserMovieInteraction m WHERE m.user.id = :userId AND m.watchStatus = :watchStatus")
    List<Integer> findWatchedMovieIdsByUserId(@Param("userId") UUID userId, @Param("watchStatus") WatchStatus watchStatus);

    @Query("SELECT m.tmdbMovieId FROM UserMovieInteraction m WHERE m.user.id = :userId AND m.favorite = true")
    List<Integer> findFavoriteMovieIdsByUserId(@Param("userId") UUID userId);


    @Query("SELECT m FROM UserMovieInteraction m WHERE m.user.id = :userId AND m.tmdbMovieId = :movieId")
    Optional<UserMovieInteraction> findByUserIdAndMovieId(@Param("userId") UUID userId, @Param("movieId") Integer movieId);

}
