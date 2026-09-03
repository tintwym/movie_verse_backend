package dev.team08.backend.controller.api;

import dev.team08.backend.dto.request.FollowCreditCheckRequest;
import dev.team08.backend.dto.request.FollowPersonRequest;
import dev.team08.backend.dto.response.FollowedPersonResponse;
import dev.team08.backend.interfaces.IUserService;
import dev.team08.backend.service.FollowService;
import dev.team08.backend.utility.AuthHelper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/follows")
public class FollowApiController {
    private final FollowService followService;
    private final IUserService userService;

    public FollowApiController(FollowService followService, IUserService userService) {
        this.followService = followService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<FollowedPersonResponse>> list(@RequestHeader("Authorization") String auth) {
        UUID userId = AuthHelper.requireUserId(userService, auth);
        return ResponseEntity.ok(followService.list(userId));
    }

    @GetMapping("/{personId}/status")
    public ResponseEntity<Map<String, Boolean>> status(
            @RequestHeader("Authorization") String auth,
            @PathVariable Integer personId) {
        UUID userId = AuthHelper.requireUserId(userService, auth);
        return ResponseEntity.ok(Map.of("following", followService.isFollowing(userId, personId)));
    }

    @PostMapping
    public ResponseEntity<FollowedPersonResponse> follow(
            @RequestHeader("Authorization") String auth,
            @RequestBody FollowPersonRequest request) {
        UUID userId = AuthHelper.requireUserId(userService, auth);
        return ResponseEntity.ok(followService.follow(userId, request));
    }

    @DeleteMapping("/{personId}")
    public ResponseEntity<Void> unfollow(
            @RequestHeader("Authorization") String auth,
            @PathVariable Integer personId) {
        UUID userId = AuthHelper.requireUserId(userService, auth);
        followService.unfollow(userId, personId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/check-credits")
    public ResponseEntity<Map<String, Integer>> checkCredits(
            @RequestHeader("Authorization") String auth,
            @RequestBody List<FollowCreditCheckRequest> credits) {
        UUID userId = AuthHelper.requireUserId(userService, auth);
        return ResponseEntity.ok(Map.of("created", followService.checkCredits(userId, credits)));
    }
}
