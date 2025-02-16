package dev.team08.movie_verse_backend.dto.request;

import java.util.ArrayList;
import java.util.List;

public class RegisterUserRequest {
    private String username;
    private String password;
    private String email;
    private List<String> favoriteGenres;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getFavoriteGenres() {
        return favoriteGenres;
    }

    public void setFavoriteGenres(List<String> favoriteGenres) {
        this.favoriteGenres = favoriteGenres != null ? favoriteGenres : new ArrayList<>();
    }
}
