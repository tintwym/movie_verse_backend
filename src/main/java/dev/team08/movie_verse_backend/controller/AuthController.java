package dev.team08.movie_verse_backend.controller;

import dev.team08.movie_verse_backend.dto.request.LoginUserRequest;
import dev.team08.movie_verse_backend.dto.request.RegisterAdminRequest;
import dev.team08.movie_verse_backend.dto.response.AuthResponse;
import dev.team08.movie_verse_backend.entity.User;
import dev.team08.movie_verse_backend.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final HttpSession session;

    @Autowired
    public AuthController(UserService userService, HttpSession session) {
        this.userService = userService;
        this.session = session;
    }


    @PostMapping("/register")
    public String register(@RequestBody RegisterAdminRequest registerAdminRequest) {

        boolean registerSuccess = userService.registerAdmin(registerAdminRequest);

        if (registerSuccess) {
            // Get the user object by username
            User user = userService.getUserByUsername(registerAdminRequest.getUsername());

            // Add the username to the session
            session.setAttribute("username", user.getUsername());
        }
        return "";
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginUserRequest loginRequest) {
        AuthResponse response = userService.loginAdmin(loginRequest);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        return ResponseEntity.ok(response);
    }
}
