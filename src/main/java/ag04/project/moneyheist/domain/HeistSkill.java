package ag04.project.moneyheist.domain;

import javax.persistence.*;

@Entity
public class HeistSkill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String level;
    private Long members;

    @ManyToOne()
    private Heist heist;

    @ManyToOne()
    private Skill skill;

    public HeistSkill(Long id, String level, Long members, Heist heist, Skill skill) {
        this.id = id;
        this.level = level;
        this.members = members;
        this.heist = heist;
        this.skill = skill;
    }

    public HeistSkill() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Heist getHeist() {
        return heist;
    }

    public void setHeist(Heist heist) {
        this.heist = heist;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }
}
