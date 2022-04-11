package ag04.project.moneyheist.api.command;

import ag04.project.moneyheist.api.group.CreateMember;
import ag04.project.moneyheist.api.group.UpdateMemberSkill;

import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;

public class SkillCommand {

    private String name;

    @Pattern(regexp = "^[*]{1,10}$")
    private String level;

    @Null(groups = {CreateMember.class, UpdateMemberSkill.class})
    private Long members;

    public SkillCommand(String name, String level, Long members) {
        this.name = name;
        this.level = level;
        this.members = members;
    }

    public SkillCommand() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Long getMembers() {
        return members;
    }

    public void setMembers(Long members) {
        this.members = members;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SkillCommand that = (SkillCommand) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return level != null ? level.equals(that.level) : that.level == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (level != null ? level.hashCode() : 0);
        return result;
    }
}
