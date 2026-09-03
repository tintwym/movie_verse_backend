package dev.team08.backend.utility;

import dev.team08.backend.entity.User;
import dev.team08.backend.interfaces.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

public final class AuthHelper {

    private AuthHelper() {}

    public static UUID requireUserId(IUserService userService, String authorizationHeader) {
        User user = userService.getUserFromToken(authorizationHeader);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired token");
        }
        return user.getId();
    }
}
