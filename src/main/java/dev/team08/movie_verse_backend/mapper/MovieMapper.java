package dev.team08.movie_verse_backend.mapper;

import dev.team08.movie_verse_backend.entity.*;
import dev.team08.movie_verse_backend.repository.*;
import org.springframework.stereotype.Component;

@Component
public class MovieMapper {

    private final GenreRepository genreRepository;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;

    // 注入需要的 Repository
    public MovieMapper(GenreRepository genreRepository, 
                       MovieRepository movieRepository, UserRepository userRepository) {
        this.genreRepository = genreRepository;
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
    }

//    public static MovieRequest toMovieRequest(Movie movie) {
//        return new MovieRequest(
//                movie.getId(),
//                movie.getTitle(),
//                movie.getReleaseYear(),
//                movie.getViews(),
//                movie.getDescription(),
//                movie.getGenres().stream().map(Genre::getName).collect(Collectors.toList()),
//                movie.getActors().stream().map(actor -> new CastRequest(actor.getFirstName(), actor.getLastName())).toList(),
//                movie.getDirectors().stream().map(director -> new CastRequest(director.getFirstName(), director.getLastName())).toList(),
//                movie.getProducers().stream().map(Producer::getCompanyName).toList(),
//                movie.getDuration(),
//                movie.getRatings().stream().map(MovieRating::getRating).collect(Collectors.toList()),  // **确保这里转换正确**
//                movie.getPosterUrl()
//        );
//    }

//    // 从 MovieRequest 转换为 Movie 实体
//    public Movie fromMovieRequest(MovieRequest request) {
//        Movie movie = new Movie();
//        movie.setTitle(request.getTitle());
//        movie.setReleaseYear(request.getReleaseYear());
////        movie.setRatingCount();
//        movie.setDescription(request.getDescription());
//        movie.setDuration(request.getDuration());
//        movie.setPosterUrl(request.getPosterUrl());
//        movie.setViews(request.getViews());
//        return movie;
//    }

    public Movie saveMovie(Movie movie) {
        return movieRepository.save(movie);
    }

//    public UUID getMovieIdByTitle(String title) {
//        Optional<Movie> movie = movieRepository.findByTitle(title);
//        if (movie.isPresent()) {
//            return movie.orElseThrow().getId(); // 返回电影的 UUID
//        }
//        return null; // 如果没找到电影，返回 null
//    }

//    public void updateMovieFromRequest(Movie movie, MovieRequest request, GenreRepository genreRepository, MovieRepository movieRepository) {
//        if (request.getTitle() != null) movie.setTitle(request.getTitle());
//        if (request.getReleaseYear() > 0) movie.setReleaseYear(request.getReleaseYear());
//        if (request.getViews() >= 0) movie.setViews(request.getViews());
//        if (request.getDescription() != null) movie.setDescription(request.getDescription());
//        if (request.getDuration() > 0) movie.setDuration(request.getDuration());
//        if (request.getPosterUrl() != null) movie.setPosterUrl(request.getPosterUrl());
//
//        // 更新 Genres
//        if (request.getGenreNames() != null && !request.getGenreNames().isEmpty()) {
//            List<Genre> genres = request.getGenreNames().stream()
//                    .map(name -> genreRepository.findByName(name).orElseGet(() -> {
//                        Genre newGenre = new Genre();
//                        newGenre.setName(name);
//                        return genreRepository.save(newGenre);
//                    }))
//                    .collect(Collectors.toList());
//            movie.setGenres(genres);
//        }
//    }
//
//    public void deleteMovie(String title) {
//        UUID movieId = getMovieIdByTitle(title);
//        if (movieId != null) {
//            movieRepository.deleteById(movieId); // 使用 UUID 删除电影
//        }
//
//    }

}