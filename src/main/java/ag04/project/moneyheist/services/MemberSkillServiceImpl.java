package ag04.project.moneyheist.services;

import ag04.project.moneyheist.domain.Member;
import ag04.project.moneyheist.domain.MemberSkill;
import ag04.project.moneyheist.repositories.MemberSkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberSkillServiceImpl implements MemberSkillService {

    private final MemberSkillRepository memberSkillRepository;

    @Autowired
    public MemberSkillServiceImpl(MemberSkillRepository memberSkillRepository) {
        this.memberSkillRepository = memberSkillRepository;
    }

    @Override
    public void saveAllMemberSkill(Member member) {
        memberSkillRepository.saveAll(member.getMemberSkill());
    }

    @Override
    public void deleteById(Long id) {
        memberSkillRepository.deleteById(id);
    }

    public MemberSkill findBySkillId(Long skillId) {
        List<MemberSkill> list = memberSkillRepository.findAll();

        return list.stream().filter(memberSkill1 -> memberSkill1.getSkill().getId().equals(skillId)).findFirst().orElse(null);
    }

    @Override
    public void saveAll(List<MemberSkill> memberSkill) {
        memberSkillRepository.saveAll(memberSkill);
    }
}
