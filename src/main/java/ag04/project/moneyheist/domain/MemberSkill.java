package ag04.project.moneyheist.domain;

import javax.persistence.*;

@Entity
public class MemberSkill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    private Member member;

    @ManyToOne()
    private Skill skill;

    private String skillLevel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public String getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(String skillLevel) {
        this.skillLevel = skillLevel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MemberSkill that = (MemberSkill) o;

        return skill != null ? skill.equals(that.skill) : that.skill == null;
    }

    @Override
    public int hashCode() {
        return skill != null ? skill.hashCode() : 0;
    }
}
