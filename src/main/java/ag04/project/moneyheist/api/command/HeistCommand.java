package ag04.project.moneyheist.api.command;

import ag04.project.moneyheist.annotations.DateTimeCheck;
import ag04.project.moneyheist.annotations.NameDuplicate;
import ag04.project.moneyheist.annotations.SkillNameDuplicate;
import ag04.project.moneyheist.api.group.ConfirmMembersInHeist;
import ag04.project.moneyheist.api.group.CreateHeist;
import ag04.project.moneyheist.api.group.UpdateHeistSkill;

import javax.validation.constraints.Null;
import java.time.LocalDateTime;
import java.util.List;

@DateTimeCheck(groups = CreateHeist.class)
public class HeistCommand {

    @Null(groups = {UpdateHeistSkill.class, ConfirmMembersInHeist.class})
    @NameDuplicate(groups = CreateHeist.class)
    private String name;

    @Null(groups = {UpdateHeistSkill.class, ConfirmMembersInHeist.class})
    private String location;

    @Null(groups = {UpdateHeistSkill.class, ConfirmMembersInHeist.class})
    private LocalDateTime startTime;

    @Null(groups = {UpdateHeistSkill.class, ConfirmMembersInHeist.class})
    private LocalDateTime endTime;

    @SkillNameDuplicate(considerSkillLevel = true, groups = {UpdateHeistSkill.class, CreateHeist.class})
    @Null(groups = ConfirmMembersInHeist.class)
    private List<SkillCommand> skills;

    @Null(groups = {UpdateHeistSkill.class, CreateHeist.class})
    private List<String> members;

    public HeistCommand(String name, String location, LocalDateTime startTime, LocalDateTime endTime, List<SkillCommand> skills, List<String> members) {
        this.name = name;
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
        this.skills = skills;
        this.members = members;
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

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }
}
