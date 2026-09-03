package dev.team08.backend.service;

import dev.team08.backend.entity.Genre;
import dev.team08.backend.entity.MovieReview;
import dev.team08.backend.entity.User;
import dev.team08.backend.entity.UserMovieInteraction;
import dev.team08.backend.enums.LikeStatus;
import dev.team08.backend.enums.WatchStatus;
import dev.team08.backend.interfaces.IUserMovieInteractionService;
import dev.team08.backend.repository.MovieReviewRepository;
import dev.team08.backend.repository.UserMovieInteractionRepository;
import dev.team08.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        WatchStatus status = interaction.getWatchStatus();
        // Only toggle NO_PLANS <-> PLANNED. Never overwrite WATCHED.
        if (status == WatchStatus.PLANNED) {
            interaction.setWatchStatus(WatchStatus.NO_PLANS);
        } else if (status == WatchStatus.NO_PLANS) {
            interaction.setWatchStatus(WatchStatus.PLANNED);
        }
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
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.UNAUTHORIZED,
                    "Invalid or expired token");
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

    /**
     * Genre-based recommendations: fetches user's favorite genres and
     * returns TMDB genre IDs for the frontend to discover new movies.
     */
    @Override
    public List<String> getRecommendedMovieIds(List<Map<String, Object>> userInteractions) {
        if (userInteractions == null || userInteractions.isEmpty()) {
            return Collections.emptyList();
        }

        // Extract genre preferences from interaction data
        String userTypes = userInteractions.stream()
                .map(row -> (String) row.get("user_types"))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse("");

        if (userTypes.isEmpty()) {
            return Collections.emptyList();
        }

        // Return genre names so clients can fetch TMDB discover results
        return Arrays.stream(userTypes.split("/"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .distinct()
                .limit(5)
                .collect(Collectors.toList());
    }

    // MNP update
    @Override
    public List<Integer> getWatchedMovieIds(UUID userId) {
        return userMovieInteractionRepository.findWatchedMovieIdsByUserId(userId, WatchStatus.WATCHED);
    }

    @Override
    public void updateWatchStatus(UUID userId, Integer tmdbMovieId, WatchStatus watchStatus) {
        userMovieInteractionRepository.findByUserIdAndMovieId(userId, tmdbMovieId)
                .ifPresent(interaction -> {
                    interaction.setWatchStatus(watchStatus);
                    userMovieInteractionRepository.save(interaction);
                });
    }

    @Override
    public List<Integer> getFavoriteMovieIds(UUID userId) {
        return userMovieInteractionRepository.findFavoriteMovieIdsByUserId(userId);
    }

    @Override
    public List<Integer> getWatchlistMovieIds(UUID userId) {
        return userMovieInteractionRepository.findWatchlistMovieIdsByUserId(userId, WatchStatus.PLANNED);
    }
}
