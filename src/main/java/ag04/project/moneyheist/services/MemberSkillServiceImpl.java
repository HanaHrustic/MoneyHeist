package ag04.project.moneyheist.services;

import ag04.project.moneyheist.domain.Member;
import ag04.project.moneyheist.domain.Skill;
import ag04.project.moneyheist.repositories.MemberSkillRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberSkillServiceImpl implements MemberSkillService {

    private final MemberSkillRepository memberSkillRepository;

    public MemberSkillServiceImpl(MemberSkillRepository memberSkillRepository) {
        this.memberSkillRepository = memberSkillRepository;
    }

    @Override
    public void save(Member member, List<Skill> skills) {
        memberSkillRepository.saveAll(member.getMemberSkill());
    }
}
