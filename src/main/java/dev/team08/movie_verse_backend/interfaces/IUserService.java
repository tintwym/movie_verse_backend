package dev.team08.movie_verse_backend.interfaces;

import dev.team08.movie_verse_backend.dto.request.LoginUserRequest;
import dev.team08.movie_verse_backend.dto.request.RegisterAdminRequest;
import dev.team08.movie_verse_backend.dto.request.RegisterUserRequest;
import dev.team08.movie_verse_backend.dto.request.UserProfileRequest;
import dev.team08.movie_verse_backend.dto.response.AuthResponse;
import dev.team08.movie_verse_backend.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface IUserService {
    boolean verifyToken(String token, String username);
    User getUserFromToken(String token);

    User getUserProfileFromToken(String token);

    User getUserById(UUID id);
    User getUserByUsername(String username);
    AuthResponse registerUser(RegisterUserRequest registerUserRequest);

    boolean registerAdmin(RegisterAdminRequest registerAdminRequest);
    AuthResponse loginUser(LoginUserRequest loginUserRequest);


    AuthResponse loginAdmin(LoginUserRequest loginUserRequest);

    UserProfileRequest getUserProfile(String token);
    boolean updatePassword(UUID userId, String currentPassword, String newPassword);
    //boolean loginAdmin(LoginAdminRequest loginAdminRequest);

    Optional<User> findByUsernameAndEmail(String username, String email);
}