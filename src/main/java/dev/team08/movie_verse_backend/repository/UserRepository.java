package dev.team08.movie_verse_backend.repository;

import dev.team08.movie_verse_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    User findByUsername(String username);

    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.favoriteGenres WHERE u.username = :username")
    Optional<User> findByUsernameWithGenres(String username);

    Optional<User> findByUsernameAndEmail(String username, String email);
}
