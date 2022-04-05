package ag04.project.moneyheist.api.command;

public class SkillCommand {
    private String name;
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
