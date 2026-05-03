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
        // Remove the "Bearer " prefix from the token
        token = token.replace("Bearer ", "");

        try {
            // Convert the JSON string to extract the actual username
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(usernameJson);

            // Extract the "username" field from the JSON
            String username = jsonNode.get("username").asText();

            // Check if the token is valid using the extracted username
            return jwtUtility.isTokenValid(token, username);
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract username from JSON", e);
        }
    }

    @Override
    public User getUserFromToken(String token) {
        // Remove the "Bearer " prefix from the token
        token = token.replace("Bearer ", "");

        // Get the username from the token
        String username = jwtUtility.extractUsername(token);

        // Find the user by username
        return userRepository.findByUsername(username);
    }

    @Override
    public User getUserProfileFromToken(String token) {
        // 移除 token 中的 "Bearer " 部分
        token = token.replace("Bearer ", "");

        // 获取用户名
        String username = jwtUtility.extractUsername(token);

        // 获取用户信息，包含 favoriteGenres
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
        User user = userRepository.findByUsername(loginUserRequest.getUsername());

        if (Objects.equals(user.getRole().getName(), "User"))
        {
            // Check if the user exists and the password is correct
            if (PasswordHashingUtility.verifyPassword(loginUserRequest.getPassword(), user.getPassword())) {
                // Generate JSON Web Token
                String token = jwtUtility.generateToken(user.getUsername());

                // Return the AuthResponse object with the token
                return new AuthResponse(token);
            }
        }

        // Return null if the user does not exist or the password is incorrect
        return null;
    }

    @Override
    public AuthResponse loginAdmin(LoginUserRequest loginUserRequest) {
        User user = userRepository.findByUsername(loginUserRequest.getUsername());

        if (Objects.equals(user.getRole().getName(), "Admin"))
        {
            // Check if the user exists and the password is correct
            if (PasswordHashingUtility.verifyPassword(loginUserRequest.getPassword(), user.getPassword())) {
                // Generate JSON Web Token
                String token = jwtUtility.generateToken(user.getUsername());

                // Return the AuthResponse object with the token
                return new AuthResponse(token);
            }
        }

        // Return null if the user does not exist or the password is incorrect
        return null;
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
