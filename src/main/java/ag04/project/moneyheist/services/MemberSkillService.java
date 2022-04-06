package ag04.project.moneyheist.services;

import ag04.project.moneyheist.domain.Member;
import ag04.project.moneyheist.domain.Skill;

import java.util.List;

public interface MemberSkillService {

    void save(Member member, List<Skill> skills);
}
