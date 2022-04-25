package ag04.project.moneyheist.domain;

import javax.persistence.*;

@Entity
public class MemberHeist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    private Member member;

    @ManyToOne()
    private Heist heist;

    public MemberHeist(Member member, Heist heist) {
        this.member = member;
        this.heist = heist;
    }

    public MemberHeist() {
    }

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

    public Heist getHeist() {
        return heist;
    }

    public void setHeist(Heist heist) {
        this.heist = heist;
    }
}
