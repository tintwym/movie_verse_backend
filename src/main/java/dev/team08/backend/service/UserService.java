package dev.team08.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.team08.backend.dto.request.*;
import dev.team08.backend.dto.response.AuthResponse;
import dev.team08.backend.entity.*;
import dev.team08.backend.interfaces.IUserService;
import dev.team08.backend.mapper.UserMapper;
import dev.team08.backend.repository.*;
import dev.team08.backend.utility.JwtUtility;
import dev.team08.backend.utility.PasswordHashingUtility;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final GenreRepository genreRepository;
    private final JwtUtility jwtUtility;
    private final UserMapper userMapper;

    public UserService(UserMapper userMapper, UserRepository userRepository, RoleRepository roleRepository, GenreRepository genreRepository, JwtUtility jwtUtility) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.genreRepository = genreRepository;
        this.jwtUtility = jwtUtility;
        this.userMapper = userMapper;
    }

    @PostConstruct
    public void initRoles() {
        createRoleIfNotExists("User");
        createRoleIfNotExists("Admin");
        seedDefaultAdmin();
    }

    private void seedDefaultAdmin() {
        if (userRepository.findByUsername("admin") != null) {
            return;
        }
        Role adminRole = roleRepository.findByName("Admin");
        if (adminRole == null) {
            return;
        }
        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@movieverse.local");
        admin.setPassword(PasswordHashingUtility.hashPassword("admin123"));
        admin.setRole(adminRole);
        userRepository.save(admin);
    }

    private void createRoleIfNotExists(String roleName) {
        if (roleRepository.findByName(roleName) == null) {
            Role role = new Role();
            role.setName(roleName);
            roleRepository.save(role);
        }
    }

    @PostConstruct
    public void initGenres() {
        List<String> genres = List.of("Action", "Comedy", "Drama", "Horror",
                "Science Fiction", "Romance", "Mystery", "Crime", "Documentary",
                "Animation", "Fantasy", "Thriller", "Adventure");
        genres.forEach(this::createGenreIfNotExists);
    }

    private void createGenreIfNotExists(String genreName) {
        if (genreRepository.findByName(genreName).isEmpty()) {
            Genre genre = new Genre();
            genre.setName(genreName);
            genreRepository.save(genre);
        }
    }

    @Override
    public boolean verifyToken(String token, String usernameJson) {
        // An expired / malformed token, or bad JSON, is simply "not valid".
        // Previously this method wrapped every exception in a RuntimeException,
        // which Spring then turned into a 500 Internal Server Error for the
        // client. The correct behaviour is to return `false` so the controller
        // can respond with a clean 401 Unauthorized.
        if (token == null) {
            return false;
        }
        token = token.replace("Bearer ", "");

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(usernameJson);
            JsonNode usernameNode = jsonNode.get("username");
            if (usernameNode == null) {
                return false;
            }
            String username = usernameNode.asText();
            return jwtUtility.isTokenValid(token, username);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public User getUserFromToken(String token) {
        if (token == null) {
            return null;
        }
        token = token.replace("Bearer ", "");

        String username = jwtUtility.extractUsernameSafe(token);
        if (username == null) {
            return null;
        }
        return userRepository.findByUsername(username);
    }

    @Override
    public User getUserProfileFromToken(String token) {
        if (token == null) {
            return null;
        }
        token = token.replace("Bearer ", "");

        String username = jwtUtility.extractUsernameSafe(token);
        if (username == null) {
            return null;
        }
        return userRepository.findByUsernameWithGenres(username).orElse(null);
    }
    @Override
    public User getUserById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public AuthResponse registerUser(RegisterUserRequest registerUserRequest) {
        if (registerUserRequest.getUsername() == null || registerUserRequest.getUsername().isBlank()
                || registerUserRequest.getPassword() == null || registerUserRequest.getPassword().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username and password are required");
        }
        if (userRepository.findByUsername(registerUserRequest.getUsername()) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }

        registerUserRequest.setPassword(PasswordHashingUtility.hashPassword(registerUserRequest.getPassword()));

        User user = userMapper.fromRegisterUserRequest(registerUserRequest);

        Role role = roleRepository.findByName("User");
        user.setRole(role);

        userRepository.save(user);

        String token = jwtUtility.generateToken(user.getUsername());
        return new AuthResponse(token);
    }
    
    public void setFavoriteGenres(String token, List<GenreRequest> genreRequests) {
        User user = getUserProfileFromToken(token);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired token");
        }

        // 获取 genreRequests 对应的 Genre 对象列表
        List<String> genreNames = genreRequests.stream()
                .map(GenreRequest::getName)
                .toList();

        List<Genre> genres = genreRepository.findByNameIn(genreNames);
        if (genres.isEmpty()) {
            throw new RuntimeException("No valid genres found");
        }

        // 更新用户的 favoriteGenres
        user.setFavoriteGenres(genres);
        userRepository.save(user);
    }

    @Override
    public boolean registerAdmin(RegisterAdminRequest registerAdminRequest) {
        // Hash the password before saving it to the database
        registerAdminRequest.setPassword(PasswordHashingUtility.hashPassword(registerAdminRequest.getPassword()));

        // Map the RegisterUserRequest to a User entity
        User user = userMapper.fromRegisterAdminRequest(registerAdminRequest);

        // Fetch the role from the role service
        Role role = roleRepository.findByName("Admin");
        user.setRole(role);

        // Save the user to the database
        userRepository.save(user);

        return true;
    }

    @Override
    public AuthResponse loginUser(LoginUserRequest loginUserRequest) {
        // Allow both User and Admin through the same login endpoint.
        return loginAnyRole(loginUserRequest);
    }

    @Override
    public AuthResponse loginAdmin(LoginUserRequest loginUserRequest) {
        return loginWithRole(loginUserRequest, "Admin");
    }

    // Shared login flow. Previous versions dereferenced `user.getRole().getName()`
    // unconditionally, which threw a NullPointerException — and a 500 response —
    // whenever someone tried to log in with a username that didn't exist.
    private AuthResponse loginWithRole(LoginUserRequest request, String expectedRole) {
        User user = userRepository.findByUsername(request.getUsername());
        if (user == null || user.getRole() == null) {
            return null;
        }
        if (!Objects.equals(user.getRole().getName(), expectedRole)) {
            return null;
        }
        if (!PasswordHashingUtility.verifyPassword(request.getPassword(), user.getPassword())) {
            return null;
        }
        String token = jwtUtility.generateToken(user.getUsername());
        return new AuthResponse(token, user.getRole().getName());
    }

    private AuthResponse loginAnyRole(LoginUserRequest request) {
        User user = userRepository.findByUsername(request.getUsername());
        if (user == null || user.getRole() == null) {
            return null;
        }
        if (!PasswordHashingUtility.verifyPassword(request.getPassword(), user.getPassword())) {
            return null;
        }
        String token = jwtUtility.generateToken(user.getUsername());
        return new AuthResponse(token, user.getRole().getName());
    }

    @Override
    public UserProfileRequest getUserProfile(String token){
        User user = getUserProfileFromToken(token);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired token");
        }

        List<GenreRequest> genres = user.getFavoriteGenres() == null
                ? List.of()
                : user.getFavoriteGenres().stream()
                .map(genre -> new GenreRequest(genre.getId(), genre.getName()))
                .distinct()
                .collect(Collectors.toList());

        String roleName = user.getRole() != null ? user.getRole().getName() : "User";
        return new UserProfileRequest(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                null,
                user.getAvatar(),
                roleName,
                genres
        );
    }

    @Transactional
    public boolean updateUserProfile(String token, UserProfileRequest updatedProfile) {
        User user = getUserProfileFromToken(token);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired token");
        }

        // Username changes would invalidate the existing JWT subject — skip them.
        if (updatedProfile.getEmail() != null && !updatedProfile.getEmail().isEmpty()) {
            user.setEmail(updatedProfile.getEmail());
        }

        // Password changes belong on /change-password (requires current password).
        if (updatedProfile.getAvatar() != null && !updatedProfile.getAvatar().isEmpty()) {
            user.setAvatar(updatedProfile.getAvatar());
        }

        if (updatedProfile.getFavouriteGenres() != null && !updatedProfile.getFavouriteGenres().isEmpty()) {
            List<String> genreNames = updatedProfile.getFavouriteGenres().stream()
                    .map(GenreRequest::getName)
                    .toList();

            List<Genre> genres = genreRepository.findByNameIn(genreNames);
            user.setFavoriteGenres(genres);
        }

        userRepository.save(user);
        return true;
    }

    public boolean updatePassword(UUID userId, String currentPassword, String newPassword) {
        if (currentPassword == null || newPassword == null || newPassword.isBlank()) {
            return false;
        }
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (PasswordHashingUtility.verifyPassword(currentPassword, user.getPassword())) {
                user.setPassword(PasswordHashingUtility.hashPassword(newPassword));
                userRepository.save(user);
                return true;
            }
        }

        return false;
    }

    public Optional<User> findByUsernameAndEmail(String username, String email) {
        // Query the database to find a user with matching username and email
        return userRepository.findByUsernameAndEmail(username, email);
    }

    public boolean resetPassword(UUID userId, String newPassword) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setPassword(PasswordHashingUtility.hashPassword(newPassword));
            userRepository.save(user);
            return true;
        }
        return false;
    }

}
