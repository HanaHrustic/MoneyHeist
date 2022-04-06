package ag04.project.moneyheist.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    private String sex;
    private String email;

    @OneToMany(mappedBy = "member")
    private Set<MemberSkill> memberSkill = new HashSet<>();

    @ManyToOne(optional = true)
    @JoinColumn(name = "main_skill")
    private Skill mainSkill;

    @Enumerated(EnumType.STRING)
    private MemberStatus memberStatus;

    public Member(Long id, String name, String sex, String email, Set<MemberSkill> memberSkill, Skill mainSkill, MemberStatus memberStatus) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.email = email;
        this.memberSkill = memberSkill;
        this.mainSkill = mainSkill;
        this.memberStatus = memberStatus;
    }

    public Member() {

    }

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

    public Skill getMainSkill() {
        return mainSkill;
    }

    public void setMainSkill(Skill mainSkill) {
        this.mainSkill = mainSkill;
    }

    public MemberStatus getMemberStatus() {
        return memberStatus;
    }

    public void setMemberStatus(MemberStatus memberStatus) {
        this.memberStatus = memberStatus;
    }

    public Set<MemberSkill> getMemberSkill() {
        return memberSkill;
    }

    public void setMemberSkill(Set<MemberSkill> skills) {
        this.memberSkill = skills;
    }
}
