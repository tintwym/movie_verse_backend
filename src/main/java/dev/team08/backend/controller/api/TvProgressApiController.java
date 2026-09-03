package dev.team08.backend.controller.api;

import dev.team08.backend.dto.request.TvProgressRequest;
import dev.team08.backend.dto.response.TvProgressResponse;
import dev.team08.backend.interfaces.IUserService;
import dev.team08.backend.service.TvProgressService;
import dev.team08.backend.utility.AuthHelper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/tv-progress")
public class TvProgressApiController {
    private final TvProgressService tvProgressService;
    private final IUserService userService;

    public TvProgressApiController(TvProgressService tvProgressService, IUserService userService) {
        this.tvProgressService = tvProgressService;
        this.userService = userService;
    }

    @GetMapping("/{tvId}")
    public ResponseEntity<List<TvProgressResponse>> list(
            @RequestHeader(value = "Authorization", required = false) String auth,
            @PathVariable Integer tvId) {
        UUID userId = AuthHelper.requireUserId(userService, auth);
        return ResponseEntity.ok(tvProgressService.listForShow(userId, tvId));
    }

    @PutMapping
    public ResponseEntity<TvProgressResponse> upsert(
            @RequestHeader(value = "Authorization", required = false) String auth,
            @RequestBody TvProgressRequest request) {
        UUID userId = AuthHelper.requireUserId(userService, auth);
        return ResponseEntity.ok(tvProgressService.upsert(userId, request));
    }

    @PostMapping("/{tvId}/season/{seasonNumber}")
    public ResponseEntity<Void> markSeason(
            @RequestHeader(value = "Authorization", required = false) String auth,
            @PathVariable Integer tvId,
            @PathVariable int seasonNumber,
            @RequestBody Map<String, List<Integer>> body) {
        UUID userId = AuthHelper.requireUserId(userService, auth);
        List<Integer> episodes = body != null ? body.get("episodeNumbers") : null;
        tvProgressService.markSeasonWatched(userId, tvId, seasonNumber, episodes);
        return ResponseEntity.ok().build();
    }
}
