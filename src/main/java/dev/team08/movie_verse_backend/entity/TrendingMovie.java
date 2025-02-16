package dev.team08.movie_verse_backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "trending_movies")
public class TrendingMovie extends BaseEntity{
    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    private int trendScore;

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public int getTrendScore() {
        return trendScore;
    }

    public void setTrendScore(int trendScore) {
        this.trendScore = trendScore;
    }
}
