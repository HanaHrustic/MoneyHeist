package ag04.project.moneyheist.api.DTO;

import ag04.project.moneyheist.api.view.GetMember;
import ag04.project.moneyheist.api.view.GetMemberSkills;
import ag04.project.moneyheist.api.view.ReadEligibleMembers;
import com.fasterxml.jackson.annotation.JsonView;

public class MemberSkillDTO {
    @JsonView({ReadEligibleMembers.class, GetMember.class, GetMemberSkills.class})
    private String name;
    @JsonView({ReadEligibleMembers.class, GetMember.class, GetMemberSkills.class})
    private String level;

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
}
