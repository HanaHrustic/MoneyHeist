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
}
