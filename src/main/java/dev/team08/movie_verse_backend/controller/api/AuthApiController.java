package dev.team08.movie_verse_backend.controller.api;

import dev.team08.movie_verse_backend.dto.request.ForgotPasswordRequest;
import dev.team08.movie_verse_backend.dto.request.LoginUserRequest;
import dev.team08.movie_verse_backend.dto.request.RegisterUserRequest;
import dev.team08.movie_verse_backend.dto.response.AuthResponse;
import dev.team08.movie_verse_backend.entity.User;
import dev.team08.movie_verse_backend.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthApiController {
    private final UserService userService;

    @Autowired
    public AuthApiController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterUserRequest registerUserRequest) {
        AuthResponse authResponse = userService.registerUser(registerUserRequest);
        return ResponseEntity.ok(authResponse);
    }


    @PostMapping("/users/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginUserRequest loginUserRequest) {
        AuthResponse authResponse = userService.loginUser(loginUserRequest);
        if (authResponse == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();  // 使当前会话无效
        return ResponseEntity.ok("token deleted successfully.");
    }

    @PostMapping("/verify-user")
    public ResponseEntity<String> verifyUser(@RequestBody ForgotPasswordRequest request) {
        Optional<User> userOptional = userService.findByUsernameAndEmail(request.getUsername(), request.getEmail());

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(400).body("Invalid username or email");
        }

        return ResponseEntity.ok("User verified. Proceed to reset password.");
    }
}