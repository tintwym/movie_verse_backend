package dev.team08.movie_verse_backend.entity.ids;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class UserMovieInteractionId implements Serializable {
    private UUID user;
    private Integer tmdbMovieId;

    // ✅ Default Constructor
    public UserMovieInteractionId() {}

    // ✅ Constructor with Fields
    public UserMovieInteractionId(UUID user, Integer tmdbMovieId) {
        this.user = user;
        this.tmdbMovieId = tmdbMovieId;
    }

    // ✅ Override equals() & hashCode()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserMovieInteractionId that = (UserMovieInteractionId) o;
        return Objects.equals(user, that.user) &&
               Objects.equals(tmdbMovieId, that.tmdbMovieId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, tmdbMovieId);
    }
}
