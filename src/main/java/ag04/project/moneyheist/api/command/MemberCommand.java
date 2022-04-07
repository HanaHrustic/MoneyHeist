package ag04.project.moneyheist.api.command;

import ag04.project.moneyheist.annotations.EmailDuplicate;
import ag04.project.moneyheist.annotations.SkillNameDuplicate;
import ag04.project.moneyheist.api.group.CreateMember;
import ag04.project.moneyheist.api.group.UpdateMemberSkill;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import java.util.List;

public class MemberCommand {
    @Null(groups = UpdateMemberSkill.class)
    private String name;

    @Null(groups = UpdateMemberSkill.class)
    @Pattern(regexp = "M|F", groups = CreateMember.class)
    private String sex;

    @Null(groups = UpdateMemberSkill.class)
    @Email(groups = CreateMember.class)
    @EmailDuplicate(groups = CreateMember.class)
    private String email;

    @Valid
    @SkillNameDuplicate(groups = {CreateMember.class, UpdateMemberSkill.class})
    private List<SkillCommand> skills;
    private String mainSkill;

    @Null(groups = UpdateMemberSkill.class)
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
