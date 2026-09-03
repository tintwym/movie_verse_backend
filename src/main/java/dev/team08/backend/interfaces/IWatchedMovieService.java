package dev.team08.backend.interfaces;

import dev.team08.backend.entity.Movie;
import dev.team08.backend.entity.User;
import dev.team08.backend.entity.WatchedMovie;

import java.util.List;

public interface IWatchedMovieService {
    List<WatchedMovie> getUserWatchlist(User user);
    void addMovieToWatchlist(User user, Movie movie);
}
