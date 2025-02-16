package dev.team08.movie_verse_backend.service;

import dev.team08.movie_verse_backend.entity.MovieRating;
import dev.team08.movie_verse_backend.entity.User;
import dev.team08.movie_verse_backend.entity.UserMovieInteraction;
import dev.team08.movie_verse_backend.interfaces.IMovieRatingService;
import dev.team08.movie_verse_backend.repository.MovieRatingRepository;
import dev.team08.movie_verse_backend.repository.MovieRepository;
import dev.team08.movie_verse_backend.repository.UserMovieInteractionRepository;
import dev.team08.movie_verse_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class MovieRatingService implements IMovieRatingService {
    @Autowired
    private MovieRatingRepository movieRatingRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserMovieInteractionRepository userMovieInteractionRepository;
    
    /**
     * ✅ Add or update a rating for a movie.
     */
    @Override
    @Transactional
    public void addOrUpdateRating(UUID userId, Integer tmdbMovieId, Double rating) {
        if (rating < 1.0 || rating > 5.0) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }

        UserMovieInteraction interaction = userMovieInteractionRepository
                .findByUser_IdAndTmdbMovieId(userId, tmdbMovieId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("User not found"));
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

        //movieRatingRepository.save(movieRating);
        userMovieInteractionRepository.save(interaction);
    }

    /**
     * ✅ Delete a rating for a movie.
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
     * ✅ Get a user's rating for a specific movie.
     */
    @Override
    public Optional<MovieRating> getUserRating(UUID userId, Integer tmdbMovieId) {
    	User user = userRepository.findById(userId)
                 .orElseThrow(() -> new RuntimeException("User not found"));
        return movieRatingRepository.findByUserInteraction_UserAndUserInteraction_TmdbMovieId( user,  tmdbMovieId);
    }

    /**
     * ✅ Get the average rating of a movie.
     */
    @Override
    public Double getAverageMovieRating(Integer tmdbMovieId) {
        return movieRatingRepository.getAverageRatingByTmdbMovieId(tmdbMovieId).orElse(0.0);
    }

//    public List<MovieRating> getAllRatings() {
//        return movieRatingRepository.findAll();
//    }
//
//    public MovieRating getRatingById(UUID id) {
//        return movieRatingRepository.findById(id).orElseThrow(() -> new RuntimeException("Rating not found"));
//    }
//
//    public MovieRating createRating(UUID movieId, UUID userId, int rating) {
//        Movie movie = movieRepository.findById(movieId)
//                .orElseThrow(() -> new RuntimeException("Movie not found"));
//
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//        MovieRating newrating = new MovieRating();
//        newrating.setUser(user);
//        newrating.setMovie(movie);
//        newrating.setRating(rating);
//        return movieRatingRepository.save(newrating);
//    }
//
//    public MovieRating updateRating(UUID id, int rating) {
//        // Fetch the existing rating by ID
//        MovieRating existingMovieRating = movieRatingRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Rating not found"));
//
//        // Update the relevant fields
//        existingMovieRating.setRating(rating);
//
//        // Save the updated review
//        return movieRatingRepository.save(existingMovieRating);
//    }
//
//    public void deleteRating(UUID id) {
//        movieRatingRepository.deleteById(id);
//    }
//
//    public MovieRating getRatingByUserAndMovie(UUID userId, UUID movieId) {
//        return movieRatingRepository.findByUserIdAndMovieId(userId, movieId);
//    }
//
//
//    public List<MovieRating> getRatingsByMovieId(UUID movieId) {
//        return movieRatingRepository.findByMovieId(movieId);
//    }
//
//    public Double getAverageRatingByMovie(UUID movieId) {
//        return movieRatingRepository.findAverageRatingByMovieId(movieId);
//    }
//
//    public List<MovieRating> getRatingsByUserId(UUID userId) {
//        return movieRatingRepository.findByUserId(userId);
//    }

}


