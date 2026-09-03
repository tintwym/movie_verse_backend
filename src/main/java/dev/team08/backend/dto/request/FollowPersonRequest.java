package dev.team08.backend.dto.request;

public class FollowPersonRequest {
    private Integer tmdbPersonId;
    private String personName;
    private String profilePath;

    public Integer getTmdbPersonId() { return tmdbPersonId; }
    public void setTmdbPersonId(Integer tmdbPersonId) { this.tmdbPersonId = tmdbPersonId; }
    public String getPersonName() { return personName; }
    public void setPersonName(String personName) { this.personName = personName; }
    public String getProfilePath() { return profilePath; }
    public void setProfilePath(String profilePath) { this.profilePath = profilePath; }
}
