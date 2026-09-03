package dev.team08.backend.service;

import dev.team08.backend.dto.request.TvProgressRequest;
import dev.team08.backend.dto.response.TvProgressResponse;
import dev.team08.backend.entity.TvProgress;
import dev.team08.backend.entity.User;
import dev.team08.backend.repository.TvProgressRepository;
import dev.team08.backend.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class TvProgressService {
    private final TvProgressRepository tvProgressRepository;
    private final UserRepository userRepository;

    public TvProgressService(TvProgressRepository tvProgressRepository, UserRepository userRepository) {
        this.tvProgressRepository = tvProgressRepository;
        this.userRepository = userRepository;
    }

    public List<TvProgressResponse> listForShow(UUID userId, Integer tmdbTvId) {
        return tvProgressRepository.findByUser_IdAndTmdbTvIdAndWatchedTrue(userId, tmdbTvId).stream()
                .map(p -> new TvProgressResponse(
                        p.getTmdbTvId(), p.getSeasonNumber(), p.getEpisodeNumber(), p.isWatched()))
                .toList();
    }

    @Transactional
    public TvProgressResponse upsert(UUID userId, TvProgressRequest request) {
        if (request.getTmdbTvId() == null || request.getSeasonNumber() < 0 || request.getEpisodeNumber() < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid episode progress");
        }
        TvProgress progress = tvProgressRepository
                .findByUser_IdAndTmdbTvIdAndSeasonNumberAndEpisodeNumber(
                        userId, request.getTmdbTvId(), request.getSeasonNumber(), request.getEpisodeNumber())
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
                    return new TvProgress(
                            user, request.getTmdbTvId(), request.getSeasonNumber(), request.getEpisodeNumber());
                });
        progress.setWatched(request.isWatched());
        TvProgress saved = tvProgressRepository.save(progress);
        return new TvProgressResponse(
                saved.getTmdbTvId(), saved.getSeasonNumber(), saved.getEpisodeNumber(), saved.isWatched());
    }

    @Transactional
    public void markSeasonWatched(UUID userId, Integer tmdbTvId, int seasonNumber, List<Integer> episodeNumbers) {
        if (tmdbTvId == null || seasonNumber < 0 || episodeNumbers == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid season progress");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        for (Integer ep : episodeNumbers) {
            if (ep == null || ep < 1) continue;
            TvProgress progress = tvProgressRepository
                    .findByUser_IdAndTmdbTvIdAndSeasonNumberAndEpisodeNumber(userId, tmdbTvId, seasonNumber, ep)
                    .orElseGet(() -> new TvProgress(user, tmdbTvId, seasonNumber, ep));
            progress.setWatched(true);
            tvProgressRepository.save(progress);
        }
    }
}
