package dev.team08.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tv_progress", uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_tv_progress_user_show_ep",
                columnNames = {"user_id", "tmdb_tv_id", "season_number", "episode_number"}
        )
})
public class TvProgress extends BaseEntity {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "tmdb_tv_id", nullable = false)
    private Integer tmdbTvId;

    @Column(name = "season_number", nullable = false)
    private int seasonNumber;

    @Column(name = "episode_number", nullable = false)
    private int episodeNumber;

    @Column(name = "watched", nullable = false)
    private boolean watched = true;

    public TvProgress() {}

    public TvProgress(User user, Integer tmdbTvId, int seasonNumber, int episodeNumber) {
        this.user = user;
        this.tmdbTvId = tmdbTvId;
        this.seasonNumber = seasonNumber;
        this.episodeNumber = episodeNumber;
        this.watched = true;
    }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Integer getTmdbTvId() { return tmdbTvId; }
    public void setTmdbTvId(Integer tmdbTvId) { this.tmdbTvId = tmdbTvId; }
    public int getSeasonNumber() { return seasonNumber; }
    public void setSeasonNumber(int seasonNumber) { this.seasonNumber = seasonNumber; }
    public int getEpisodeNumber() { return episodeNumber; }
    public void setEpisodeNumber(int episodeNumber) { this.episodeNumber = episodeNumber; }
    public boolean isWatched() { return watched; }
    public void setWatched(boolean watched) { this.watched = watched; }
}
