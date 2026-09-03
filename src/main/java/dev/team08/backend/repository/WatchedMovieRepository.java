package dev.team08.backend.repository;

import dev.team08.backend.entity.Movie;
import dev.team08.backend.entity.User;
import dev.team08.backend.entity.WatchedMovie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WatchedMovieRepository extends JpaRepository<WatchedMovie, UUID> {
    List<WatchedMovie> findByUser(User user);
    Optional<WatchedMovie> findByUserAndMovie(User user, Movie movie);
}
