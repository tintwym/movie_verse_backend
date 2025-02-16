package dev.team08.movie_verse_backend.service;

import dev.team08.movie_verse_backend.entity.Movie;
import dev.team08.movie_verse_backend.entity.User;
import dev.team08.movie_verse_backend.entity.WatchedMovie;
import dev.team08.movie_verse_backend.interfaces.IWatchedMovieService;
import dev.team08.movie_verse_backend.repository.WatchedMovieRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class WatchedMovieService implements IWatchedMovieService {
    private final WatchedMovieRepository watchedMovieRepository;

    public WatchedMovieService(WatchedMovieRepository watchedMovieRepository) {
        this.watchedMovieRepository = watchedMovieRepository;
    }

    @Override
    public List<WatchedMovie> getUserWatchlist(User user) {
        return watchedMovieRepository.findByUser(user);
    }

    @Override
    public void addMovieToWatchlist(User user, Movie movie) {
        watchedMovieRepository.save(new WatchedMovie(user, movie, LocalDateTime.now()));
    }
}
