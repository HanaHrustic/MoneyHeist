package ag04.project.moneyheist.api.command;

import ag04.project.moneyheist.domain.Skill;

import java.util.Set;

public class MemberCommand {
    private String name;
    private Character sex;
    private String email;
    private Set<SkillCommand> skills;
    private String mainSkill;
    private String status;

    public MemberCommand(String name, Character sex, String email, Set<SkillCommand> skills, String mainSkill, String status) {
        this.name = name;
        this.sex = sex;
        this.email = email;
        this.skills = skills;
        this.mainSkill = mainSkill;
        this.status = status;
    }

    public MemberCommand() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Character getSex() {
        return sex;
    }

    public void setSex(Character sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<SkillCommand> getSkills() {
        return skills;
    }

    public void setSkills(Set<SkillCommand> skills) {
        this.skills = skills;
    }

    public String getMainSkill() {
        return mainSkill;
    }

    public void setMainSkill(String mainSkill) {
        this.mainSkill = mainSkill;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
