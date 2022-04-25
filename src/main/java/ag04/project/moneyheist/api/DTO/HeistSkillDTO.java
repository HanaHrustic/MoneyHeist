package ag04.project.moneyheist.api.DTO;

import ag04.project.moneyheist.api.view.GetHeist;
import ag04.project.moneyheist.api.view.ReadEligibleMembers;
import com.fasterxml.jackson.annotation.JsonView;

public class HeistSkillDTO {
    @JsonView({ReadEligibleMembers.class, GetHeist.class})
    private String name;
    @JsonView({ReadEligibleMembers.class, GetHeist.class})
    private String level;
    @JsonView({ReadEligibleMembers.class, GetHeist.class})
    private Long members;

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
