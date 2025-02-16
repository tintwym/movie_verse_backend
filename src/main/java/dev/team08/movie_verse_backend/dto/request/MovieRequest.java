package dev.team08.movie_verse_backend.dto.request;

import java.util.List;
import java.util.UUID;

public class MovieRequest {
    private UUID id;
    private String title;
    private int releaseYear;
    private int views;
    private String description;
    private List<String> genreNames;
    private List<CastRequest> actorNames;
    private List<CastRequest> directorNames;
    private List<String> producerNames;
    private double duration;
    private List<Double> ratings;
    private String posterUrl;

    public MovieRequest(UUID id, String title, int releaseYear, int views, String description, List<String> genreNames, List<CastRequest> actorNames, List<CastRequest> directorNames, List<String> producerNames, double duration, List<Double> ratings, String posterUrl) {
        this.id = id;
        this.title = title;
        this.releaseYear = releaseYear;
        this.views = views;
        this.description = description;
        this.genreNames = genreNames;
        this.posterUrl = posterUrl;
        this.actorNames = actorNames;
        this.directorNames = directorNames;
        this.producerNames = producerNames;
        this.duration = duration;
        this.ratings = ratings;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public List<Double> getRatings() {
        return ratings;
    }

    public void setRatings(List<Double> ratings) {
        this.ratings = ratings;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
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

    public List<CastRequest> getActorNames() {
        return actorNames;
    }

    public void setActorNames(List<CastRequest> actorNames) {
        this.actorNames = actorNames;
    }

    public List<CastRequest> getDirectorNames() {
        return directorNames;
    }

    public void setDirectorNames(List<CastRequest> directorNames) {
        this.directorNames = directorNames;
    }

    public List<String> getProducerNames() {
        return producerNames;
    }

    public void setProducerNames(List<String> producerNames) {
        this.producerNames = producerNames;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
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

}

