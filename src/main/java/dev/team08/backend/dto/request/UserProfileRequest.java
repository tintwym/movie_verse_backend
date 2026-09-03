package dev.team08.backend.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

public class UserProfileRequest {
    private UUID id;
    private String username;
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String avatar;
    private String role;
    private List<GenreRequest> FavouriteGenres;

    public UserProfileRequest(UUID id, String username, String email, String password, String avatar, List<GenreRequest> FavouriteGenres) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.avatar = avatar;
        this.FavouriteGenres = FavouriteGenres;
    }

    public UserProfileRequest(UUID id, String username, String email, String password, String avatar, String role, List<GenreRequest> FavouriteGenres) {
        this(id, username, email, password, avatar, FavouriteGenres);
        this.role = role;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
