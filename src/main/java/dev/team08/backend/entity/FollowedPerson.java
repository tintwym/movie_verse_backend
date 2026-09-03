package dev.team08.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "followed_people", uniqueConstraints = {
        @UniqueConstraint(name = "uk_follow_user_person", columnNames = {"user_id", "tmdb_person_id"})
})
public class FollowedPerson extends BaseEntity {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "tmdb_person_id", nullable = false)
    private Integer tmdbPersonId;

    @Column(name = "person_name", nullable = false, length = 200)
    private String personName;

    @Column(name = "profile_path", length = 300)
    private String profilePath;

    @Column(name = "last_notified_credit_id")
    private Integer lastNotifiedCreditId;

    public FollowedPerson() {}

    public FollowedPerson(User user, Integer tmdbPersonId, String personName, String profilePath) {
        this.user = user;
        this.tmdbPersonId = tmdbPersonId;
        this.personName = personName;
        this.profilePath = profilePath;
    }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
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
