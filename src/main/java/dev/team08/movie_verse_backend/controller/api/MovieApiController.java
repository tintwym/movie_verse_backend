package dev.team08.movie_verse_backend.controller.api;

import dev.team08.movie_verse_backend.service.MovieService;
import dev.team08.movie_verse_backend.service.UserMovieInteractionService;
import dev.team08.movie_verse_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/movies")
public class MovieApiController {
    private final MovieService movieService;
	private final UserService userService;
    private final UserMovieInteractionService userMovieInteractionService;

    @Autowired
    public MovieApiController(MovieService movieService,  UserMovieInteractionService userMovieInteractionService, UserService userService) {
        this.userService = userService;
    	this.movieService = movieService;
        this.userMovieInteractionService = userMovieInteractionService;
    }

//    @GetMapping("/index")
//    public ResponseEntity<List<ShowMovieRequest>> showAllMovies() {
//        List<ShowMovieRequest> movies = movieService.getAllMovies().stream()
//                .map(ShowMovieRequest::fromMovie)
//                .collect(Collectors.toList());
//
//        return ResponseEntity.ok(movies);
//    }
//
//    @PostMapping("/add")
//    public ResponseEntity<?> addMovie(@RequestBody MovieRequest request,
//                                      @RequestHeader(value = "Authorization", required = false) String token) {
//        Movie newMovie = movieService.addMovie(request,token); // 调用业务逻辑处理添加电影
//        movieService.save(newMovie);// 保存电影
//        return ResponseEntity.ok("Movie add successfully.");
//    }
//
//    @DeleteMapping("/deleteByTitle/{title}")
//    public ResponseEntity<String> deleteMovieByTitle(@PathVariable String title,
//                                                     @RequestHeader(value="Authorization", required = false) String token) {
//        try {
//            movieService.deleteMovie(title, token);
//            return ResponseEntity.ok("Movie deleted successfully.");
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(404).body("Movie not found with title: " + title);
//        }
//    }
//
//    @GetMapping("/show/{id}")
//    public ResponseEntity<ShowMovieRequest> getMovieById(@RequestHeader("Authorization") String token, @PathVariable UUID id) {
//        User user = userService.getUserFromToken(token);
//    	Optional<Movie> movieOptional = Optional.ofNullable(movieService.getMovieById(id));
//        
//        if (movieOptional.isPresent()) {
//        	movieAnalyticsService.logInteraction(user.getId(), id, FavoriteCategory.VIEW);
//            return ResponseEntity.ok(ShowMovieRequest.fromMovie(movieOptional.get()));
//        }
//        
//        return ResponseEntity.notFound().build();
//    }
//    
////    @GetMapping("/search")
////    public ResponseEntity<List<ShowMovieRequest>> searchMovies(@RequestParam String keyword) {
////        List<ShowMovieRequest> movies = movieService.searchMovies(keyword).stream()
////                .map(ShowMovieRequest::fromMovie)
////                .collect(Collectors.toList());
////        return ResponseEntity.ok(movies);
////    }
//
//    @GetMapping("/all")
//    public ResponseEntity<Page<Movie>> getAllMovies(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size,
//            @RequestParam(defaultValue = "title") String sortBy) {
//
//        //Page<Movie> movies = movieService.getMovies(page, size, sortBy);
//    	Page<Movie> movies = movieService.getMovies(page, size, sortBy);
//        return ResponseEntity.ok(movies);
//    }
//
//    @GetMapping("/alphabetical")
//    public ResponseEntity<List<ShowMovieRequest>> getMoviesAlphabetically() {
//        List<ShowMovieRequest> movies = movieService.getMoviesAlphabetically().stream()
//                .map(ShowMovieRequest::fromMovie)
//                .collect(Collectors.toList());
//        return ResponseEntity.ok(movies);
//    }
//
////    @GetMapping("/popular")
////    public ResponseEntity<List<ShowMovieRequest>> getPopularMovies() {
////        List<ShowMovieRequest> movies = movieService.getPopularMovies().stream()
////                .map(ShowMovieRequest::fromMovie)
////                .collect(Collectors.toList());
////        return ResponseEntity.ok(movies);
////    }
//    
//    @GetMapping("/popular")
//    public ResponseEntity<Map<String, Object>> getPopularMovies() {
//        List<ShowMovieRequest> movies = movieService.getPopularMovies().stream()
//                .map(ShowMovieRequest::fromMovie)
//                .collect(Collectors.toList());
//        Map<String,Object> response = new HashMap<>();
//        response.put("results",movies);
//        return ResponseEntity.ok(response);
//    }
//
//    @GetMapping("/trending")
//    public ResponseEntity<Page<ShowMovieRequest>> getTrendingMovies(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size) {
//
//        Page<ShowMovieRequest> movies = movieService.getMovies(page, size, "trending")
//                .map(ShowMovieRequest::fromMovie);
//
//        return ResponseEntity.ok(movies);
//    }
//    
//    // ✅ Get Top Rated Movies (Paginated)
//    @GetMapping("/top-rated")
//    public ResponseEntity<Page<Movie>> getTopRatedMovies(Pageable pageable) {
//        return ResponseEntity.ok(movieService.getTopRatedMovies(pageable));
//    }
//    
//    // ✅ Get Upcoming Movies (Paginated)
//    @GetMapping("/upcoming")
//    public ResponseEntity<Page<Movie>> getUpcomingMovies(Pageable pageable) {
//        return ResponseEntity.ok(movieService.getUpcomingMovies(pageable));
//    }
//    
//    @GetMapping("/genre/popular")
//    public ResponseEntity<Page<Movie>> getPopularMoviesByGenre(
//            @RequestParam String genre,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size) {
//
//        Page<Movie> movies = movieService.getPopularMoviesByGenre(genre, page, size);
//        return ResponseEntity.ok(movies);
//    }
//    
//    @PostMapping("/favorites/add/{userId}/{movieId}")
//    public ResponseEntity<String> addToFavorites(@PathVariable UUID userId, @PathVariable UUID movieId) {
//        Optional<User> user = Optional.of(userService.getUserById(userId));
//        Optional<Movie> movie = Optional.of(movieService.getMovieById(movieId));
//
//        if (user.isEmpty() || movie.isEmpty()) {
//            return ResponseEntity.status(404).body("User or Movie not found");
//        }
//
//        favoriteService.addMovieToFavorites(user.get(), movie.get());
//        return ResponseEntity.ok("Movie added to favorites.");
//    }
//
//    @PostMapping("/watchlist/add/{userId}/{movieId}")
//    public ResponseEntity<String> addToWatchlist(@PathVariable UUID userId, @PathVariable UUID movieId) {
//        Optional<User> user = Optional.of(userService.getUserById(userId));
//        Optional<Movie> movie = Optional.of(movieService.getMovieById(movieId));
//
//        if (user.isEmpty() || movie.isEmpty()) {
//            return ResponseEntity.status(404).body("User or Movie not found");
//        }
//
//        favoriteService.addMovieToWatchlist(user.get(), movie.get());
//        return ResponseEntity.ok("Movie added to watchlist.");
//    }
//
//    @DeleteMapping("/favorites/remove/{userId}/{movieId}")
//    public ResponseEntity<String> removeFromFavorites(@PathVariable UUID userId, @PathVariable UUID movieId) {
//        favoriteService.removeMovieFromFavorites(userService.getUserById(userId), movieService.getMovieById(movieId));
//        return ResponseEntity.ok("Movie removed from favorites.");
//    }
//    
//    @GetMapping("/{movieId}/like")
//    public ResponseEntity<String> likeMovie(@RequestHeader("Authorization") String token, @PathVariable UUID movieId) {
//    	User user = userService.getUserFromToken(token);
//    	movieAnalyticsService.logInteraction(user.getId(), movieId, FavoriteCategory.LIKE);
//    	return ResponseEntity.ok("Movie added to likes");
//    }
//    
//    @GetMapping("/{movieId}/dislike")
//    public ResponseEntity<String> dislikeMovie(@RequestHeader("Authorization") String token, @PathVariable UUID movieId) {
//    	User user = userService.getUserFromToken(token);
//    	movieAnalyticsService.logInteraction(user.getId(), movieId, FavoriteCategory.DISLIKE);
//    	return ResponseEntity.ok("Movie added to dislikes");
//    }
//    
//    public Movie getMovieById(@PathVariable UUID id) {
//        return movieService.getMovieById(id);
//    }
//
//    @PostMapping( "/import")
//    public ResponseEntity<String> importMovies(@RequestParam("file") MultipartFile file) {
//        if (file.isEmpty()) {
//            return ResponseEntity.badRequest().body("请上传 Excel 文件！");
//        }
//        try {
//            // 将 MultipartFile 保存到临时目录
//            File tempFile = File.createTempFile("movies_", ".xlsx");
//            file.transferTo(tempFile);
//
//            // 调用 MovieService 进行导入
//            movieService.importMoviesFromExcel(tempFile.getAbsolutePath());
//
//            return ResponseEntity.ok("Excel 文件成功导入！");
//        } catch (IOException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(500).body("导入失败：" + e.getMessage());
//        }
//    }
//
//
//    @GetMapping("/search/director")
//    public List<MovieRequest> getMoviesByDirector(@RequestParam String director) {
//        return movieService.getMoviesByDirector(director);
//    }
//
//    // 3️⃣ **按演员搜索**
//    @GetMapping("/search/actor")
//    public List<MovieRequest> getMoviesByActor(@RequestParam String actor) {
//        return movieService.getMoviesByActor(actor);
//    }
//
//    // 4️⃣ **按国家搜索**
//    @GetMapping("/search/country")
//    public List<MovieRequest> getMoviesByCountry(@RequestParam String country) {
//        return movieService.getMoviesByCountry(country);
//    }
//
//    // 5️⃣ **按制片公司搜索**
//    @GetMapping("/search/producer")
//    public List<MovieRequest> getMoviesByProducer(@RequestParam String company) {
//        return movieService.getMoviesByProducer(company);
//    }
//
//    @GetMapping("/search/genre")
//    public List<MovieRequest> getMoviesByGenre(@RequestParam String genre) {
//        return movieService.searchMoviesByGenre(genre);
//    }
//
//    @GetMapping("/top-views")
//    public List<MovieRequest> getMoviesByViews(@RequestParam(defaultValue = "10") int limit) {
//        return movieService.getMoviesByViews(limit);
//    }
//
//    // 2️⃣ **获取按评分排序的电影**
//    @GetMapping("/top-rating")
//    public List<MovieRequest> getMoviesByRating(@RequestParam(defaultValue = "10") int limit) {
//        return movieService.getMoviesByRating(limit);
//    }
//
//    // 6️⃣ **多条件搜索**
//    @GetMapping("/search_multi_tasks")
//    public List<MovieRequest> searchMovies(@RequestParam(required = false) String title,
//                                           @RequestParam(required = false) String genre,
//                                           @RequestParam(required = false) Integer year,
//                                           @RequestParam(required = false) Double minRating,
//                                           @RequestParam(required = false) Double maxRating,
//                                           @RequestParam(required = false) String director,
//                                           @RequestParam(required = false) String actor,
//                                           @RequestParam(required = false) String country,
//                                           @RequestParam(required = false) String company) {
//        return movieService.searchMovies(title, genre, year, minRating, maxRating, director, actor, country, company);
//    }
//
//    @GetMapping("/search")
//    public ResponseEntity<List<MovieRequest>> searchMovies(
//            @RequestParam String title,
//            @RequestHeader(value = "Authorization", required = false) String token) {
//        // 解析 token 获取用户信息
//        User user = null;
//        if (token != null && !token.isBlank()) {
//            try {
//                user = userService.getUserFromToken(token);
//            } catch (Exception e) {
//                user = null; // 无效 token，视为未登录用户
//            }
//        }
//        List<MovieRequest> searchResults = movieService.searchMoviesByTitle(title, user);
//        return ResponseEntity.ok(searchResults);
//    }
//
//    @PutMapping("/updateByTitle/{title}")
//    public ResponseEntity<?> updateMovieByTitle(@PathVariable String title,
//                                                @RequestBody MovieRequest request,
//                                                @RequestHeader(value = "Authorization", required = false) String token) {
//        try {
//            Movie updatedMovie = movieService.updateMovie(title, request, token);
//            return ResponseEntity.ok("Movie updated successfully.");
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(404).body(e.getMessage());
//        }
//    }
//
//    @GetMapping("/recommend")
//    public Object getRecommendedMovies(@RequestHeader(value = "Authorization", required = false) String token) {
//        if (token == null || token.isBlank()) {
//            // 未登录用户，返回浏览量最高的前 10 部电影（不包含 user_types）
////            List<Map<String, Object>> topMovies = movieService.getTopMoviesByViews(10);
//            return movieService.getMoviesByViews(10);
//        }
//
//        // 获取用户
//        User user = userService.getUserProfileFromToken(token);
//        if (user == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
//        }
//
//        // 获取用户的交互数据
//        List<Map<String, Object>> userInteractions = userService.getUserInteractions(token);
//
//        String userTypes = user.getFavoriteGenres().stream()
//                .map(Genre::getName)
//                .collect(Collectors.joining("/")); // 拼接成 "Drama/Action/Science Fiction"
//
//        if (userInteractions.isEmpty()) {
//            // 新用户，返回 user_types + 空电影数据
//            Map<String, Object> newUserResponse = new HashMap<>();
//            newUserResponse.put("user_types", userTypes);
//            newUserResponse.put("movie_name", ""); // 空列表
//            newUserResponse.put("rating", -1);
//            newUserResponse.put("favorite", -1);
//            newUserResponse.put("clicks", -1);
//            newUserResponse.put("watched", -1);
//            newUserResponse.put("planned", -1);
//
//
////            return ResponseEntity.ok(newUserResponse);
//////
//            List<Map<String, Object>> defaultData = List.of(newUserResponse);
//            List<Map<String, Object>> recommendations = movieService.callPythonRecommendApi(defaultData);
//            return ResponseEntity.ok(recommendations);
//        }
//
//        // 在老用户的数据里添加 user_types
//        for (Map<String, Object> movieData : userInteractions) {
//            movieData.put("user_types", userTypes);
//        }
//
////          return ResponseEntity.ok(userInteractions);
//        // 调用 Python 推荐 API 获取推荐结果
//        List<Map<String, Object>> recommendations = movieService.callPythonRecommendApi(userInteractions);
//        return ResponseEntity.ok(recommendations);
//    }
//    
//    @GetMapping("/movies/{movieId}/keywords")
//    public ResponseEntity<Set<Keyword>> getMovieKeywords(@PathVariable UUID movieId) {
//        Optional<Movie> movie = Optional.ofNullable(movieService.getMovieById(movieId));
//        if (movie.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        }
//        return ResponseEntity.ok(movie.get().getKeywords());
//    }
//    
}
