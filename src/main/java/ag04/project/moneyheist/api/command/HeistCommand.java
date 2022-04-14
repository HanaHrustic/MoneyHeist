package ag04.project.moneyheist.api.command;

import ag04.project.moneyheist.annotations.DateTimeCheck;
import ag04.project.moneyheist.annotations.NameDuplicate;
import ag04.project.moneyheist.annotations.SkillNameDuplicate;
import ag04.project.moneyheist.api.group.CreateHeist;
import ag04.project.moneyheist.api.group.UpdateHeistSkill;

import javax.validation.constraints.Null;
import java.time.LocalDateTime;
import java.util.List;

@DateTimeCheck
public class HeistCommand {

    @Null(groups = UpdateHeistSkill.class)
    @NameDuplicate
    private String name;

    @Null(groups = UpdateHeistSkill.class)
    private String location;

    @Null(groups = UpdateHeistSkill.class)
    private LocalDateTime startTime;

    @Null(groups = UpdateHeistSkill.class)
    private LocalDateTime endTime;

    @SkillNameDuplicate(considerSkillLevel = true, groups = {UpdateHeistSkill.class, CreateHeist.class})
    private List<SkillCommand> skills;

    public HeistCommand(String name, String location, LocalDateTime startTime, LocalDateTime endTime, List<SkillCommand> skills) {
        this.name = name;
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
        this.skills = skills;
    }

    public HeistCommand() {
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

    public List<SkillCommand> getSkills() {
        return skills;
    }

    public void setSkills(List<SkillCommand> skills) {
        this.skills = skills;
    }
}
