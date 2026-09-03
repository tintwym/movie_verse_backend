package dev.team08.backend.controller.api;

import dev.team08.backend.dto.response.NotificationResponse;
import dev.team08.backend.interfaces.IUserService;
import dev.team08.backend.service.NotificationService;
import dev.team08.backend.utility.AuthHelper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
public class NotificationApiController {
    private final NotificationService notificationService;
    private final IUserService userService;

    public NotificationApiController(NotificationService notificationService, IUserService userService) {
        this.notificationService = notificationService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> list(@RequestHeader(value = "Authorization", required = false) String auth) {
        UUID userId = AuthHelper.requireUserId(userService, auth);
        return ResponseEntity.ok(notificationService.listForUser(userId));
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Map<String, Long>> unreadCount(@RequestHeader(value = "Authorization", required = false) String auth) {
        UUID userId = AuthHelper.requireUserId(userService, auth);
        return ResponseEntity.ok(Map.of("count", notificationService.unreadCount(userId)));
    }

    @PostMapping("/{id}/read")
    public ResponseEntity<Void> markRead(
            @RequestHeader(value = "Authorization", required = false) String auth,
            @PathVariable UUID id) {
        UUID userId = AuthHelper.requireUserId(userService, auth);
        notificationService.markRead(userId, id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/read-all")
    public ResponseEntity<Void> markAllRead(@RequestHeader(value = "Authorization", required = false) String auth) {
        UUID userId = AuthHelper.requireUserId(userService, auth);
        notificationService.markAllRead(userId);
        return ResponseEntity.ok().build();
    }
}
