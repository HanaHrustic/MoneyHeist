package ag04.project.moneyheist.api.DTO;


import ag04.project.moneyheist.api.view.ReadEligibleMembers;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.List;

public class EligibleMembersDTO {
    @JsonView(ReadEligibleMembers.class)
    private List<HeistSkillDTO> skills;
    @JsonView(ReadEligibleMembers.class)
    private List<MemberDTO> members;

    public List<HeistSkillDTO> getSkills() {
        return skills;
    }

    public void setSkills(List<HeistSkillDTO> skills) {
        this.skills = skills;
    }

    public List<MemberDTO> getMembers() {
        return members;
    }

    public void setMembers(List<MemberDTO> members) {
        this.members = members;
    }
}
