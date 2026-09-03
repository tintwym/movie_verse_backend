package dev.team08.backend.service;

import dev.team08.backend.entity.MovieRating;
import dev.team08.backend.entity.User;
import dev.team08.backend.entity.UserMovieInteraction;
import dev.team08.backend.interfaces.IMovieRatingService;
import dev.team08.backend.repository.MovieRatingRepository;
import dev.team08.backend.repository.UserMovieInteractionRepository;
import dev.team08.backend.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@Service
public class MovieRatingService implements IMovieRatingService {
    private final MovieRatingRepository movieRatingRepository;
    private final UserRepository userRepository;
    private final UserMovieInteractionRepository userMovieInteractionRepository;

    public MovieRatingService(
            MovieRatingRepository movieRatingRepository,
            UserRepository userRepository,
            UserMovieInteractionRepository userMovieInteractionRepository) {
        this.movieRatingRepository = movieRatingRepository;
        this.userRepository = userRepository;
        this.userMovieInteractionRepository = userMovieInteractionRepository;
    }

    /**
     * Add or update a rating for a movie.
     */
    @Override
    @Transactional
    public void addOrUpdateRating(UUID userId, Integer tmdbMovieId, Double rating) {
        if (rating == null || rating < 1.0 || rating > 5.0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rating must be between 1 and 5.");
        }

        UserMovieInteraction interaction = userMovieInteractionRepository
                .findByUser_IdAndTmdbMovieId(userId, tmdbMovieId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "User not found"));
                     UserMovieInteraction newInteraction = new UserMovieInteraction(user, tmdbMovieId);
                     return userMovieInteractionRepository.save(newInteraction);
                });

        MovieRating movieRating = interaction.getRating();

        if (movieRating == null) {
            movieRating = new MovieRating(interaction, rating);
            movieRating.setUser(interaction.getUser());
            movieRating.setTmdbMovieId(interaction.getTmdbMovieId());
            interaction.setRating(movieRating);
        } else {
            movieRating.setRating(rating);
        }

        userMovieInteractionRepository.save(interaction);
    }

    /**
     * Delete a rating for a movie.
     */
    @Override
    @Transactional
    public void deleteRating(UUID userId, Integer tmdbMovieId) {
        userMovieInteractionRepository.findByUser_IdAndTmdbMovieId(userId, tmdbMovieId).ifPresent(interaction -> {
            if (interaction.getRating() != null) {
                movieRatingRepository.delete(interaction.getRating());
                interaction.setRating(null);
                userMovieInteractionRepository.save(interaction);
            }
        });
    }

    /**
     * Get a user's rating for a specific movie.
     */
    @Override
    public Optional<MovieRating> getUserRating(UUID userId, Integer tmdbMovieId) {
    	User user = userRepository.findById(userId)
                 .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "User not found"));
        return movieRatingRepository.findByUserInteraction_UserAndUserInteraction_TmdbMovieId( user,  tmdbMovieId);
    }

    /**
     * Get the average rating of a movie.
     */
    @Override
    public Double getAverageMovieRating(Integer tmdbMovieId) {
        return movieRatingRepository.getAverageRatingByTmdbMovieId(tmdbMovieId).orElse(0.0);
    }
}
