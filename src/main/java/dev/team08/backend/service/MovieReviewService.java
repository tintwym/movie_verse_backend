package dev.team08.backend.service;

import dev.team08.backend.dto.response.CommunityReviewResponse;
import dev.team08.backend.entity.MovieReview;
import dev.team08.backend.entity.User;
import dev.team08.backend.entity.UserMovieInteraction;
import dev.team08.backend.interfaces.IMovieReviewService;
import dev.team08.backend.repository.MovieReviewRepository;
import dev.team08.backend.repository.UserMovieInteractionRepository;
import dev.team08.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MovieReviewService implements IMovieReviewService {
    private final MovieReviewRepository movieReviewRepository;
    private final UserRepository userRepository;
    private final UserMovieInteractionRepository userMovieInteractionRepository;

    public MovieReviewService(
            MovieReviewRepository movieReviewRepository,
            UserRepository userRepository,
            UserMovieInteractionRepository userMovieInteractionRepository) {
        this.movieReviewRepository = movieReviewRepository;
        this.userRepository = userRepository;
        this.userMovieInteractionRepository = userMovieInteractionRepository;
    }

    /**
     * Add a new review or update an existing one.
     */
    @Override
    @Transactional
    public void addOrUpdateReview(UUID userId, Integer tmdbMovieId, String reviewText, boolean isEdit) {
        UserMovieInteraction interaction = userMovieInteractionRepository
                .findByUser_IdAndTmdbMovieId(userId, tmdbMovieId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("User not found"));
                     UserMovieInteraction newInteraction = new UserMovieInteraction(user, tmdbMovieId);
                     return userMovieInteractionRepository.save(newInteraction);
                });

        MovieReview review = interaction.getReview();

        if (review == null) {
            review = new MovieReview(interaction, reviewText);
            review.setUser(interaction.getUser());
            review.setTmdbMovieId(interaction.getTmdbMovieId());
            interaction.setReview(review);
        } else if (isEdit) {
            review.editReview(reviewText);
        } else {
            review.setOriginalReviewText(reviewText);
        }

        userMovieInteractionRepository.save(interaction);
    }

    /**
     * Delete a review.
     */
    @Override
    @Transactional
    public void deleteReview(UUID userId, Integer tmdbMovieId) {
        userMovieInteractionRepository.findByUser_IdAndTmdbMovieId(userId, tmdbMovieId).ifPresent(interaction -> {
            if (interaction.getReview() != null) {
                movieReviewRepository.delete(interaction.getReview());
                interaction.setReview(null);
                userMovieInteractionRepository.save(interaction);
            }
        });
    }

    /**
     * Get all reviews for a movie.
     */
    @Override
    @Transactional(readOnly = true)
    public List<MovieReview> getReviewsByMovieId(Integer tmdbMovieId) {
        return movieReviewRepository.findByUserInteraction_TmdbMovieId(tmdbMovieId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommunityReviewResponse> getCommunityReviews(Integer tmdbMovieId) {
        return movieReviewRepository.findByUserInteraction_TmdbMovieId(tmdbMovieId).stream()
                .map(review -> {
                    String text = review.getEditedReviewText() != null && !review.getEditedReviewText().isBlank()
                            ? review.getEditedReviewText()
                            : review.getOriginalReviewText();
                    Double rating = null;
                    if (review.getUserInteraction() != null && review.getUserInteraction().getRating() != null) {
                        rating = review.getUserInteraction().getRating().getRating();
                    }
                    String username = review.getUser() != null ? review.getUser().getUsername() : "Anonymous";
                    return new CommunityReviewResponse(username, text, review.isEdited(), rating);
                })
                .toList();
    }

    /**
     * Get a user's review for a specific movie.
     */
    @Override
    public Optional<MovieReview> getUserReview(UUID userId, Integer tmdbMovieId) {
        return movieReviewRepository.findByUserIdAndTmdbMovieId(userId, tmdbMovieId);
    }

    @Override
    public int getReviewCountByUserId(UUID userId) {
        return movieReviewRepository.countByUserId(userId);
    }
}
