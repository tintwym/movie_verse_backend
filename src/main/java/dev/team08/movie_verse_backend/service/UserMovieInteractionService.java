package dev.team08.movie_verse_backend.service;

import dev.team08.movie_verse_backend.entity.Genre;
import dev.team08.movie_verse_backend.entity.MovieReview;
import dev.team08.movie_verse_backend.entity.User;
import dev.team08.movie_verse_backend.entity.UserMovieInteraction;
import dev.team08.movie_verse_backend.enums.LikeStatus;
import dev.team08.movie_verse_backend.enums.WatchStatus;
import dev.team08.movie_verse_backend.interfaces.IUserMovieInteractionService;
import dev.team08.movie_verse_backend.repository.MovieReviewRepository;
import dev.team08.movie_verse_backend.repository.UserMovieInteractionRepository;
import dev.team08.movie_verse_backend.repository.UserRepository;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserMovieInteractionService implements IUserMovieInteractionService {

    private final UserMovieInteractionRepository userMovieInteractionRepository;
    private final MovieReviewRepository movieReviewRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public UserMovieInteractionService(UserMovieInteractionRepository userMovieInteractionRepository, 
    		UserRepository userRepository, MovieReviewRepository movieReviewRepository, UserService userService
    		) 
    {
        this.userMovieInteractionRepository = userMovieInteractionRepository;
        this.userRepository = userRepository;
        this.movieReviewRepository = movieReviewRepository;	
        this.userService = userService;
    }
    /**
     * Ensures that an interaction entry exists for the given user and movie.
     */
    private UserMovieInteraction getOrCreateInteraction(UUID userId, Integer tmdbMovieId) {
        return userMovieInteractionRepository.findByUser_IdAndTmdbMovieId(userId, tmdbMovieId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("User not found"));
                    UserMovieInteraction newInteraction = new UserMovieInteraction(user, tmdbMovieId);
                    return userMovieInteractionRepository.save(newInteraction);
                });
    }

    @Override
    @Transactional
    public void logMovieView(UUID userId, Integer tmdbMovieId) {
        UserMovieInteraction interaction = getOrCreateInteraction(userId, tmdbMovieId);
        interaction.setViews(interaction.getViews() + 1);  // ✅ Increment views
        userMovieInteractionRepository.save(interaction);
    }

    @Override
    @Transactional
    public void markMovieAsWatched(UUID userId, Integer tmdbMovieId) {
        UserMovieInteraction interaction = getOrCreateInteraction(userId, tmdbMovieId);
        interaction.setWatchStatus(WatchStatus.WATCHED);
        userMovieInteractionRepository.save(interaction);
    }

    @Override
    @Transactional
    public void likeOrDislikeMovie(UUID userId, Integer tmdbMovieId, LikeStatus likeStatus) {
        UserMovieInteraction interaction = getOrCreateInteraction(userId, tmdbMovieId);
        interaction.setLikeStatus(likeStatus);
        userMovieInteractionRepository.save(interaction);
    }

    @Override
    @Transactional
    public void toggleFavorite(UUID userId, Integer tmdbMovieId) {
        UserMovieInteraction interaction = getOrCreateInteraction(userId, tmdbMovieId);
        interaction.setFavorite(!interaction.isFavorite());
        userMovieInteractionRepository.save(interaction);
    }

    @Override
    @Transactional
    public void toggleWatchlist(UUID userId, Integer tmdbMovieId) {
        UserMovieInteraction interaction = getOrCreateInteraction(userId, tmdbMovieId);
        interaction.setWatchStatus(
                interaction.getWatchStatus() == WatchStatus.PLANNED? WatchStatus.NO_PLANS : WatchStatus.PLANNED
        );
        userMovieInteractionRepository.save(interaction);
    }
    @Override
    @Transactional
    public void addOrUpdateReview(UUID userId, Integer tmdbMovieId, String reviewText, boolean isEdit) {
        UserMovieInteraction interaction = getOrCreateInteraction(userId, tmdbMovieId);
        MovieReview review = interaction.getReview();

        if (review == null) {
            review = new MovieReview(interaction, reviewText);
            interaction.setReview(review);
        } else {
            if (isEdit) {
                review.editReview(reviewText);  // ✅ Track edited version
            } else {
                review.setOriginalReviewText(reviewText);
            }
        }

        movieReviewRepository.save(review);
        userMovieInteractionRepository.save(interaction);
    }
    
	@Override
	public Optional<UserMovieInteraction> getUserMovieInteraction(UUID userId, Integer tmdbMovieId) {
		return userMovieInteractionRepository.findByUser_IdAndTmdbMovieId(userId,tmdbMovieId);
	}
	
	@Override
	public List<UserMovieInteraction> getAllUserMovieInteractionsByUser(UUID userId) {
		return userMovieInteractionRepository.findByUser_Id(userId);
	}
	
	@Override
    public List<Map<String, Object>> getUserInteractions(String token) {
        User user = userService.getUserProfileFromToken(token);
        if (user == null) {
            throw new RuntimeException("User not found or token is invalid.");
        }
        UUID userId = user.getId();

        String user_types = user.getFavoriteGenres().stream()
                .map(Genre::getName)
                .distinct()
                .collect(Collectors.joining("/"));

        List<UserMovieInteraction> interactions = userMovieInteractionRepository.findAllByUser_Id(userId);
        List<Map<String, Object>> userInteractions = new ArrayList<>();

        // 如果没有交互记录，返回默认值
        if (interactions.isEmpty()) {
            Map<String, Object> userInteraction = new HashMap<>();
            userInteraction.put("tmdbMovieId", -1);
            userInteraction.put("user_types", user_types);
            userInteraction.put("rating", -1);
            userInteraction.put("favorite", -1);
            userInteraction.put("clicks", -1);
            userInteraction.put("watched", -1);
            userInteraction.put("planned", -1);
            userInteractions.add(userInteraction);
            return userInteractions;
        }

        // 将每个交互记录转换为 Map 格式
        for (UserMovieInteraction interaction : interactions) {
            Map<String, Object> userInteraction = new HashMap<>();
            userInteraction.put("tmdbMovieId", interaction.getTmdbMovieId());
            userInteraction.put("user_types", user_types);
            userInteraction.put("rating", interaction.getRating() != null ? interaction.getRating().getRating() : -1);
            userInteraction.put("favorite", interaction.isFavorite() ? 1 : 0);
            userInteraction.put("clicks", interaction.getViews());
            userInteraction.put("watched", interaction.getWatchStatus() == WatchStatus.WATCHED ? 1 : 0);
            userInteraction.put("planned", interaction.getWatchStatus() == WatchStatus.PLANNED ? 1 : 0);
            userInteractions.add(userInteraction);
        }
        return userInteractions;
    }

    @Override
    public List<String> callPythonRecommendApi(List<Map<String, Object>> userInteractions) {
        String pythonApiUrl = "http://127.0.0.1:5000/recommend";
        RestTemplate restTemplate = new RestTemplate();

        try {
            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // 将交互数据转换为 JSON 格式
            HttpEntity<List<Map<String, Object>>> requestEntity = new HttpEntity<>(userInteractions, headers);
            System.out.println(requestEntity);
            // 调用 Python API
            ResponseEntity<Map> response = restTemplate.exchange(
                    pythonApiUrl,
                    HttpMethod.POST,
                    requestEntity,
                    Map.class
            );
            System.out.println("Sending data to Python API: " + userInteractions);

            // 解析返回结果
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return (List<String>) response.getBody().get("recommendations");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Sending data to Python API: " + userInteractions);

            throw new RuntimeException("Failed to call Python API: " + e.getMessage());
        }

        return Collections.emptyList(); // 如果调用失败，返回空列表
    }

    // MNP update
    public List<Integer> getWatchedMovieIds(UUID userId) {
        return userMovieInteractionRepository.findWatchedMovieIdsByUserId(userId, WatchStatus.WATCHED);
    }

    public void updateWatchStatus(UUID userId, Integer tmdbMovieId, WatchStatus watchStatus) {
        UserMovieInteraction interaction = userMovieInteractionRepository.findByUserIdAndMovieId(userId, tmdbMovieId)
                .orElseThrow(() -> new RuntimeException("Movie not found for user"));

        interaction.setWatchStatus(watchStatus);
        userMovieInteractionRepository.save(interaction);
    }

    public List<Integer> getFavoriteMovieIds(UUID userId) {
        return userMovieInteractionRepository.findFavoriteMovieIdsByUserId(userId);
    }
}
