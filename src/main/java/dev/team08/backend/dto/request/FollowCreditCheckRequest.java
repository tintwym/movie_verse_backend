package dev.team08.backend.dto.request;

public class FollowCreditCheckRequest {
    private Integer tmdbPersonId;
    private Integer creditId;
    private String title;
    private String mediaType;
    private String releaseDate;

    public Integer getTmdbPersonId() { return tmdbPersonId; }
    public void setTmdbPersonId(Integer tmdbPersonId) { this.tmdbPersonId = tmdbPersonId; }
    public Integer getCreditId() { return creditId; }
    public void setCreditId(Integer creditId) { this.creditId = creditId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getMediaType() { return mediaType; }
    public void setMediaType(String mediaType) { this.mediaType = mediaType; }
    public String getReleaseDate() { return releaseDate; }
    public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }
}
