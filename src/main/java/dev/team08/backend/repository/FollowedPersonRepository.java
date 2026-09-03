package dev.team08.backend.repository;

import dev.team08.backend.entity.FollowedPerson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FollowedPersonRepository extends JpaRepository<FollowedPerson, UUID> {
    List<FollowedPerson> findByUser_IdOrderByCreatedAtDesc(UUID userId);
    Optional<FollowedPerson> findByUser_IdAndTmdbPersonId(UUID userId, Integer tmdbPersonId);
    boolean existsByUser_IdAndTmdbPersonId(UUID userId, Integer tmdbPersonId);
    void deleteByUser_IdAndTmdbPersonId(UUID userId, Integer tmdbPersonId);
    void deleteByUser_Id(UUID userId);
}
