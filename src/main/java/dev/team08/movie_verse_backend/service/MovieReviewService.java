package dev.team08.movie_verse_backend.service;

import dev.team08.movie_verse_backend.dto.request.MLReviewRequest;
import dev.team08.movie_verse_backend.dto.response.MLReviewResponse;
import dev.team08.movie_verse_backend.entity.MovieReview;
import dev.team08.movie_verse_backend.entity.User;
import dev.team08.movie_verse_backend.entity.UserMovieInteraction;
import dev.team08.movie_verse_backend.interfaces.IMovieReviewService;
import dev.team08.movie_verse_backend.repository.MovieRepository;
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
    private MovieRepository movieRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private  UserMovieInteractionRepository userMovieInteractionRepository;
    
    /**
     * ✅ Add a new review or update an existing one.
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
        System.out.println(review.getReviewSentiment());;

        //movieReviewRepository.save(review);
        userMovieInteractionRepository.save(interaction);
    }

    /**
     * ✅ Delete a review.
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
     * ✅ Get all reviews for a movie.
     */
    @Override
    public List<MovieReview> getReviewsByMovieId(Integer tmdbMovieId) {
        return movieReviewRepository.findByUserInteraction_TmdbMovieId(tmdbMovieId);
    }

    /**
     * ✅ Get a user's review for a specific movie.
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
    	      // 设置请求头
    	      HttpHeaders headers = new HttpHeaders();
    	      headers.setContentType(MediaType.APPLICATION_JSON);
    	      MLReviewRequest request = new MLReviewRequest(review);

    	      // 将交互数据转换为 JSON 格式
    	      HttpEntity<MLReviewRequest> requestEntity = new HttpEntity<>(request, headers);

    	      // 调用 Python API
//    	      ResponseEntity<Map> response = restTemplate.exchange(
//    	              pythonApiUrl,
//    	              HttpMethod.POST,
//    	              requestEntity,
//    	              Map.class
//    	      );
    	        MLReviewResponse response = restTemplate.postForObject(pythonApiUrl, requestEntity, MLReviewResponse.class);

    	      System.out.println("Sending data to Python API: " + review);
              System.out.println("Response from Python API: " + response.getResults());;
    	      return response != null ? response.getResults() : "No results found";

    	  } catch (Exception e) {
    	      e.printStackTrace();

    	      throw new RuntimeException("Failed to call Python API: " + e.getMessage());
    	  }
    	}

//    public List<MovieReview> getAllReviews() {
//        return movieReviewRepository.findAll();
//    }
//
//    public MovieReview getReviewById(UUID id) {
//        return movieReviewRepository.findById(id).orElseThrow(() -> new RuntimeException("Review not found"));
//    }
//
//    public MovieReview createReview(UUID movieId, UUID userId, String content) {
//        Movie movie = movieRepository.findById(movieId)
//                .orElseThrow(() -> new RuntimeException("Movie not found"));
//
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        MovieReview movieReview = new MovieReview();
//        movieReview.setMovie(movie);
//        movieReview.setUser(user);
//        movieReview.setContent(content);
//        return movieReviewRepository.save(movieReview);
//    }
//
//    public MovieReview updateReview(UUID id, String content) {
//        // Fetch the existing review by ID
//        MovieReview existingMovieReview = movieReviewRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Review not found"));
//
//        // Update the relevant fields
//        existingMovieReview.setContent(content);
//
//        // Save the updated review
//        return movieReviewRepository.save(existingMovieReview);
//    }
//
//    public void deleteReview(UUID id) {
//        movieReviewRepository.deleteById(id);
//    }
//
//    public MovieReview getReviewByUserAndMovie(UUID userId, UUID movieId) {
//        return movieReviewRepository.findByUserIdAndMovieId(userId, movieId);
//    }
//
//    public List<MovieReview> getReviewsByMovieId(UUID movieId) {
//        return movieReviewRepository.findByMovieId(movieId);
//    }
//
//    public List<MovieReview> getReviewsByUserId(UUID userId) {
//        return movieReviewRepository.findByUserId(userId);
//    }
//
//    public void incrementLikes(UUID reviewId) {
//        MovieReview movieReview = movieReviewRepository.findById(reviewId)
//                .orElseThrow(() -> new EntityNotFoundException("Review not found with ID: " + reviewId));
//
//        movieReview.setLikes(movieReview.getLikes() + 1);
//        movieReviewRepository.save(movieReview);
//    }
    public int getReviewCountByUserId(UUID userId) {
        return movieReviewRepository.countByUserId(userId);
    }


}

