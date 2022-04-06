package ag04.project.moneyheist.services;

import ag04.project.moneyheist.api.DTO.MemberDTO;
import ag04.project.moneyheist.api.command.MemberCommand;
import ag04.project.moneyheist.api.converter.MemberCommandToMember;
import ag04.project.moneyheist.api.converter.MemberToMemberDTO;
import ag04.project.moneyheist.domain.Member;
import ag04.project.moneyheist.domain.MemberSkill;
import ag04.project.moneyheist.domain.Skill;
import ag04.project.moneyheist.repositories.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberCommandToMember memberCommandToMember;
    private final MemberToMemberDTO memberToMemberDTO;
    private final SkillService skillService;
    private final MemberSkillService memberSkillService;

    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository, MemberCommandToMember memberCommandToMember, MemberToMemberDTO memberToMemberDTO, SkillService skillService, MemberSkillService memberSkillService) {
        this.memberRepository = memberRepository;
        this.memberCommandToMember = memberCommandToMember;
        this.memberToMemberDTO = memberToMemberDTO;
        this.memberSkillService = memberSkillService;
        this.skillService = skillService;
    }

    @Override
    @Transactional
    public MemberDTO addMember(MemberCommand memberCommand) {
        Member memberToSave = memberCommandToMember.convert(memberCommand);

        List<Skill> savedSkills = skillService.saveAllSkills(memberToSave.getMemberSkill().stream().map(MemberSkill::getSkill).collect(Collectors.toList()));

        if(memberToSave.getMainSkill() != null){
            Optional<Skill> skillByName = skillService.findSkillByName(memberToSave.getMainSkill().getName());
            skillByName.ifPresent(memberToSave::setMainSkill);
        }

        memberToSave.setMemberSkill(memberToSave.getMemberSkill().stream().peek(
                memberSkill -> memberSkill.setSkill(
                        savedSkills.stream()
                                .filter(skill -> skill.getName().equals(memberSkill.getSkill().getName()))
                                .findFirst()
                                .orElse(null))).collect(Collectors.toSet()));

        Member savedMember = memberRepository.save(memberToSave);

        memberSkillService.save(savedMember, savedSkills);

        return memberToMemberDTO.convert(savedMember);
    }
}
