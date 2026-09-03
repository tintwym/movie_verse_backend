package dev.team08.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class JwtSecretValidator implements ApplicationRunner {

    private static final String WEAK_DEFAULT = "change-me-to-a-long-random-secret-key";

    @Value("${jwt.secret}")
    private String jwtSecret;

    private final Environment environment;

    public JwtSecretValidator(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void run(ApplicationArguments args) {
        boolean localProfile = java.util.Arrays.stream(environment.getActiveProfiles())
                .anyMatch(p -> p.equalsIgnoreCase("local"));
        if (localProfile) {
            return;
        }
        if (jwtSecret == null || jwtSecret.isBlank()
                || jwtSecret.startsWith(WEAK_DEFAULT)
                || jwtSecret.length() < 32) {
            throw new IllegalStateException(
                    "JWT_SECRET must be set to a strong secret (32+ chars) outside the local profile. "
                            + "Refusing to start with a weak/default secret.");
        }
    }
}
