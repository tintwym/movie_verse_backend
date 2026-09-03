package dev.team08.backend.service;

import dev.team08.backend.dto.request.FollowCreditCheckRequest;
import dev.team08.backend.dto.request.FollowPersonRequest;
import dev.team08.backend.dto.response.FollowedPersonResponse;
import dev.team08.backend.entity.FollowedPerson;
import dev.team08.backend.entity.User;
import dev.team08.backend.repository.FollowedPersonRepository;
import dev.team08.backend.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class FollowService {
    private final FollowedPersonRepository followedPersonRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public FollowService(
            FollowedPersonRepository followedPersonRepository,
            UserRepository userRepository,
            NotificationService notificationService) {
        this.followedPersonRepository = followedPersonRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    public List<FollowedPersonResponse> list(UUID userId) {
        return followedPersonRepository.findByUser_IdOrderByCreatedAtDesc(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    public boolean isFollowing(UUID userId, Integer tmdbPersonId) {
        return followedPersonRepository.existsByUser_IdAndTmdbPersonId(userId, tmdbPersonId);
    }

    @Transactional
    public FollowedPersonResponse follow(UUID userId, FollowPersonRequest request) {
        if (request.getTmdbPersonId() == null || request.getPersonName() == null || request.getPersonName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Person id and name required");
        }
        return followedPersonRepository.findByUser_IdAndTmdbPersonId(userId, request.getTmdbPersonId())
                .map(this::toResponse)
                .orElseGet(() -> {
                    try {
                        User user = userRepository.findById(userId)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
                        FollowedPerson fp = followedPersonRepository.save(new FollowedPerson(
                                user,
                                request.getTmdbPersonId(),
                                request.getPersonName().trim(),
                                request.getProfilePath()
                        ));
                        notificationService.create(
                                userId,
                                "follow",
                                "Following " + fp.getPersonName(),
                                "You'll get updates when " + fp.getPersonName() + " appears in new titles.",
                                "/people/" + fp.getTmdbPersonId()
                        );
                        return toResponse(fp);
                    } catch (org.springframework.dao.DataIntegrityViolationException e) {
                        return followedPersonRepository
                                .findByUser_IdAndTmdbPersonId(userId, request.getTmdbPersonId())
                                .map(this::toResponse)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT, "Already following"));
                    }
                });
    }

    @Transactional
    public void unfollow(UUID userId, Integer tmdbPersonId) {
        followedPersonRepository.deleteByUser_IdAndTmdbPersonId(userId, tmdbPersonId);
    }

    @Transactional
    public int checkCredits(UUID userId, List<FollowCreditCheckRequest> credits) {
        if (credits == null || credits.isEmpty()) {
            return 0;
        }
        int created = 0;
        for (FollowCreditCheckRequest credit : credits) {
            if (credit.getTmdbPersonId() == null || credit.getCreditId() == null) {
                continue;
            }
            FollowedPerson fp = followedPersonRepository
                    .findByUser_IdAndTmdbPersonId(userId, credit.getTmdbPersonId())
                    .orElse(null);
            if (fp == null) {
                continue;
            }
            if (Objects.equals(fp.getLastNotifiedCreditId(), credit.getCreditId())) {
                continue;
            }
            // Skip first sync so following doesn't flood with old credits
            if (fp.getLastNotifiedCreditId() == null) {
                fp.setLastNotifiedCreditId(credit.getCreditId());
                followedPersonRepository.save(fp);
                continue;
            }
            String media = credit.getMediaType() != null && credit.getMediaType().equalsIgnoreCase("tv")
                    ? "tv" : "movie";
            String title = credit.getTitle() != null ? credit.getTitle() : "a new title";
            notificationService.create(
                    userId,
                    "credit",
                    fp.getPersonName() + " — new credit",
                    fp.getPersonName() + " is in \"" + title + "\""
                            + (credit.getReleaseDate() != null ? " (" + credit.getReleaseDate() + ")" : "") + ".",
                    "/" + ("tv".equals(media) ? "tv" : "movies") + "/" + credit.getCreditId()
            );
            fp.setLastNotifiedCreditId(credit.getCreditId());
            followedPersonRepository.save(fp);
            created++;
        }
        return created;
    }

    private FollowedPersonResponse toResponse(FollowedPerson fp) {
        return new FollowedPersonResponse(
                fp.getId().toString(),
                fp.getTmdbPersonId(),
                fp.getPersonName(),
                fp.getProfilePath(),
                fp.getLastNotifiedCreditId()
        );
    }
}
