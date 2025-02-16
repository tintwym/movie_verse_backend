package dev.team08.movie_verse_backend.controller.api;

import dev.team08.movie_verse_backend.dto.request.UserProfileRequest;
import dev.team08.movie_verse_backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class ProfileApiController {

    private final UserService userService;

    public ProfileApiController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile/me")
    public UserProfileRequest getProfile(@RequestHeader("Authorization") String token) {
        return userService.getUserProfile(token);
    }

    @PutMapping("/profile/update")
    public ResponseEntity<?> updateUserProfile(
            @RequestHeader("Authorization") String token,
            @RequestBody UserProfileRequest updatedProfile) {

        boolean success  = userService.updateUserProfile(token, updatedProfile);

        if (success) {
            return ResponseEntity.ok(Collections.singletonMap("message", "Profile updated successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", "Failed to update profile"));
        }
    }

}
