package ag04.project.moneyheist.services;

import ag04.project.moneyheist.domain.Member;
import ag04.project.moneyheist.domain.MemberSkill;

public interface MemberSkillService {

    void save(Member member);

    void deleteById(Long id);

    MemberSkill findBySkillId(Long skillId);
}
