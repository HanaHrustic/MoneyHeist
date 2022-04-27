package ag04.project.moneyheist.services;

import ag04.project.moneyheist.domain.Member;
import ag04.project.moneyheist.domain.MemberSkill;

import java.util.List;

public interface MemberSkillService {

    void saveAllMemberSkill(Member member);

    void deleteById(Long id);

    MemberSkill findBySkillId(Long skillId);

    void saveAll(List<MemberSkill> memberSkill);
}
