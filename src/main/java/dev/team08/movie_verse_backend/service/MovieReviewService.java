package dev.team08.movie_verse_backend.service;

import dev.team08.movie_verse_backend.dto.request.MLReviewRequest;
import dev.team08.movie_verse_backend.dto.response.MLReviewResponse;
import dev.team08.movie_verse_backend.entity.MovieReview;
import dev.team08.movie_verse_backend.entity.User;
import dev.team08.movie_verse_backend.entity.UserMovieInteraction;
import dev.team08.movie_verse_backend.interfaces.IMovieReviewService;
import dev.team08.movie_verse_backend.repository.MovieReviewRepository;
import dev.team08.movie_verse_backend.repository.UserMovieInteractionRepository;
import dev.team08.movie_verse_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MovieReviewService implements IMovieReviewService {
    @Autowired
    private MovieReviewRepository movieReviewRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private  UserMovieInteractionRepository userMovieInteractionRepository;
    
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
        
        review.setReviewSentiment(callPythonReviewSentimentApi(reviewText));
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
    public List<MovieReview> getReviewsByMovieId(Integer tmdbMovieId) {
        return movieReviewRepository.findByUserInteraction_TmdbMovieId(tmdbMovieId);
    }

    /**
     * Get a user's review for a specific movie.
     */
    @Override
    public Optional<MovieReview> getUserReview(UUID userId, Integer tmdbMovieId) {
        return movieReviewRepository.findByUserIdAndTmdbMovieId(userId, tmdbMovieId);
    }
    
    @Override
    public String callPythonReviewSentimentApi(String review) {
        String pythonApiUrl = "http://127.0.0.1:5001/predict";
        RestTemplate restTemplate = new RestTemplate();
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            MLReviewRequest request = new MLReviewRequest(review);
            HttpEntity<MLReviewRequest> requestEntity = new HttpEntity<>(request, headers);
            MLReviewResponse response = restTemplate.postForObject(pythonApiUrl, requestEntity, MLReviewResponse.class);
            return response != null && response.getResults() != null ? response.getResults() : "No results found";
        } catch (Exception e) {
            throw new RuntimeException("Failed to call Python API", e);
        }
    }

    @Override
    public int getReviewCountByUserId(UUID userId) {
        return movieReviewRepository.countByUserId(userId);
    }


}

