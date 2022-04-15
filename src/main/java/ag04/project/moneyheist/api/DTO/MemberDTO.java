package ag04.project.moneyheist.api.DTO;

import ag04.project.moneyheist.api.view.ReadEligibleMembers;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.List;

public class MemberDTO {
    private Long id;

    @JsonView(ReadEligibleMembers.class)
    private String name;
    @JsonView(ReadEligibleMembers.class)
    private List<MemberSkillDTO> skills;

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
}
