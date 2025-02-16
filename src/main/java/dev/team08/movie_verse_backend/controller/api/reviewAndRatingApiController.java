package dev.team08.movie_verse_backend.controller.api;

import dev.team08.movie_verse_backend.service.MovieRatingService;
import dev.team08.movie_verse_backend.service.MovieReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class reviewAndRatingApiController {
    @Autowired
    private MovieReviewService movieReviewService;
    @Autowired
    private MovieRatingService movieRatingService;

    //**** these methods are likely to be used by web and mobile app ****
    //get review and rating of a user for a movie
//    @GetMapping("/getreviewandrating/movies/{movieId}/users/{userId}")
//    public ResponseEntity<ReviewAndRatingResponse> getReviewAndRatingForMovie(
//            @PathVariable UUID movieId,
//            @PathVariable UUID userId) {
//        //get review and rating using user and movie id
//        MovieReview movieReview = movieReviewService.getReviewByUserAndMovie(userId, movieId);
//        MovieRating movieRating = movieRatingService.getRatingByUserAndMovie(userId, movieId);
//        ReviewAndRatingResponse response = new ReviewAndRatingResponse(
//                userId,
//                movieId,
//                movieReview != null ? movieReview.getContent() :null,
//                movieRating != null ? movieRating.getRating() : 0.0
//        );
//        return ResponseEntity.ok(response);
//    }

    //get review and rating of a user for an episode
//    @GetMapping("/getreviewandrating/series/{seriesId}/seasons/{seasonId}/episodes/{episodeId}/users/{userId}")
//    public ResponseEntity<ReviewAndRatingResponse> getReviewAndRatingForEpisode(
//            @PathVariable UUID seriesId,
//            @PathVariable UUID seasonId,
//            @PathVariable UUID episodeId,
//            @PathVariable UUID userId) {
//        EpisodeReview review = episodeReviewService.getReviewByUserAndEpisode(userId, episodeId);
//        EpisodeRating rating = episodeRatingService.getRatingByUserAndEpisode(userId, episodeId);
//        ReviewAndRatingResponse response = new ReviewAndRatingResponse(
//                userId,
//                episodeId,
//                review != null ? review.getContent() :null,
//                rating != null ? rating.getRating() : 0
//        );
//        return ResponseEntity.ok(response);
//    }
//
//    //create or update new rating and review for a movie
//    @PostMapping("/createorupdateratingandreview/movies/{movieId}/users/{userId}")
//    public ResponseEntity<String> createOrUpdateRatingAndReviewForMovie(
//            @PathVariable UUID movieId,
//            @PathVariable UUID userId,
//            @RequestBody RatingAndReviewRequest request) {
//        // Validate that the movie ID in the URL matches the movie ID in the request payload
//        if (!movieId.equals(request.getItemId())) {
//            return ResponseEntity.badRequest().body("Movie ID mismatch");
//        }
//        //handle rating
//        MovieRating existingMovieRating = movieRatingService.getRatingByUserAndMovie(request.getUserId(), request.getItemId());
//        if (existingMovieRating == null) {
//            movieRatingService.createRating(request.getUserId(), request.getItemId(), request.getRating());
//        } else {
//            movieRatingService.updateRating(existingMovieRating.getId(), request.getRating());
//        }
//
//        // Handle Review
//        MovieReview existingMovieReview = movieReviewService.getReviewByUserAndMovie(request.getUserId(), request.getItemId());
//        if (existingMovieReview == null) {
//            movieReviewService.createReview(request.getUserId(), request.getItemId(), request.getReviewContent());
//        } else {
//            movieReviewService.updateReview(existingMovieReview.getId(), request.getReviewContent());
//        }
//
//        return ResponseEntity.ok("Rating and review processed successfully");
//    }
//
//    //create or update new rating and review for an episode
//    @PostMapping("/createorupdateratingandreview/series/{seriesId}/seasons/{seasonId}/episodes/{episdoeId}/users/{userId}")
//    public ResponseEntity<String> createOrUpdateRatingAndReviewForEpisode(
//            @PathVariable UUID seriesId,
//            @PathVariable UUID seasonId,
//            @PathVariable UUID episodeId,
//            @PathVariable UUID userId,
//            @RequestBody RatingAndReviewRequest request) {
//        if (!episodeId.equals(request.getItemId())) {
//            return ResponseEntity.badRequest().body("Episode ID mismatch");
//        }
//        //handle rating
//        EpisodeRating existingRating = episodeRatingService.getRatingByUserAndEpisode(request.getUserId(), request.getItemId());
//        if (existingRating == null) {
//            episodeRatingService.createRating(request.getUserId(), request.getItemId(), request.getRating());
//        } else {
//            episodeRatingService.updateRating(existingRating.getId(), request.getRating());
//        }
//        //handle review
//        EpisodeReview existingReview = episodeReviewService.getReviewByUserAndEpisode(request.getUserId(), request.getItemId());
//        if (existingReview == null) {
//            episodeReviewService.createReview(request.getUserId(), request.getItemId(), request.getReviewContent());
//        } else {
//            episodeReviewService.updateReview(existingReview.getId(), request.getReviewContent());
//        }
//        return ResponseEntity.ok("Rating and review processed successfully");
//    }
//
//    //update like of a review of a movie
//    @PatchMapping("/review/like/movies/{movieId}/{reviewId}")
//    public ResponseEntity<String> incrementLikesForMovieReview(
//            @PathVariable UUID movieId,
//            @PathVariable UUID reviewId) {
//        try {
//            movieReviewService.incrementLikes(reviewId);
//            return ResponseEntity.ok("Like count updated successfully");
//        } catch (EntityNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        }
//    }
//
//    //update like of a review of an episode
//    @PatchMapping("/review/like/series/{seriesId}/seasons/{seasonId}/episodes/{episodeId}/{reviewId}")
//    public ResponseEntity<String> incrementLikesForEpisodeReview(
//            @PathVariable UUID seriesId,
//            @PathVariable UUID seasonId,
//            @PathVariable UUID episodeId,
//            @PathVariable UUID reviewId) {
//        try {
//            episodeReviewService.incrementLikes(reviewId);
//            return ResponseEntity.ok("Like count updated successfully");
//        } catch (EntityNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        }
//    }
//
//    //get all reviews of a movie
//    @GetMapping("/review/movie/{movieId}")
//    public ResponseEntity<List<MovieReview>> getReviewsByMovieId(@PathVariable UUID movieId) {
//        List<MovieReview> movieReviews = movieReviewService.getReviewsByMovieId(movieId);
//        return ResponseEntity.ok(movieReviews);
//    }
//
//    //get all reviews of an episode
//    @GetMapping("/review/series/{seriesId}/seasons/{seasonId}/episodes/{episodeId}")
//    public ResponseEntity<List<EpisodeReview>> getReviewsByEpisodeId(
//            @PathVariable UUID seriesId,
//            @PathVariable UUID seasonId,
//            @PathVariable UUID episodeId) {
//        List<EpisodeReview> reviews = episodeReviewService.getReviewsByEpisodeId(episodeId);
//        return ResponseEntity.ok(reviews);
//    }
//
////    //update a review of a movie
////    @PutMapping("/updatereview/movie/{movieId}/{reviewId}")
////    public ResponseEntity<MovieReview> updateReviewForMovie(
////            @PathVariable UUID movieId,
////            @PathVariable UUID reviewId,
////            @RequestBody MovieReview movieReviewDetails) {
////
////        // Call the service layer to update the review
////        MovieReview updatedMovieReview = movieReviewService.updateReview(reviewId, movieReviewDetails.getContent());
////
////        // Return the updated review as a response
////        return ResponseEntity.ok(updatedMovieReview);
////    }
////
////    //update a review of an episode
////    @PutMapping("/updatereview/series/{seriesId}/seasons/{seasonId}/episodes/{episodeId}/{reviewId}")
////    public ResponseEntity<EpisodeReview> updateReviewForEpisode(
////            @PathVariable UUID seriesId,
////            @PathVariable UUID seasonId,
////            @PathVariable UUID episodeId,
////            @PathVariable UUID reviewId,
////            @RequestBody MovieReview movieReviewDetails) {
////
////        // Call the service layer to update the review
////        EpisodeReview updatedReview = episodeReviewService.updateReview(reviewId, movieReviewDetails.getContent());
////
////        // Return the updated review as a response
////        return ResponseEntity.ok(updatedReview);
////    }
//
//    //delete a review of a movie
//    @DeleteMapping("/deletereview/movies/{movieId}/{reviewId}")
//    public ResponseEntity<Void> deleteReviewForMovie(
//            @PathVariable UUID movieId,
//            @PathVariable UUID reviewId) {
//        movieReviewService.deleteReview(reviewId);
//        return ResponseEntity.noContent().build();
//    }
//
//    //delete a review of an episode
//    @DeleteMapping("/deletereview/series/{seriesId}/seasons/{seasonId}/episodes/{episodeId}/{reviewId}")
//    public ResponseEntity<Void> deleteReviewForEpisode(
//            @PathVariable UUID seriesId,
//            @PathVariable UUID seasonId,
//            @PathVariable UUID episodeId,
//            @PathVariable UUID reviewId) {
//        episodeReviewService.deleteReview(reviewId);
//        return ResponseEntity.noContent().build();
//    }
//
//    //get the average rating of a movie by all user
//    @GetMapping("/averagerating/movie/{movieId}")
//    public ResponseEntity<Double> getAverageRatingByMovie(@PathVariable UUID movieId) {
//        Double averageRating = movieRatingService.getAverageRatingByMovie(movieId);
//        return ResponseEntity.ok(averageRating != null ? averageRating : 0.0);
//    }
//
//    //get the average rating of an episode by all user
//    @GetMapping("/averagerating/series/{seriesId}/seasons/{seasonId}/episodes/{episodeId}")
//    public ResponseEntity<Double> getAverageRatingByEpisode(
//            @PathVariable UUID seriesId,
//            @PathVariable UUID seasonId,
//            @PathVariable UUID episodeId) {
//        Double averageRating = episodeRatingService.getAverageRatingByEpisode(episodeId);
//        return ResponseEntity.ok(averageRating != null ? averageRating : 0.0);
//    }
//
//    //get the average rating of a season by all user
//    @GetMapping("/averagerating/series/{seriesId}/seasons/{seasonId}")
//    public ResponseEntity<Double> getAverageRatingBySeason(
//            @PathVariable UUID seriesId,
//            @PathVariable UUID seasonId) {
//        Double averageRating = episodeRatingService.getAverageRatingBySeason(seasonId);
//        return ResponseEntity.ok(averageRating != null ? averageRating : 0.0);
//    }
//
//    //get the average rating of a series by all user
//    @GetMapping("/averagerating/series/{seriesId}")
//    public ResponseEntity<Double> getAverageRatingBySeries(@PathVariable UUID seriesId) {
//        Double averageRating = episodeRatingService.getAverageRatingBySeries(seriesId);
//        return ResponseEntity.ok(averageRating != null ? averageRating : 0.0);
//    }
//
//    //**** these methods are not likely to be used by web and mobile app and for testing only ****
//    //get all reviews for all movies
//    @GetMapping("/reviews/movies")
//    public ResponseEntity<List<MovieReview>> getAllReviewsForMovies() {
//        return ResponseEntity.ok(movieReviewService.getAllReviews());
//    }
//
//    //get all reviews for all episodes
//    @GetMapping("/reviews/series")
//    public ResponseEntity<List<EpisodeReview>> getAllReviewsForMovie() {
//        return ResponseEntity.ok(episodeReviewService.getAllReviews());
//    }
//
//    //get all rating for all movies
//    @GetMapping("/ratings/movie")
//    public ResponseEntity<List<MovieRating>> getAllRatingsForMovies() {
//        return ResponseEntity.ok(movieRatingService.getAllRatings());
//    }
//
//    //get all rating for all episodes
//    @GetMapping("/ratings/series")
//    public ResponseEntity<List<EpisodeRating>> getAllRatingsForEpisodes() {
//        return ResponseEntity.ok(episodeRatingService.getAllRatings());
//    }
//
//    //get specific review for movie
//    @GetMapping("/review/movies/{id}")
//    public ResponseEntity<MovieReview> getReviewByIdForMovie(@PathVariable UUID id) {
//        return ResponseEntity.ok(movieReviewService.getReviewById(id));
//    }
//
//    //get specific review for episode
//    @GetMapping("/review/series/{id}")
//    public ResponseEntity<EpisodeReview> getReviewByIdForEpisode(@PathVariable UUID id) {
//        return ResponseEntity.ok(episodeReviewService.getReviewById(id));
//    }
//
//    //get specific rating for movie
//    @GetMapping("/rating/movie/{id}")
//    public ResponseEntity<MovieRating> getRatingByIdForMovie(@PathVariable UUID id) {
//        return ResponseEntity.ok(movieRatingService.getRatingById(id));
//    }
//
//    //get specific rating for episode
//    @GetMapping("/rating/series/{id}")
//    public ResponseEntity<EpisodeRating> getRatingByIdForEpisode(@PathVariable UUID id) {
//        return ResponseEntity.ok(episodeRatingService.getRatingById(id));
//    }
//
//    //get review by user ID and movie ID
//    @GetMapping("/review/user/{userId}/movie/{movieId}")
//    public ResponseEntity<MovieReview> getReviewByUserAndMovie(
//            @PathVariable UUID userId,
//            @PathVariable UUID movieId) {
//
//        MovieReview movieReview = movieReviewService.getReviewByUserAndMovie(userId, movieId);
//        return ResponseEntity.ok(movieReview);
//    }
//
//    //get review by user ID and episode ID
//    @GetMapping("/review/user/{userId}/series/{seriesId}/seasons/{seasonId}/episodes/{episodeId}")
//    public ResponseEntity<EpisodeReview> getReviewByUserAndEpisode(
//            @PathVariable UUID userId,
//            @PathVariable UUID seriesId,
//            @PathVariable UUID seasonId,
//            @PathVariable UUID episodeId) {
//
//        EpisodeReview review = episodeReviewService.getReviewByUserAndEpisode(userId, episodeId);
//        return ResponseEntity.ok(review);
//    }
//
//    //get rating by user ID and movie ID
//    @GetMapping("/rating/user/{userId}/movie/{movieId}")
//    public ResponseEntity<MovieRating> getRatingByUserAndMovie(
//            @PathVariable UUID userId,
//            @PathVariable UUID movieId) {
//
//        MovieRating movieRating = movieRatingService.getRatingByUserAndMovie(userId, movieId);
//        return ResponseEntity.ok(movieRating);
//    }
//
//    //get rating by user ID and episode ID
//    @GetMapping("/rating/user/{userId}/series/{seriesId}/seasons/{seasonId}/episodes/{episodeId}")
//    public ResponseEntity<EpisodeRating> getRatingByUserAndEpisode(
//            @PathVariable UUID userId,
//            @PathVariable UUID seriesId,
//            @PathVariable UUID seasonId,
//            @PathVariable UUID episodeId) {
//
//        EpisodeRating rating = episodeRatingService.getRatingByUserAndEpisode(userId, episodeId);
//        return ResponseEntity.ok(rating);
//    }
//
//    //get all reviews of a user for movies
//    @GetMapping("/review/movies/user/{userId}")
//    public ResponseEntity<List<MovieReview>> getReviewsByUserIdForMovies(@PathVariable UUID userId) {
//        List<MovieReview> movieReviews = movieReviewService.getReviewsByUserId(userId);
//        return ResponseEntity.ok(movieReviews);
//    }
//
//    //get all reviews of a user for episodes
//    @GetMapping("/review/series/user/{userId}")
//    public ResponseEntity<List<EpisodeReview>> getReviewsByUserIdForEpisodes(@PathVariable UUID userId) {
//        List<EpisodeReview> reviews = episodeReviewService.getReviewsByUserId(userId);
//        return ResponseEntity.ok(reviews);
//    }
//
//    //get all ratings of a movie
//    @GetMapping("/rating/movie/{movieId}")
//    public ResponseEntity<List<MovieRating>> getRatingsByMovieId(@PathVariable UUID movieId) {
//        List<MovieRating> movieRatings = movieRatingService.getRatingsByMovieId(movieId);
//        return ResponseEntity.ok(movieRatings);
//    }
//
//    //get all ratings of an episode
//    @GetMapping("/rating/series/{seriesId}/seasons/{seasonId}/episodes/{episodeId}")
//    public ResponseEntity<List<EpisodeRating>> getRatingsByEpisodeId(
//            @PathVariable UUID seriesId,
//            @PathVariable UUID seasonId,
//            @PathVariable UUID episodeId) {
//        List<EpisodeRating> ratings = episodeRatingService.getRatingsByEpisodeId(episodeId);
//        return ResponseEntity.ok(ratings);
//    }
//
//    //get all ratings of a user for Movies
//    @GetMapping("/rating/movies/user/{userId}")
//    public ResponseEntity<List<MovieRating>> getRatingsByUserIdForMovies(@PathVariable UUID userId) {
//        List<MovieRating> movieRatings = movieRatingService.getRatingsByUserId(userId);
//        return ResponseEntity.ok(movieRatings);
//    }
//
//    //get all ratings of a user for episodes
//    @GetMapping("/rating/series/{seriesId}/seasons/{seasonId}/episodes/{episodeId}/user/{userId}")
//    public ResponseEntity<List<EpisodeRating>> getRatingsByUserIdForEpisodes(
//            @PathVariable UUID seriesId,
//            @PathVariable UUID seasonId,
//            @PathVariable UUID episodeId,
//            @PathVariable UUID userId) {
//        List<EpisodeRating> ratings = episodeRatingService.getRatingsByUserId(userId);
//        return ResponseEntity.ok(ratings);
//    }

}