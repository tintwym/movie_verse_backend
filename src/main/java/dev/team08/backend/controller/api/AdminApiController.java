package dev.team08.backend.controller.api;

import dev.team08.backend.dto.response.AdminReviewResponse;
import dev.team08.backend.dto.response.AdminUserResponse;
import dev.team08.backend.interfaces.IUserService;
import dev.team08.backend.service.AdminService;
import dev.team08.backend.utility.AuthHelper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
public class AdminApiController {
    private final AdminService adminService;
    private final IUserService userService;

    public AdminApiController(AdminService adminService, IUserService userService) {
        this.adminService = adminService;
        this.userService = userService;
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> stats(@RequestHeader(value = "Authorization", required = false) String auth) {
        AuthHelper.requireAdmin(userService, auth);
        return ResponseEntity.ok(adminService.stats());
    }

    @GetMapping("/users")
    public ResponseEntity<List<AdminUserResponse>> users(@RequestHeader(value = "Authorization", required = false) String auth) {
        AuthHelper.requireAdmin(userService, auth);
        return ResponseEntity.ok(adminService.listUsers());
    }

    @GetMapping("/reviews")
    public ResponseEntity<List<AdminReviewResponse>> reviews(@RequestHeader(value = "Authorization", required = false) String auth) {
        AuthHelper.requireAdmin(userService, auth);
        return ResponseEntity.ok(adminService.listReviews());
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(
            @RequestHeader(value = "Authorization", required = false) String auth,
            @PathVariable UUID userId) {
        AuthHelper.requireAdmin(userService, auth);
        adminService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/reviews")
    public ResponseEntity<Void> deleteReview(
            @RequestHeader(value = "Authorization", required = false) String auth,
            @RequestParam UUID userId,
            @RequestParam Integer tmdbMovieId) {
        AuthHelper.requireAdmin(userService, auth);
        adminService.deleteReview(userId, tmdbMovieId);
        return ResponseEntity.ok().build();
    }
}
