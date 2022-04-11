package ag04.project.moneyheist.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "skill")
    private Set<MemberSkill> memberSkill = new HashSet<>();

    @OneToMany(mappedBy = "skill")
    private Set<HeistSkill> heistSkill = new HashSet<>();

    public Skill(Long id, String name, Set<MemberSkill> memberSkill, Set<HeistSkill> heistSkill) {
        this.id = id;
        this.name = name;
        this.memberSkill = memberSkill;
        this.heistSkill = heistSkill;
    }

    public Skill() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<MemberSkill> getMemberSkill() {
        return memberSkill;
    }

    public void setMemberSkill(Set<MemberSkill> skills) {
        this.memberSkill = skills;
    }

    public Set<HeistSkill> getHeistSkill() {
        return heistSkill;
    }

    public void setHeistSkill(Set<HeistSkill> heistSkill) {
        this.heistSkill = heistSkill;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Skill skill = (Skill) o;

        return name != null ? name.equals(skill.name) : skill.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}