package dev.team08.movie_verse_backend.repository;

import dev.team08.movie_verse_backend.entity.Movie;
import dev.team08.movie_verse_backend.entity.User;
import dev.team08.movie_verse_backend.entity.WatchedMovie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WatchedMovieRepository extends JpaRepository<WatchedMovie, UUID> {
    List<WatchedMovie> findByUser(User user);
    Optional<WatchedMovie> findByUserAndMovie(User user, Movie movie);
}
