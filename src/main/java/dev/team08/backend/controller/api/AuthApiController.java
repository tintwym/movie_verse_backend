package dev.team08.backend.controller.api;

import dev.team08.backend.dto.request.ForgotPasswordRequest;
import dev.team08.backend.dto.request.LoginUserRequest;
import dev.team08.backend.dto.request.RegisterUserRequest;
import dev.team08.backend.dto.request.ResetPasswordRequest;
import dev.team08.backend.dto.response.AuthResponse;
import dev.team08.backend.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthApiController {
    private final UserService userService;

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
        session.invalidate();
        return ResponseEntity.ok(Map.of(
                "message", "Logged out. Discard the client JWT; server sessions do not revoke tokens."
        ));
    }

    /**
     * Starts password reset. Always returns the same message to avoid account enumeration.
     * When the user exists, a one-time resetToken is included (demo without email delivery).
     */
    @PostMapping("/verify-user")
    public ResponseEntity<Map<String, String>> verifyUser(@RequestBody ForgotPasswordRequest request) {
        String token = userService.issuePasswordResetToken(request.getUsername(), request.getEmail());
        if (token == null) {
            return ResponseEntity.ok(Map.of(
                    "message", "If that account exists, a reset token was issued.",
                    "resetToken", ""
            ));
        }
        return ResponseEntity.ok(Map.of(
                "message", "If that account exists, a reset token was issued.",
                "resetToken", token
        ));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPasswordAuth(@RequestBody ResetPasswordRequest request) {
        boolean ok = userService.resetPasswordWithToken(
                request.getUsername(),
                request.getEmail(),
                request.getResetToken(),
                request.getNewPassword()
        );
        if (!ok) {
            return ResponseEntity.status(400).body("Invalid or expired reset request.");
        }
        return ResponseEntity.ok("Password reset successfully.");
    }
}
