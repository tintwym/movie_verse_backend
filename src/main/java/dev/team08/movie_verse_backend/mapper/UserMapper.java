package dev.team08.movie_verse_backend.mapper;

import dev.team08.movie_verse_backend.dto.request.LoginUserRequest;
import dev.team08.movie_verse_backend.dto.request.RegisterAdminRequest;
import dev.team08.movie_verse_backend.dto.request.RegisterUserRequest;
import dev.team08.movie_verse_backend.entity.Genre;
import dev.team08.movie_verse_backend.entity.User;
import dev.team08.movie_verse_backend.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    private final GenreRepository genreRepository;

    @Autowired
    public UserMapper(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public User fromRegisterUserRequest(RegisterUserRequest registerUserRequest) {
        User user = new User();
        user.setUsername(registerUserRequest.getUsername());
        user.setPassword(registerUserRequest.getPassword());
        user.setEmail(registerUserRequest.getEmail());

        List<String> favoriteGenres = registerUserRequest.getFavoriteGenres() != null ?
                registerUserRequest.getFavoriteGenres() : new ArrayList<>();

        List<Genre> genres = favoriteGenres.stream()
                .map(genreName -> genreRepository.findByName(genreName))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        user.setFavoriteGenres(genres);
        return user;
    }

    public User fromRegisterAdminRequest(RegisterAdminRequest registerAdminRequest) {
        User user = new User();
        user.setUsername(registerAdminRequest.getUsername());
        user.setPassword(registerAdminRequest.getPassword());
        user.setEmail(registerAdminRequest.getEmail());
        return user;
    }

    public User fromLoginUserRequest(LoginUserRequest loginUserRequest) {
        User user = new User();
        user.setUsername(loginUserRequest.getUsername());
        user.setPassword(loginUserRequest.getPassword());
        return user;
    }
}