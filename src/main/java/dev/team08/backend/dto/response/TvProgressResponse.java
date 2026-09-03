package dev.team08.backend.dto.response;

public class TvProgressResponse {
    private Integer tmdbTvId;
    private int seasonNumber;
    private int episodeNumber;
    private boolean watched;

    public TvProgressResponse() {}

    public TvProgressResponse(Integer tmdbTvId, int seasonNumber, int episodeNumber, boolean watched) {
        this.tmdbTvId = tmdbTvId;
        this.seasonNumber = seasonNumber;
        this.episodeNumber = episodeNumber;
        this.watched = watched;
    }

    public Integer getTmdbTvId() { return tmdbTvId; }
    public void setTmdbTvId(Integer tmdbTvId) { this.tmdbTvId = tmdbTvId; }
    public int getSeasonNumber() { return seasonNumber; }
    public void setSeasonNumber(int seasonNumber) { this.seasonNumber = seasonNumber; }
    public int getEpisodeNumber() { return episodeNumber; }
    public void setEpisodeNumber(int episodeNumber) { this.episodeNumber = episodeNumber; }
    public boolean isWatched() { return watched; }
    public void setWatched(boolean watched) { this.watched = watched; }
}
