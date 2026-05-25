package dev.team08.movie_verse_backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.team08.movie_verse_backend.dto.request.*;
import dev.team08.movie_verse_backend.dto.response.AuthResponse;
import dev.team08.movie_verse_backend.entity.*;
import dev.team08.movie_verse_backend.interfaces.IUserService;
import dev.team08.movie_verse_backend.mapper.UserMapper;
import dev.team08.movie_verse_backend.repository.*;
import dev.team08.movie_verse_backend.utility.JwtUtility;
import dev.team08.movie_verse_backend.utility.PasswordHashingUtility;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Autowired
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
        // Hash the password before saving it to the database

        registerUserRequest.setPassword(PasswordHashingUtility.hashPassword(registerUserRequest.getPassword()));

        // Map the RegisterUserRequest to a User entity
        User user = userMapper.fromRegisterUserRequest(registerUserRequest);

        // Fetch the role from the role service
        Role role = roleRepository.findByName("User");
        user.setRole(role);

        // Save the user to the database
        userRepository.save(user);

        // Generate JSON Web Token
        String token = jwtUtility.generateToken(user.getUsername());

        // Return the AuthResponse object with the token
        return new AuthResponse(token);
    }
    
    public void setFavoriteGenres(String token, List<GenreRequest> genreRequests) {
        User user = getUserProfileFromToken(token);

        if (user == null) {
            throw new RuntimeException("User not found or token is invalid.");
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
        return loginWithRole(loginUserRequest, "User");
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
        return new AuthResponse(token);
    }

    @Override
    public UserProfileRequest getUserProfile(String token){
        User user = getUserProfileFromToken(token);

        if (user == null) {
            throw new RuntimeException("User not found or token is invalid.");
        }

        List<GenreRequest> genres = user.getFavoriteGenres().stream()
                .map(genre -> new GenreRequest(genre.getId(), genre.getName()))  // 只返回需要的字段
                .distinct()  // 去重，确保没有重复的genres
                .collect(Collectors.toList());

        return new UserProfileRequest(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getAvatar(),
                genres
        );
    }

    @Transactional
    public boolean updateUserProfile(String token, UserProfileRequest updatedProfile) {
        String username = jwtUtility.extractUsername(token.replace("Bearer ", ""));
        User user = userRepository.findByUsernameWithGenres(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 更新用户名
        if (updatedProfile.getUsername() != null && !updatedProfile.getUsername().isEmpty()) {
            user.setUsername(updatedProfile.getUsername());
        }

        // 更新邮箱
        if (updatedProfile.getEmail() != null && !updatedProfile.getEmail().isEmpty()) {
            user.setEmail(updatedProfile.getEmail());
        }

        // 更新密码（需要加密）
        if (updatedProfile.getPassword() != null && !updatedProfile.getPassword().isEmpty()) {
            user.setPassword(PasswordHashingUtility.hashPassword(updatedProfile.getPassword()));
        }

        if (updatedProfile.getAvatar() != null && !updatedProfile.getAvatar().isEmpty()) {
            user.setAvatar(updatedProfile.getAvatar());
        }

        // 更新喜欢的电影类型
        if (updatedProfile.getFavouriteGenres() != null && !updatedProfile.getFavouriteGenres().isEmpty()) {
            List<String> genreNames = updatedProfile.getFavouriteGenres().stream()
                    .map(GenreRequest::getName) // 提取名称
                    .toList();

            List<Genre> genres = genreRepository.findByNameIn(genreNames);
            user.setFavoriteGenres(genres);
        }

        userRepository.save(user);
        return true;
    }

    public boolean updatePassword(UUID userId, String currentPassword, String newPassword) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            // Check if current password matches the stored one
            if (PasswordHashingUtility.verifyPassword(currentPassword, user.getPassword())) {
                // Update the password if current password is correct
                user.setPassword(newPassword);
                userRepository.save(user);
                return true;
            }
        }

        // If user not found or current password doesn't match
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
