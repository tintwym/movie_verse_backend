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
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final MovieRatingRepository ratingRepository;
    private final RoleRepository roleRepository;
    private final GenreRepository genreRepository;
    private final MovieRepository movieRepository;
    private final JwtUtility jwtUtility;
    private final RestTemplate restTemplate;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserMapper userMapper, UserRepository userRepository, MovieRatingRepository ratingRepository, RoleRepository roleRepository, GenreRepository genreRepository, MovieRepository movieRepository, JwtUtility jwtUtility, RestTemplate restTemplate) {
        this.userRepository = userRepository;
        this.ratingRepository = ratingRepository;
        this.roleRepository = roleRepository;
        this.genreRepository = genreRepository;
        this.movieRepository = movieRepository;
        this.jwtUtility = jwtUtility;
        this.restTemplate = restTemplate;
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
            System.out.println("Created role: " + roleName);
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
            System.out.println("Created genre: " + genreName);
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
//        String username = jwtUtility.extractUsername(token);
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

    /**
     * 获取用户交互数据，以 DataFrame 结构返回（列表 Map 结构）
     */
//    public List<Map<String, Object>> getUserInteractions(String token) {
//        User user = getUserProfileFromToken(token);
//
//        if (user == null) {
//            throw new RuntimeException("User not found or token is invalid.");
//        }
//
//        List<Map<String, Object>> tableData = new ArrayList<>();
//
//        List<MovieRating> ratings = ratingRepository.findByUserId(user.getId());
//        for (MovieRating rating : ratings) {
//            Map<String, Object> row = new HashMap<>();
//            row.put("user_id", rating.getUser().getId().toString());
//            row.put("username", rating.getUser().getUsername());
//            row.put("movie_name", rating.getMovie().getTitle());
//            row.put("rating", Optional.ofNullable(rating.getRating()).orElse(-1.0));
//
//            // 统计喜欢次数（LIKE + FAVORITE）
//            int favoriteCount = favoriteRepository.countByUserAndMovieAndCategory(rating.getUser(), rating.getMovie(), FavoriteCategory.LIKE)
//                    + favoriteRepository.countByUserAndMovieAndCategory(rating.getUser(), rating.getMovie(), FavoriteCategory.FAVORITE);
//            row.put("favorite", favoriteCount);
//
//
////            row.put("search", 1);
//            row.put("clicks", 1);
//            int watchedCount = favoriteRepository.countByUserAndMovieAndCategory(rating.getUser(), rating.getMovie(), FavoriteCategory.VIEW);
//            row.put("watched", watchedCount);
//
//
//            // 计划观看次数（LIKE_BUT_UNWATCHED）
//            int plannedCount = favoriteRepository.countByUserAndMovieAndCategory(rating.getUser(), rating.getMovie(), FavoriteCategory.LIKE_BUT_UNWATCHED);
//            row.put("planned", plannedCount);
//            tableData.add(row);
//        }
//
//        List<Favorite> favorites = favoriteRepository.findByUser(user);
//        for (Favorite favorite : favorites) {
//            Movie movie = favorite.getMovie();
//
//            // 检查这个电影是否已经在评分列表中
//            boolean alreadyExists = tableData.stream()
//                    .anyMatch(row -> row.get("movie_name").equals(movie.getTitle()));
//
//            if (!alreadyExists) {
//                Map<String, Object> row = new HashMap<>();
//                row.put("user_id", user.getId().toString());
//                row.put("username", user.getUsername());
//                row.put("movie_name", movie.getTitle());
//                row.put("rating", -1.0); // 没有评分
//
//                // 统计喜欢次数
//                int favoriteCount = favoriteRepository.countByUserAndMovieAndCategory(user, movie, FavoriteCategory.LIKE)
//                        + favoriteRepository.countByUserAndMovieAndCategory(user, movie, FavoriteCategory.FAVORITE);
//                row.put("favorite", favoriteCount);
//
//                // 统计观看次数
//                int watchedCount = favoriteRepository.countByUserAndMovieAndCategory(user, movie, FavoriteCategory.VIEW);
//                row.put("watched", watchedCount);
//                row.put("clicks", 1);
////                row.put("search", 1);
//                row.put("planned", 0);
//
//                tableData.add(row);
//            }
//        }
//        return tableData;
//    }

//    public List<Map<String, Object>> getRecommendedMovies(String token) {
//        List<Map<String, Object>> userInteractions = getUserInteractions(token);
//
//        // 配置 HTTP 请求
//        String pythonApiUrl = "http://127.0.0.1:5000/recommend";  // Python 服务器地址
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<List<Map<String, Object>>> request = new HttpEntity<>(userInteractions, headers);
//
//        // 发送请求到 Python 服务器
//        ResponseEntity<Map> response = restTemplate.exchange(pythonApiUrl, HttpMethod.POST, request, Map.class);
//
//        // 解析 Python 返回的数据
//        if (response.getStatusCode() == HttpStatus.OK) {
//            return (List<Map<String, Object>>) response.getBody().get("recommendations");
//        } else {
//            throw new RuntimeException("Failed to fetch recommendations from Python API");
//        }
//    }

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
