package ag04.project.moneyheist.api.DTO;

import ag04.project.moneyheist.api.view.GetHeist;
import ag04.project.moneyheist.api.view.GetHeistStatus;
import ag04.project.moneyheist.domain.HeistStatus;
import com.fasterxml.jackson.annotation.JsonView;

import java.time.LocalDateTime;
import java.util.List;

public class HeistDTO {
    private Long id;

    @JsonView(GetHeist.class)
    private String name;

    @JsonView(GetHeist.class)
    private String location;

    @JsonView(GetHeist.class)
    private LocalDateTime startTime;

    @JsonView(GetHeist.class)
    private LocalDateTime endTime;

    @JsonView(GetHeist.class)
    private List<HeistSkillDTO> skills;

    @JsonView({GetHeist.class, GetHeistStatus.class})
    private HeistStatus status;

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

    public List<HeistSkillDTO> getSkills() {
        return skills;
    }

    public void setSkills(List<HeistSkillDTO> skills) {
        this.skills = skills;
    }

    public HeistStatus getStatus() {
        return status;
    }

    public void setStatus(HeistStatus status) {
        this.status = status;
    }
}
