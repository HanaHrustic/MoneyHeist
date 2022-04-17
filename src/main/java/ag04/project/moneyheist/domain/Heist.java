package ag04.project.moneyheist.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
public class Heist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String location;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @OneToMany(mappedBy = "heist")
    private List<HeistSkill> heistSkills;

    @Enumerated(EnumType.STRING)
    private HeistStatus status = HeistStatus.PLANNING;

    @OneToMany(mappedBy = "heist")
    private Set<MemberHeist> memberHeists;

    public Heist(Long id, String name, String location, LocalDateTime startTime, LocalDateTime endTime, List<HeistSkill> heistSkills, HeistStatus status, Set<MemberHeist> memberHeists) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
        this.heistSkills = heistSkills;
        this.status = status;
        this.memberHeists = memberHeists;
    }

    public Heist() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public List<HeistSkill> getHeistSkills() {
        return heistSkills;
    }

    public void setHeistSkills(List<HeistSkill> heistSkills) {
        this.heistSkills = heistSkills;
    }

    public HeistStatus getStatus() {
        return status;
    }

    public void setStatus(HeistStatus status) {
        this.status = status;
    }

    public Set<MemberHeist> getMemberHeists() {
        return memberHeists;
    }

    public void setMemberHeists(Set<MemberHeist> memberHeists) {
        this.memberHeists = memberHeists;
    }
}
