package dev.team08.movie_verse_backend.dto.request;

import java.util.List;


public class AddMovieRequest {
    private String title;
    private int releaseYear;
    private int views;
    private String description;
    private List<String> genreNames;
    private List<CastRequest> actorNames;
    private List<CastRequest> directorNames;
    private List<String> producerNames;
    private String prequelTitle;
    private String sequelTitle;
    private double duration;
    private List<Integer> ratings;


    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public List<Integer> getRatings() {
        return ratings;
    }

    public void setRatings(List<Integer> ratings) {
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

    public String getPrequelTitle() {
        return prequelTitle;
    }

    public void setPrequelTitle(String prequelTitle) {
        this.prequelTitle = prequelTitle;
    }

    public String getSequelTitle() {
        return sequelTitle;
    }

    public void setSequelTitle(String sequelTitle) {
        this.sequelTitle = sequelTitle;
    }
}
