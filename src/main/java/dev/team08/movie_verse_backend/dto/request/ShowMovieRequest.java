package dev.team08.movie_verse_backend.dto.request;

import java.util.List;
import java.util.UUID;

public class ShowMovieRequest {
    private UUID id;
    private String title;
    private int releaseYear;
    private int views;
    private String description;
    private List<String> genreNames;
    private double duration;
    private List<Double> ratings;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getGenreNames() {
        return genreNames;
    }

    public void setGenreNames(List<String> genreNames) {
        this.genreNames = genreNames;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public List<Double> getRatings() {
        return ratings;
    }

    public void setRatings(List<Double> ratings) {
        this.ratings = ratings;
    }

    public double getAverageRating() {
        if (ratings == null || ratings.isEmpty()) {
            return 0.0;
        }
        double sum = 0;
        for (Double rating : ratings) {
            sum += rating;
        }
        return sum / ratings.size();
    }

    @Override
    public String toString() {
        return "ShowAllMovie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", releaseYear=" + releaseYear +
                ", views=" + views +
                ", description='" + description + '\'' +
                ", genreNames=" + genreNames +
                ", duration=" + duration +
                ", ratings=" + ratings +
                '}';
    }

    public ShowMovieRequest(UUID id, String title, int releaseYear, int views, String description, List<String> genreNames,
                            double duration, List<Double> ratings) {
        this.id = id;
        this.title = title;
        this.releaseYear = releaseYear;
        this.views = views;
        this.description = description;
        this.genreNames = genreNames;
        this.duration = duration;
        this.ratings = ratings;
    }

//    public static ShowMovieRequest fromMovie(Movie movie){
//        return new ShowMovieRequest(movie.getId(),
//                movie.getTitle(),
//                movie.getReleaseYear(),
//                movie.getViews(),
//                movie.getDescription(),
//                movie.getGenres().stream().map(Genre::getName).collect(Collectors.toList()),
//                movie.getDuration(),
//                movie.getRatings().stream().map(MovieRating::getRating).collect(Collectors.toList())
//
//
//        );
//    }
}
