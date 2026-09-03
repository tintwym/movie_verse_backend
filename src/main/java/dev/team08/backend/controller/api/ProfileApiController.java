package dev.team08.backend.controller.api;

import dev.team08.backend.dto.request.UserProfileRequest;
import dev.team08.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class ProfileApiController {

    private final UserService userService;

    public ProfileApiController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile/me")
    public ResponseEntity<UserProfileRequest> getProfile(@RequestHeader("Authorization") String token) {
        try {
            return ResponseEntity.ok(userService.getUserProfile(token));
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired token");
        }
    }

    @PutMapping("/profile/update")
    public ResponseEntity<?> updateUserProfile(
            @RequestHeader("Authorization") String token,
            @RequestBody UserProfileRequest updatedProfile) {

        try {
            boolean success = userService.updateUserProfile(token, updatedProfile);
            if (success) {
                return ResponseEntity.ok(Collections.singletonMap("message", "Profile updated successfully"));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", "Failed to update profile"));
        } catch (ResponseStatusException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired token");
        }
    }

}
