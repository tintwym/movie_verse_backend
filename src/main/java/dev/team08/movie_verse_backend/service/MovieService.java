package dev.team08.movie_verse_backend.service;

import dev.team08.movie_verse_backend.interfaces.IMovieService;
import dev.team08.movie_verse_backend.mapper.MovieMapper;
import dev.team08.movie_verse_backend.repository.*;
import dev.team08.movie_verse_backend.utility.JwtUtility;
import org.springframework.stereotype.Service;

@Service
public class MovieService implements IMovieService {
    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final MovieRatingRepository ratingRepository;
    private final UserRepository userRepository;
    private final MovieMapper movieMapper;
    private final JwtUtility jwtUtility;
    private final UserService userService;

    public MovieService(MovieRepository movieRepository, GenreRepository genreRepository,  MovieRatingRepository ratingRepository, UserRepository userRepository, MovieMapper movieMapper, JwtUtility jwtUtility, UserService userService) {
        this.movieRepository = movieRepository;
        this.genreRepository = genreRepository;
        this.ratingRepository = ratingRepository;
        this.userRepository = userRepository;
        this.movieMapper = movieMapper;
        this.jwtUtility = jwtUtility;
        this.userService = userService;
    }

//    @Override
//    public List<Movie> getAllMovies() {
//        return movieRepository.findByIsDeletedFalse();
//    }
//    
//    public Page<Movie> getMovies(int page, int size, String sortBy) {
//    	Pageable pageable = PageRequest.of(page,  size, Sort.by(sortBy).descending());
//    	
//    	if (sortBy.equalsIgnoreCase("popular")) {
//            pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("views")));
//        } else if (sortBy.equalsIgnoreCase("trending")) {
//        	return getTrendingMovies(pageable);
//        } else if (sortBy.equalsIgnoreCase("rating")) {
//            pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("rating")));
//        } else if (sortBy.equalsIgnoreCase("alphabetical")) {
//            pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("title")));
//        }
//
//        return movieRepository.findAll(pageable);
//    }
//    
//    private Page<Movie> getTrendingMovies(Pageable pageable) {
//        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
//        List<UUID> trendingMovieIds = movieAnalyticsRepository.findTrendingMovies(sevenDaysAgo,pageable);
//
//        if (trendingMovieIds.isEmpty()) {
//            // Fallback to popular movies (sorted by views)
//            return movieRepository.findAll(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Order.desc("views"))));
//        }
//        return movieRepository.findMoviesByIdList(trendingMovieIds, pageable);
//    }
//    
//    public Page<Movie> getPopularMoviesByGenre(String genre, int page, int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        return movieRepository.findPopularMoviesByGenre(genre, pageable);
//    }
//    
//    @Override
//    public Page<Movie> getTopRatedMovies(Pageable pageable) {
//        return movieRepository.findByOrderByRatingDesc(pageable);
//    }
//    
//    @Override
//    public Page<Movie> getUpcomingMovies(Pageable pageable) {
//        int currentYear = Year.now().getValue(); // Get current year
//        return movieRepository.findByReleaseYearGreaterThanEqualOrderByReleaseYearAsc(currentYear, pageable);
//    }
//
//    @Transactional
//    @Override
//    public Movie addMovie(MovieRequest request, String token) {
//        User user = userService.getUserProfileFromToken(token);
//
//        if (user == null) {
//            throw new RuntimeException("User not found or token is invalid.");
//        }
//        Movie movie = movieMapper.fromMovieRequest(request);
//
//        movie = movieRepository.save(movie);
//
//        // 处理电影分类 (Genres)
//        List<Genre> genres = request.getGenreNames().stream()
//                .map(name -> genreRepository.findByName(name).orElseGet(() -> {
//                    Genre newGenre = new Genre();
//                    newGenre.setName(name);
//                    return genreRepository.save(newGenre);
//                }))
//                .collect(Collectors.toList());
//        movie.setGenres(genres);
//
//        // 处理演员 (Actors)
//        List<Actor> actors = request.getActorNames().stream()
//                .map(name -> actorRepository.findByFirstNameAndLastName(name.getFirstName(), name.getLastName()).orElseGet(() -> {
//                    Actor newActor = new Actor();
//                    newActor.setFirstName(name.getFirstName());
//                    newActor.setLastName(name.getLastName());
//                    return actorRepository.save(newActor);
//                }))
//                .collect(Collectors.toList());
//        movie.setActors(actors);
//
//        // 处理导演 (Directors)
//        List<Director> directors = request.getDirectorNames().stream()
//                .map(name -> directorRepository.findByFirstNameAndLastName(name.getFirstName(), name.getLastName()).orElseGet(() -> {
//                    Director newDirector = new Director();
//                    newDirector.setFirstName(name.getFirstName());
//                    newDirector.setLastName(name.getLastName());
//                    return directorRepository.save(newDirector);
//                }))
//                .collect(Collectors.toList());
//        movie.setDirectors(directors);
//
//        // 处理制片人 (Producers)
//        List<Producer> producers = request.getProducerNames().stream()
//                .map(name -> producerRepository.findByCompanyName(name).orElseGet(() -> {
//                    Producer newProducer = new Producer();
//                    newProducer.setCompanyName(name);
//                    return producerRepository.save(newProducer);
//                }))
//                .collect(Collectors.toList());
//        movie.setProducers(producers);
//
//        User defaultUser = userRepository.findByUsername("default_user");
//        if (defaultUser == null) {
//            throw new RuntimeException("Default user not found in database.");
//        }
//
//        // 处理评分
//        List<MovieRating> ratings;
//        if (request.getRatings() == null || request.getRatings().isEmpty()) {
//            // 如果没有评分数据，使用默认评分
//            MovieRating defaultRating = new MovieRating();
//            defaultRating.setMovie(movie);
//            defaultRating.setUser(defaultUser);
//            defaultRating.setRating(0);
//            ratings = List.of(defaultRating);
//        } else {
//            // 处理请求中的评分数据
//            Movie finalMovie = movie;
//            ratings = request.getRatings().stream()
//                    .map(r -> {
//                        MovieRating rating = new MovieRating();
//                        rating.setRating(r);
//                        rating.setMovie(finalMovie);
//                        rating.setUser(defaultUser);
//                        return rating;
//                    })
//                    .collect(Collectors.toList());
//        }
//
//        // 先保存评分数据
//        ratingRepository.saveAll(ratings);
//        movie.setRatings(ratings);
//
//        // 计算平均评分
//        updateMovieAverageRating(movie);
//
//        return movieRepository.save(movie);
//    }
//
//    @Transactional
//    public void updateMovieAverageRating(Movie movie) {
//        // 计算当前电影所有评分的平均分
//        Double averageRating = ratingRepository.calculateAverageRatingByMovieId(movie.getId());
//        if (averageRating == null) {
//            averageRating = 0.0; // 避免 NullPointerException
//        }
//        movie.setRating(averageRating);
//        movieRepository.save(movie);
//    }
//
//    @Transactional
//    @Override
//    public Movie getMovieWithRatings(UUID movieId) {
//        return movieRepository.findById(movieId)
//                .map(movie -> {
//                    Hibernate.initialize(movie.getRatings()); // 强制加载 ratings
//                    return movie;
//                })
//                .orElse(null);
//    }
//
//    @Override
//    public void save(Movie movie){
//        movieMapper.saveMovie(movie);
//    }
//
//    @Transactional
//    @Override
//    public Movie updateMovie(String title, MovieRequest request, String token) {
//        User user = userService.getUserProfileFromToken(token);
//        if (user == null) {
//            throw new RuntimeException("User not found or token is invalid.");
//        }
//
//        Movie movie = movieRepository.findByTitle(title).orElseThrow(() -> new RuntimeException("Movie not found"));
//
//        movieMapper.updateMovieFromRequest(movie, request, genreRepository, movieRepository); ;
//
//        return movieRepository.save(movie);
//    }
//
//    @Override
//    public void deleteMovie(String title, String token) {
//        User user = userService.getUserProfileFromToken(token);
//        if (user == null) {
//            throw new RuntimeException("User not found or token is invalid.");
//        }
//
//        movieMapper.deleteMovie(title);
//    }
//
//    @Override
//    public Movie getMovieById(UUID id){
//        return movieRepository.findById(id).orElse(null);
//    }
//    
//    @Override
//    public List<Movie> searchMovies(String keyword) {
//        return movieRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword);
//    }
//
//    @Override
//    public List<Movie> getAllMoviesPaginated(int page, int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        return movieRepository.findByIsDeletedFalse(pageable).getContent();
//    }
//
//    @Override
//    public List<Movie> getMoviesAlphabetically() {
//        return movieRepository.findByIsDeletedFalseOrderByTitleAsc();
//    }
//
//    @Override
//    public List<Movie> getPopularMovies() {
//        return movieRepository.findByIsDeletedFalseOrderByViewsDesc();
//    }
//
//    
//    // 1️⃣ **按电影名称搜索**
//    public List<MovieRequest> searchMoviesByTitle(String title, User user) {
//        List<Movie> movies = movieRepository.findByTitleContainingIgnoreCase(title);
//        // 获取最相关的前三个电影
//        List<Movie> topMovies = movies.stream().limit(3).toList();
//
////        if (user == null) {
////            user = userRepository.findByUsername("default_user");
////            if (user == null) {
////                throw new RuntimeException("Default user not found in database.");
////            }
////        }
////
////        // 记录搜索历史到 MovieAnalytics 表
////        for (Movie movie : topMovies) {
////            MovieAnalytics searchRecord = new MovieAnalytics(movie, user, InteractionType.SEARCH);
////            movieAnalyticsRepository.save(searchRecord);
////        }
//        return movies.stream().map(MovieMapper::toMovieRequest).toList();
//    }
//
//    // 2️⃣ **按电影类型搜索**
//    public List<MovieRequest> searchMoviesByGenre(String genre) {
//        List<Movie> movies = movieRepository.findByGenreName(genre);
//        return movies.stream().map(MovieMapper::toMovieRequest).toList();
//    }
//
//    // 3️⃣ **按年份搜索**
//    public List<MovieRequest> searchMoviesByYear(int year) {
//        List<Movie> movies = movieRepository.findByReleaseYear(year);
//        return movies.stream().map(MovieMapper::toMovieRequest).toList();
//    }
//
//    // 4️⃣ **多条件搜索**
//    public List<MovieRequest> searchMovies(String title, String genre, Integer year) {
//        List<Movie> movies = movieRepository.searchMovies(title, genre, year);
//        return movies.stream().map(MovieMapper::toMovieRequest).toList();
//    }
//
//    // 3️⃣ **按演员搜索**
//    public List<MovieRequest> getMoviesByActor(String actorName) {
//        List<Movie> movies = movieRepository.findByActorName(actorName);
//        return movies.stream().map(MovieMapper::toMovieRequest).toList();
//    }
//
//    // 4️⃣ **按国家搜索**
//    public List<MovieRequest> getMoviesByCountry(String country) {
//        List<Movie> movies = movieRepository.findByCountryName(country);
//        return movies.stream().map(MovieMapper::toMovieRequest).toList();
//    }
//
//    // 5️⃣ **按制片公司搜索**
//    public List<MovieRequest> getMoviesByProducer(String company) {
//        List<Movie> movies = movieRepository.findByProducerName(company);
//        return movies.stream().map(MovieMapper::toMovieRequest).toList();
//    }
//
//    // 2️⃣ **按导演搜索**
//    public List<MovieRequest> getMoviesByDirector(String directorName) {
//        List<Movie> movies = movieRepository.findByDirectorName(directorName);
//        return movies.stream().map(MovieMapper::toMovieRequest).toList();
//    }
//
//    // 1️⃣ **按观看人数降序排序**
//    public List<MovieRequest> getMoviesByViews(int limit) {
//        List<Movie> movies = movieRepository.findMoviesOrderByViewsDesc(PageRequest.of(0, limit));
//        return movies.stream().map(MovieMapper::toMovieRequest).toList();
//    }
//
//    // 2️⃣ **按评分降序排序**
//    public List<MovieRequest> getMoviesByRating(int limit) {
//        List<Movie> movies = movieRepository.findMoviesOrderByRatingDesc(PageRequest.of(0, limit));
//        return movies.stream().map(MovieMapper::toMovieRequest).toList();
//    }
//
//    // 6️⃣ **多条件搜索**
//    public List<MovieRequest> searchMovies(String title, String genre, Integer year, Double minRating, Double maxRating, String director, String actor, String country, String company) {
//        List<Movie> movies = movieRepository.searchMovies(title, genre, year, minRating, maxRating, director, actor, country, company);
//        return movies.stream().map(MovieMapper::toMovieRequest).toList();
//    }
//
//    private static final int MAX_ROWS = 1000;
//    private static final String pathPrefix = "/xxx/xxx/xx/";
//    public void importMoviesFromExcel(String filePath) {
//        try (FileInputStream fis = new FileInputStream(new File(filePath));
//             Workbook workbook = new XSSFWorkbook(fis)) {
//
//            Sheet sheet = workbook.getSheetAt(0);
//            Iterator<Row> rowIterator = sheet.iterator();
//
//            List<Movie> movieList = new ArrayList<>();
//
//            User defaultUser = userRepository.findByUsername("default_user");
//            if (defaultUser == null) {
//                throw new RuntimeException("Default user not found in database.");
//            }
//
//            // 跳过表头
//            if (rowIterator.hasNext()) rowIterator.next();
//
//            int count = 0;
//            while (rowIterator.hasNext() && count < MAX_ROWS) {
//                Row row = rowIterator.next();
//
//                // 解析 Excel 数据
//                String title = row.getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue();
//                int releaseYear = (int) row.getCell(4, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getNumericCellValue();
//                String countryStr = row.getCell(3, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue();
//                double duration = row.getCell(10, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getNumericCellValue();
//                String description = row.getCell(11, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue();
//                double rating = row.getCell(8, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getNumericCellValue();
//                int views = (int) row.getCell(9, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getNumericCellValue();
//                double posterUrlCell = row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getNumericCellValue();
//                String genresStr = row.getCell(5, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue();
//                String actorsStr = row.getCell(7, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue();
//                String directorsStr = row.getCell(6, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue();
//                String posterUrl = pathPrefix + String.format("%.0f", posterUrlCell);
//                // 解析类别、演员、导演
//                List<Genre> genres = getOrCreateGenres(genresStr);
//                List<Actor> actors = getOrCreateActors(actorsStr);
//                List<Director> directors = getOrCreateDirectors(directorsStr);
//                List<Country> country = getOrCreateCountries(countryStr);
//                // 创建 Movie 实体
//                Movie movie = new Movie();
//                movie.setTitle(title);
//                movie.setReleaseYear(releaseYear);
//                movie.setCountries(country);
//                movie.setDuration(duration);
//                movie.setDescription(description);
//                movie.setRating(rating);
//                movie.setViews(views);
//                movie.setPosterUrl(posterUrl);
//                movie.setGenres(genres);
//                movie.setActors(actors);
//                movie.setDirectors(directors);
//
//// **处理评分**
//                MovieRating defaultRating = new MovieRating();
//                defaultRating.setMovie(movie);
//                defaultRating.setUser(defaultUser);
//                defaultRating.setRating(rating); // 默认评分 0
//                movie.setRatings(List.of(defaultRating));
//
//
//
//                movieList.add(movie);
//                count++;
//            }
//
//            // 批量保存
//            if (!movieList.isEmpty()) {
//                movieRepository.saveAll(movieList);
//                System.out.println("成功导入 " + movieList.size() + " 条电影数据");
//            } else {
//                System.out.println("Excel 文件为空，未导入任何数据");
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    private List<Country> getOrCreateCountries(String countryStr){
//        List<Country> countries = new ArrayList<>();
//        if (countryStr.isEmpty()) return countries;
//
//        String[] countryNames = countryStr.split("/");
//        for (String name : countryNames) {
//            name = name.trim();
//            String finalName = name;
//            Country country = countryRepository.findByName(name).orElseGet(() -> {
//                Country newCountry = new Country();
//                newCountry.setName(finalName);
//                return countryRepository.save(newCountry);
//            });
//            countries.add(country);
//        }
//        return countries;
//
//    }
//
//
//    // 获取或创建类别
//    private List<Genre> getOrCreateGenres(String genreStr) {
//        List<Genre> genres = new ArrayList<>();
//        if (genreStr.isEmpty()) return genres;
//
//        String[] genreNames = genreStr.split("/");
//        for (String name : genreNames) {
//            name = name.trim();
//            String finalName = name;
//            Genre genre = genreRepository.findByName(name).orElseGet(() -> {
//                Genre newGenre = new Genre();
//                newGenre.setName(finalName);
//                return genreRepository.save(newGenre);
//            });
//            genres.add(genre);
//        }
//        return genres;
//    }
//
//    // 获取或创建演员
//    private List<Actor> getOrCreateActors(String actorStr) {
//        List<Actor> actors = new ArrayList<>();
//        if (actorStr.isEmpty()) return actors;
//
//        String[] actorNames = actorStr.split("/");
//        for (String name : actorNames) {
//            String[] nameParts = name.trim().split(" ");
//            String firstName = nameParts.length > 0 ? nameParts[0] : "";
//            String lastName = nameParts.length > 1 ? nameParts[1] : "";
//            Actor actor = actorRepository.findByFirstNameAndLastName(firstName,lastName).orElseGet(() -> {
//                Actor newActor = new Actor();
//                newActor.setFirstName(firstName);
//                newActor.setLastName(lastName);
//                return actorRepository.save(newActor);
//            });
//            actors.add(actor);
//        }
//        return actors;
//    }
//
//    // 获取或创建导演
//    private List<Director> getOrCreateDirectors(String directorStr) {
//        List<Director> directors = new ArrayList<>();
//        if (directorStr.isEmpty()) return directors;
//
//        String[] directorNames = directorStr.split("/");
//        for (String fullName : directorNames) {
//            String[] nameParts = fullName.trim().split(" ");
//            String firstName = nameParts.length > 0 ? nameParts[0] : "";
//            String lastName = nameParts.length > 1 ? nameParts[1] : "";
//
//            Director director = directorRepository.findByFirstNameAndLastName(firstName, lastName).orElseGet(() -> {
//                Director newDirector = new Director();
//                newDirector.setFirstName(firstName);
//                newDirector.setLastName(lastName);
//                return directorRepository.save(newDirector);
//            });
//            directors.add(director);
//        }
//        return directors;
//    }
//
//    public List<Map<String, Object>> callPythonRecommendApi(List<Map<String, Object>> userInteractions) {
//        String pythonApiUrl = "http://127.0.0.1:5000/recommend";
//        RestTemplate restTemplate = new RestTemplate();
//
//        try {
//            // 设置请求头
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
//
//            // 将交互数据转换为 JSON 格式
//            HttpEntity<List<Map<String, Object>>> requestEntity = new HttpEntity<>(userInteractions, headers);
//
//            // 调用 Python API
//            ResponseEntity<Map> response = restTemplate.exchange(
//                    pythonApiUrl,
//                    HttpMethod.POST,
//                    requestEntity,
//                    Map.class
//            );
//            System.out.println("Sending data to Python API: " + userInteractions);
//
//            // 解析返回结果
//            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
//                return (List<Map<String, Object>>) response.getBody().get("recommendations");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("Sending data to Python API: " + userInteractions);
//
//            throw new RuntimeException("Failed to call Python API: " + e.getMessage());
//        }
//
//        return Collections.emptyList(); // 如果调用失败，返回空列表
//    }

}
