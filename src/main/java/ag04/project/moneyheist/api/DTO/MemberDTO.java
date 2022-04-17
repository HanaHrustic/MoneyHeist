package ag04.project.moneyheist.api.DTO;

import ag04.project.moneyheist.api.view.GetMember;
import ag04.project.moneyheist.api.view.GetMemberSkills;
import ag04.project.moneyheist.api.view.ReadEligibleMembers;
import ag04.project.moneyheist.domain.MemberStatus;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.List;

public class MemberDTO {
    private Long id;

    @JsonView({ReadEligibleMembers.class, GetMember.class})
    private String name;

    @JsonView(GetMember.class)
    private String sex;

    @JsonView(GetMember.class)
    private String email;

    @JsonView({ReadEligibleMembers.class, GetMember.class, GetMemberSkills.class})
    private List<MemberSkillDTO> skills;

    @JsonView({GetMember.class, GetMemberSkills.class})
    private String mainSkill;

    @JsonView(GetMember.class)
    private MemberStatus status;

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

    public List<MemberSkillDTO> getSkills() {
        return skills;
    }

    public void setSkills(List<MemberSkillDTO> skills) {
        this.skills = skills;
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

    public String getMainSkill() {
        return mainSkill;
    }

    public void setMainSkill(String mainSkill) {
        this.mainSkill = mainSkill;
    }

    public MemberStatus getStatus() {
        return status;
    }

    public void setStatus(MemberStatus status) {
        this.status = status;
    }
}
