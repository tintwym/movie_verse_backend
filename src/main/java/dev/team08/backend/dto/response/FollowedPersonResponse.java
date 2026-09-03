package dev.team08.backend.dto.response;

public class FollowedPersonResponse {
    private String id;
    private Integer tmdbPersonId;
    private String personName;
    private String profilePath;
    private Integer lastNotifiedCreditId;

    public FollowedPersonResponse() {}

    public FollowedPersonResponse(
            String id, Integer tmdbPersonId, String personName,
            String profilePath, Integer lastNotifiedCreditId) {
        this.id = id;
        this.tmdbPersonId = tmdbPersonId;
        this.personName = personName;
        this.profilePath = profilePath;
        this.lastNotifiedCreditId = lastNotifiedCreditId;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Integer getTmdbPersonId() { return tmdbPersonId; }
    public void setTmdbPersonId(Integer tmdbPersonId) { this.tmdbPersonId = tmdbPersonId; }
    public String getPersonName() { return personName; }
    public void setPersonName(String personName) { this.personName = personName; }
    public String getProfilePath() { return profilePath; }
    public void setProfilePath(String profilePath) { this.profilePath = profilePath; }
    public Integer getLastNotifiedCreditId() { return lastNotifiedCreditId; }
    public void setLastNotifiedCreditId(Integer lastNotifiedCreditId) {
        this.lastNotifiedCreditId = lastNotifiedCreditId;
    }
}
