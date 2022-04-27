package ag04.project.moneyheist.services;

import ag04.project.moneyheist.Mail.EmailService;
import ag04.project.moneyheist.api.DTO.MemberDTO;
import ag04.project.moneyheist.api.command.MemberCommand;
import ag04.project.moneyheist.api.converter.MemberCommandToMember;
import ag04.project.moneyheist.api.converter.MemberToMemberDTO;
import ag04.project.moneyheist.domain.*;
import ag04.project.moneyheist.exceptions.BadRequest;
import ag04.project.moneyheist.exceptions.EntityNotFound;
import ag04.project.moneyheist.repositories.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberCommandToMember memberCommandToMember;
    private final MemberToMemberDTO memberToMemberDTO;
    private final SkillService skillService;
    private final MemberSkillService memberSkillService;
    private final EmailService emailService;

    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository, MemberCommandToMember memberCommandToMember, MemberToMemberDTO memberToMemberDTO, SkillService skillService, MemberSkillService memberSkillService, EmailService emailService) {
        this.memberRepository = memberRepository;
        this.memberCommandToMember = memberCommandToMember;
        this.memberToMemberDTO = memberToMemberDTO;
        this.skillService = skillService;
        this.memberSkillService = memberSkillService;
        this.emailService = emailService;
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

        memberSkillService.saveAllMemberSkill(savedMember);

        emailService.sendSimpleMessage(savedMember.getEmail(), "ADDED TO DATABASE",
                "You have been added to participate in a possible future heists.");

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

            memberSkillService.saveAllMemberSkill(existingMember.get());

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
    public List<Member> getAllMembersFromHeist(Long heistId) {
        return memberRepository.findMembersByHeistId(heistId);
    }


    @Override
    public List<Member> getAllMembersFromHeistSkill(List<HeistSkill> skills) {
        return memberRepository.findByMemberBySkillNameIn(skills.stream().map(skill -> skill.getSkill().getName()).collect(Collectors.toList()));
    }

    @Override
    public List<Member> findAllByNames(List<String> names) {
        return memberRepository.findByNameIn(names);
    }

    @Override
    public void getPossibleOutcome(Optional<Heist> heistById, Float requiredMembers, Float numberOfMembers) {
        if ((numberOfMembers / requiredMembers < 1) && (numberOfMembers / requiredMembers >= 0.75)) {
            List<Member> membersToUpdate = getMembersToUpdateStatus(heistById, numberOfMembers, 1f);
            membersToUpdate = membersToUpdate.stream()
                    .peek(member -> member.setMemberStatus(MemberStatus.INCARCERATED)).collect(Collectors.toList());
            memberRepository.saveAll(membersToUpdate);
        } else if ((numberOfMembers / requiredMembers < 0.75) && (numberOfMembers / requiredMembers >= 0.50)) {
            if (new Random().nextDouble() <= 0.50) {
                List<Member> membersToUpdate = getMembersToUpdateStatus(heistById, numberOfMembers, 1f);
                membersToUpdate = membersToUpdate.stream()
                        .peek(this::setStatusEitherExpiredOrIncarcerated).toList();
                memberRepository.saveAll(membersToUpdate);
            } else {
                List<Member> membersToUpdate = getMembersToUpdateStatus(heistById, numberOfMembers, 2f);
                membersToUpdate = membersToUpdate.stream()
                        .peek(this::setStatusEitherExpiredOrIncarcerated).toList();
                memberRepository.saveAll(membersToUpdate);
            }
        } else if ((numberOfMembers / requiredMembers < 0.50)) {
            List<Member> membersToUpdate = heistById.get().getMemberHeists().stream()
                    .map(MemberHeist::getMember)
                    .peek(this::setStatusEitherExpiredOrIncarcerated).toList();
            memberRepository.saveAll(membersToUpdate);
        }
    }

    private void setStatusEitherExpiredOrIncarcerated(Member memberToUpdate) {
        if (new Random().nextDouble() <= 0.50) {
            memberToUpdate.setMemberStatus(MemberStatus.INCARCERATED);
        } else {
            memberToUpdate.setMemberStatus(MemberStatus.EXPIRED);
        }
    }

    private List<Member> getMembersToUpdateStatus(Optional<Heist> heistById, Float numberOfMembers, Float num) {
        double numToIncarcerate = Math.ceil(numberOfMembers * (num / 3f));
        List<Long> memberIds = heistById.get().getMemberHeists().stream().map(MemberHeist::getMember).map(Member::getId).collect(Collectors.toCollection(LinkedList::new));
        List<Long> memberIdsToRemove = new ArrayList<>();
        memberIdsToRemove = removeMemberIDFromList(memberIds, memberIdsToRemove, (int) numToIncarcerate);
        List<Long> finalMemberIdsToRemove = memberIdsToRemove;
        return heistById.get().getMemberHeists().stream()
                .map(MemberHeist::getMember)
                .filter(member -> finalMemberIdsToRemove.contains(member.getId())).toList();
    }

    public List<Long> removeMemberIDFromList(List<Long> memberIds, List<Long> memberIdsToRemove, Integer numToIncarcerate) {
        Random rand = new Random();
        if (memberIds.size() > 0) {
            if (numToIncarcerate-- > 0) {
                int randomIndex = rand.nextInt(memberIds.size());
                memberIdsToRemove.add(memberIds.get(randomIndex));
                memberIds.remove(randomIndex);
                removeMemberIDFromList(memberIds, memberIdsToRemove, numToIncarcerate);
            }
        }
        return memberIdsToRemove;
    }
}
