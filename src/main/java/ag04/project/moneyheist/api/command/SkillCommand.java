package ag04.project.moneyheist.api.command;

import javax.validation.constraints.Pattern;

public class SkillCommand {

    private String name;

    @Pattern(regexp = "^[*]{1,10}$")
    private String level;

    public SkillCommand(String name, String level) {
        this.name = name;
        this.level = level;
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
}
