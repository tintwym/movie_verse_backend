package dev.team08.backend.controller.api;

import dev.team08.backend.dto.request.ChangePasswordRequest;
import dev.team08.backend.dto.request.GenreRequest;
import dev.team08.backend.dto.request.ResetPasswordRequest;
import dev.team08.backend.dto.request.UserProfileRequest;
import dev.team08.backend.service.UserService;
import dev.team08.backend.utility.AuthHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserApiController {
    private final UserService userService;

    public UserApiController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/verify-token")
    public ResponseEntity<?> verifyToken(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody String usernameJson) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization token is missing or invalid");
        }
        boolean isValid = userService.verifyToken(token, usernameJson);
        return isValid ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/token")
    public ResponseEntity<UserProfileRequest> getUserFromToken(
            @RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization token is missing or invalid");
        }
        return ResponseEntity.ok(userService.getUserProfile(token));
    }

    @PostMapping("/setGenres")
    public ResponseEntity<String> setGenres(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody List<GenreRequest> genreRequests) {
        try {
            AuthHelper.requireUserId(userService, token);
            userService.setFavoriteGenres(token, genreRequests);
            return ResponseEntity.ok("Genres updated successfully");
        } catch (ResponseStatusException e) {
            throw e;
        } catch (RuntimeException e) {
            String message = e.getMessage() != null ? e.getMessage() : "Invalid request";
            if (message.toLowerCase().contains("token") || message.toLowerCase().contains("not found or")) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired token");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody ChangePasswordRequest changePasswordRequest) {
        if (changePasswordRequest == null
                || changePasswordRequest.getCurrentPassword() == null
                || changePasswordRequest.getNewPassword() == null) {
            return ResponseEntity.badRequest().body("Current and new password are required");
        }
        UUID userId = AuthHelper.requireUserId(userService, token);
        boolean isUpdated = userService.updatePassword(
                userId,
                changePasswordRequest.getCurrentPassword(),
                changePasswordRequest.getNewPassword());
        if (isUpdated) {
            return ResponseEntity.ok("Password updated successfully");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Current password is incorrect");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        if (request == null) {
            return ResponseEntity.badRequest().body("Invalid or expired reset request.");
        }
        try {
            boolean ok = userService.resetPasswordWithToken(
                    request.getUsername(),
                    request.getEmail(),
                    request.getResetToken(),
                    request.getNewPassword()
            );
            if (!ok) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired reset request.");
            }
            return ResponseEntity.ok("Password reset successfully.");
        } catch (ResponseStatusException e) {
            throw e;
        }
    }
}
