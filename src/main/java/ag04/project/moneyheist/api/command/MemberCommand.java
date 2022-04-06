package ag04.project.moneyheist.api.command;

import ag04.project.moneyheist.annotations.EmailDuplicate;
import ag04.project.moneyheist.annotations.SkillNameDuplicate;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.util.List;

public class MemberCommand {
    private String name;

    @Pattern(regexp = "M|F")
    private String sex;

    @Email
    @EmailDuplicate
    private String email;

    @Valid
    @SkillNameDuplicate
    private List<SkillCommand> skills;
    private String mainSkill;
    private String status;

    public MemberCommand(String name, String sex, String email, List<SkillCommand> skills, String mainSkill, String status) {
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<SkillCommand> getSkills() {
        return skills;
    }

    public void setSkills(List<SkillCommand> skills) {
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
