package dev.team08.backend.repository;

import dev.team08.backend.entity.TvProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TvProgressRepository extends JpaRepository<TvProgress, UUID> {
    List<TvProgress> findByUser_IdAndTmdbTvIdAndWatchedTrue(UUID userId, Integer tmdbTvId);
    Optional<TvProgress> findByUser_IdAndTmdbTvIdAndSeasonNumberAndEpisodeNumber(
            UUID userId, Integer tmdbTvId, int seasonNumber, int episodeNumber);
    void deleteByUser_Id(UUID userId);
}
