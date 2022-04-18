package ag04.project.moneyheist.services;

import ag04.project.moneyheist.api.DTO.MemberDTO;
import ag04.project.moneyheist.api.command.MemberCommand;
import ag04.project.moneyheist.api.converter.MemberCommandToMember;
import ag04.project.moneyheist.api.converter.MemberToMemberDTO;
import ag04.project.moneyheist.domain.HeistSkill;
import ag04.project.moneyheist.domain.Member;
import ag04.project.moneyheist.domain.MemberSkill;
import ag04.project.moneyheist.domain.Skill;
import ag04.project.moneyheist.exceptions.BadRequest;
import ag04.project.moneyheist.exceptions.EntityNotFound;
import ag04.project.moneyheist.repositories.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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


        if (memberToSave.getMainSkill() != null) {
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

        memberSkillService.save(savedMember);

        return memberToMemberDTO.convert(savedMember);
    }

    @Override
    public boolean isDuplicateEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    @Transactional
    @Override
    public void updateMember(MemberCommand memberCommand, Long memberId) {
        Member memberToUpdate = memberCommandToMember.convert(memberCommand);

        Optional<Member> existingMember = memberRepository.findById(memberId);

        if (existingMember.isPresent()) {
            List<Skill> savedSkills = skillService.saveAllSkills(Stream.concat(memberToUpdate.getMemberSkill()
                            .stream().map(MemberSkill::getSkill),
                    existingMember.get().getMemberSkill().stream().map(MemberSkill::getSkill)).collect(Collectors.toList()));
            Set<MemberSkill> updatedMemberSkills = new HashSet<>();

            existingMember.get().getMemberSkill().forEach(memberSkill -> {
                memberToUpdate.getMemberSkill().stream()
                        .filter(m -> m.getSkill().getName().equals(memberSkill.getSkill().getName()))
                        .findFirst().ifPresent(updatedSkill -> memberSkill.setSkillLevel(updatedSkill.getSkillLevel()));
                updatedMemberSkills.add(memberSkill);
            });
            updatedMemberSkills.addAll(memberToUpdate.getMemberSkill());

            existingMember.get().setMemberSkill(updatedMemberSkills.stream()
                    .peek(memberSkill -> memberSkill.setMember(existingMember.get())).collect(Collectors.toSet()));

            memberSkillService.save(existingMember.get());

            if (savedSkills.contains(memberToUpdate.getMainSkill())) {
                existingMember.get().setMainSkill(savedSkills.stream()
                        .filter(skill -> skill.equals(memberToUpdate.getMainSkill()))
                        .findFirst().orElse(null));
                memberRepository.save(existingMember.get());
            } else if (memberToUpdate.getMainSkill() != null) {
                throw new BadRequest("Main skill is not part of Member Skills!");
            }
        } else {
            throw new EntityNotFound("Member does not exist!");
        }
    }

    @Transactional
    @Override
    public void deleteMemberSkill(Long memberId, String skillName) {
        Optional<Member> existingMember = memberRepository.findById(memberId);
        Optional<Skill> skillToDelete = skillService.findSkillByName(skillName);
        if (existingMember.isPresent() && skillToDelete.isPresent()) {
            MemberSkill memberSkill = memberSkillService.findBySkillId(skillToDelete.get().getId());
            memberSkillService.deleteById(memberSkill.getId());
        } else {
            throw new EntityNotFound("Does not exist!");
        }
    }

    @Override
    public MemberDTO getMemberById(Long memberId) {
        Optional<Member> memberById = memberRepository.findById(memberId);
        if (memberById.isPresent()) {
            return memberToMemberDTO.convert(memberById.get());
        } else {
            throw new EntityNotFound("Member does not exist!");
        }
    }

    @Override
    public MemberDTO getMemberSkills(Long memberId) {
        Optional<Member> memberById = memberRepository.findById(memberId);

        if (memberById.isPresent()) {
            return memberToMemberDTO.convert(memberById.get());
        } else {
            throw new EntityNotFound("Member does not exist!");
        }
    }

    @Override
    public List<Member> getAllMembersFromMemberHeist(Long heistId) {
        return memberRepository.findByHeistId(heistId);
    }


    @Override
    public List<Member> getAllMembersFromHeistSkill(List<HeistSkill> skills) {
        return memberRepository.findByMemberBySkillNameIn(skills.stream().map(skill -> skill.getSkill().getName()).collect(Collectors.toList()));
    }

    @Override
    public List<Member> findAllByNames(List<String> names) {
        return memberRepository.findByNameIn(names);
    }
}
