package dev.team08.backend.service;

import dev.team08.backend.dto.response.AdminReviewResponse;
import dev.team08.backend.dto.response.AdminUserResponse;
import dev.team08.backend.entity.MovieReview;
import dev.team08.backend.entity.User;
import dev.team08.backend.repository.FollowedPersonRepository;
import dev.team08.backend.repository.MovieReviewRepository;
import dev.team08.backend.repository.NotificationRepository;
import dev.team08.backend.repository.TvProgressRepository;
import dev.team08.backend.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class AdminService {
    private final UserRepository userRepository;
    private final MovieReviewRepository movieReviewRepository;
    private final MovieReviewService movieReviewService;
    private final NotificationRepository notificationRepository;
    private final FollowedPersonRepository followedPersonRepository;
    private final TvProgressRepository tvProgressRepository;

    public AdminService(
            UserRepository userRepository,
            MovieReviewRepository movieReviewRepository,
            MovieReviewService movieReviewService,
            NotificationRepository notificationRepository,
            FollowedPersonRepository followedPersonRepository,
            TvProgressRepository tvProgressRepository) {
        this.userRepository = userRepository;
        this.movieReviewRepository = movieReviewRepository;
        this.movieReviewService = movieReviewService;
        this.notificationRepository = notificationRepository;
        this.followedPersonRepository = followedPersonRepository;
        this.tvProgressRepository = tvProgressRepository;
    }

    public Map<String, Object> stats() {
        Map<String, Object> map = new HashMap<>();
        map.put("userCount", userRepository.count());
        map.put("reviewCount", movieReviewRepository.count());
        return map;
    }

    public List<AdminUserResponse> listUsers() {
        return userRepository.findAll().stream()
                .map(u -> new AdminUserResponse(
                        u.getId().toString(),
                        u.getUsername(),
                        u.getEmail(),
                        u.getRole() != null ? u.getRole().getName() : "User",
                        u.getCreatedAt() != null ? u.getCreatedAt().toString() : null
                ))
                .toList();
    }

    public List<AdminReviewResponse> listReviews() {
        return movieReviewRepository.findAll().stream()
                .map(this::toReviewResponse)
                .toList();
    }

    @Transactional
    public void deleteUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (user.getRole() != null && "Admin".equals(user.getRole().getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot delete admin accounts");
        }
        notificationRepository.deleteByUser_Id(userId);
        followedPersonRepository.deleteByUser_Id(userId);
        tvProgressRepository.deleteByUser_Id(userId);
        userRepository.delete(user);
    }

    @Transactional
    public void deleteReview(UUID authorUserId, Integer tmdbMovieId) {
        movieReviewService.deleteReview(authorUserId, tmdbMovieId);
    }

    private AdminReviewResponse toReviewResponse(MovieReview r) {
        String text = r.isEdited() && r.getEditedReviewText() != null
                ? r.getEditedReviewText()
                : r.getOriginalReviewText();
        String username = r.getUser() != null ? r.getUser().getUsername() : "unknown";
        String userId = r.getUser() != null && r.getUser().getId() != null
                ? r.getUser().getId().toString() : null;
        return new AdminReviewResponse(
                userId,
                username,
                r.getTmdbMovieId(),
                text,
                r.isEdited(),
                r.getReviewSentiment()
        );
    }
}
