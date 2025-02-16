package dev.team08.movie_verse_backend.dto.request;

import java.util.List;
import java.util.UUID;

public class UserProfileRequest {
    private UUID id;
    private String username;
    private String email;
    private String password;
    private String avatar;
    private List<GenreRequest> FavouriteGenres;

    public UserProfileRequest(UUID id, String username, String email, String password, String avatar, List<GenreRequest> FavouriteGenres) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.avatar = avatar;
        this.FavouriteGenres = FavouriteGenres;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<GenreRequest> getFavouriteGenres() {
        return FavouriteGenres;
    }

    public void setFavouriteGenres(List<GenreRequest> favouriteGenres) {
        FavouriteGenres = favouriteGenres;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

}
