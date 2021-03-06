package ag04.project.moneyheist.services;

import ag04.project.moneyheist.Mail.EmailService;
import ag04.project.moneyheist.api.DTO.EligibleMembersDTO;
import ag04.project.moneyheist.api.DTO.HeistDTO;
import ag04.project.moneyheist.api.DTO.HeistSkillDTO;
import ag04.project.moneyheist.api.DTO.MemberDTO;
import ag04.project.moneyheist.api.command.HeistCommand;
import ag04.project.moneyheist.api.converter.HeistCommandToHeist;
import ag04.project.moneyheist.api.converter.HeistSkillToHeistSkillDTO;
import ag04.project.moneyheist.api.converter.HeistToHeistDTO;
import ag04.project.moneyheist.api.converter.MemberToMemberDTO;
import ag04.project.moneyheist.domain.*;
import ag04.project.moneyheist.exceptions.ActionNotFound;
import ag04.project.moneyheist.exceptions.BadRequest;
import ag04.project.moneyheist.exceptions.EntityNotFound;
import ag04.project.moneyheist.repositories.HeistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class HeistServiceImpl implements HeistService {
    private final HeistRepository heistRepository;
    private final HeistCommandToHeist heistCommandToHeist;
    private final HeistToHeistDTO heistToHeistDTO;
    private final SkillService skillService;
    private final HeistSkillService heistSkillService;
    private final MemberService memberService;
    private final HeistSkillToHeistSkillDTO heistSkillToHeistSkillDTO;
    private final MemberToMemberDTO memberToMemberDTO;
    private final MemberHeistService memberHeistService;
    private final EmailService emailService;
    private final MemberSkillService memberSkillService;
    private final Integer levelUpTime;

    @Autowired
    public HeistServiceImpl(HeistRepository heistRepository, HeistCommandToHeist heistCommandToHeist, HeistToHeistDTO heistToHeistDTO, SkillService skillService, HeistSkillService heistSkillService, MemberService memberService, HeistSkillToHeistSkillDTO heistSkillToHeistSkillDTO, MemberToMemberDTO memberToMemberDTO, MemberHeistService memberHeistService, EmailService emailService, MemberSkillService memberSkillService, @Value("${skill.levelUpTime}") Integer levelUpTime) {
        this.heistRepository = heistRepository;
        this.heistCommandToHeist = heistCommandToHeist;
        this.heistToHeistDTO = heistToHeistDTO;
        this.skillService = skillService;
        this.heistSkillService = heistSkillService;
        this.memberService = memberService;
        this.heistSkillToHeistSkillDTO = heistSkillToHeistSkillDTO;
        this.memberToMemberDTO = memberToMemberDTO;
        this.memberHeistService = memberHeistService;
        this.emailService = emailService;
        this.memberSkillService = memberSkillService;
        this.levelUpTime = levelUpTime;
    }

    @Override
    public HeistDTO addHeist(HeistCommand heistCommand) {
        Heist heistToSave = heistCommandToHeist.convert(heistCommand);

        List<Skill> savedSkills = skillService.saveAllSkills(heistToSave.getHeistSkills().stream().map(HeistSkill::getSkill).distinct().collect(Collectors.toList()));

        heistToSave.setHeistSkills(heistToSave.getHeistSkills().stream().peek(
                heistSkill -> heistSkill.setSkill(
                        savedSkills.stream()
                                .filter(skill -> skill.getName().equals(heistSkill.getSkill().getName()))
                                .findFirst()
                                .orElse(null))).collect(Collectors.toList()));

        if (heistCommand.getStartTime().isBefore(LocalDateTime.now())) {
            heistToSave.setStatus(HeistStatus.IN_PROGRESS);
        }

        Heist savedHeist = heistRepository.save(heistToSave);

        heistSkillService.saveAllHeistSkills(savedHeist);

        return heistToHeistDTO.convert(savedHeist);
    }

    @Override
    public void updateHeistSkills(HeistCommand heistCommand, Long memberId) {
        Heist heistToUpdate = heistCommandToHeist.convert(heistCommand);

        Optional<Heist> existingHeist = heistRepository.findById(memberId);

        if (existingHeist.isPresent()) {
            if (!existingHeist.get().getStatus().equals(HeistStatus.IN_PROGRESS)) {
                List<Skill> savedSkills = skillService.saveAllSkills(Stream.concat(heistToUpdate.getHeistSkills()
                                .stream().map(HeistSkill::getSkill),
                        existingHeist.get().getHeistSkills().stream().map(HeistSkill::getSkill)).collect(Collectors.toList()));
                Set<HeistSkill> updatedHeistSkills = new HashSet<>();

                existingHeist.get().getHeistSkills().forEach(heistSkill -> {
                    heistToUpdate.getHeistSkills().stream()
                            .filter(h -> h.equals(heistSkill))
                            .findFirst().ifPresent(updatedSkill -> heistSkill.setMembers(updatedSkill.getMembers()));
                    updatedHeistSkills.add(heistSkill);
                });
                updatedHeistSkills.addAll(heistToUpdate.getHeistSkills());

                existingHeist.get().setHeistSkills(updatedHeistSkills.stream()
                        .peek(heistSkill -> {
                            heistSkill.setHeist(existingHeist.get());
                            heistSkill.setSkill(savedSkills.stream().filter(skill -> skill.equals(heistSkill.getSkill()))
                                    .findFirst().orElse(null));
                        }).collect(Collectors.toList()));

                heistSkillService.saveAllHeistSkills(existingHeist.get());
            } else {
                throw new ActionNotFound("Heist has already started!");
            }
        } else {
            throw new EntityNotFound("Heist does not exist!");
        }
    }

    @Override
    public EligibleMembersDTO getEligibleMembers(Long heistId) {
        EligibleMembersDTO eligibleMembersDTO = new EligibleMembersDTO();
        Optional<Heist> heistById = heistRepository.findById(heistId);
        if (heistById.isPresent()) {
            if (!heistById.get().getStatus().equals(HeistStatus.READY)) {
                List<HeistSkill> heistSkills = heistById.get().getHeistSkills();

                eligibleMembersDTO.setSkills(heistSkills.stream().map(heistSkillToHeistSkillDTO::convert).collect(Collectors.toList()));
                List<Member> eligibleMembersToFilter = memberService.getAllMembersFromHeistSkill(heistSkills);

                List<Member> eligibleMembers = filterMembersBySkillAndStatus(heistById, eligibleMembersToFilter);
                eligibleMembers = eligibleMembers.stream().filter(eligibleMember -> {
                    List<Heist> memberHeists = findHeistsForMemberId(eligibleMember.getId());
                    for (Heist heist : memberHeists) {
                        if (!(heist.getStartTime().isAfter(heistById.get().getEndTime()) || heist.getEndTime().isBefore(heistById.get().getStartTime()))) {
                            return false;
                        }
                    }
                    return true;
                }).toList();
                eligibleMembersDTO.setMembers(eligibleMembers.stream().map(memberToMemberDTO::convert).collect(Collectors.toList()));
            } else {
                throw new ActionNotFound("Heist members have already been confirmed!");
            }
        } else {
            throw new EntityNotFound("Heist does not exist!");
        }
        return eligibleMembersDTO;
    }

    @Override
    public void confirmMembersInHeist(HeistCommand heistCommand, Long heistId) {
        Optional<Heist> heistById = heistRepository.findById(heistId);
        if (heistById.isPresent()) {
            if (heistById.get().getStatus().equals(HeistStatus.PLANNING)) {
                List<Member> membersFromCommand = memberService.findAllByNames(heistCommand.getMembers());

                membersFromCommand = filterMembersBySkillAndStatus(heistById, membersFromCommand);
                if (membersFromCommand.size() != heistCommand.getMembers().size()) {
                    throw new BadRequest("Not all members are valid.");
                }
                heistById.get().setMemberHeists(new HashSet<>(memberHeistService.assignMemberToHeist(heistById.get(), membersFromCommand)));
                heistById.get().setStatus(HeistStatus.READY);
                heistRepository.save(heistById.get());
            } else {
                throw new ActionNotFound("Heist status is not PLANNING!");
            }
        } else {
            throw new EntityNotFound("Heist does not exist!");
        }
    }

    @Override
    public void manualStartHeist(Long heistId) {
        Optional<Heist> heistById = heistRepository.findById(heistId);
        if (heistById.isPresent()) {
            if (heistById.get().getStatus().equals(HeistStatus.READY)) {
                heistById.get().setStatus(HeistStatus.IN_PROGRESS);
                List<Member> membersInHeist = heistById.get().getMemberHeists().stream().map(MemberHeist::getMember).toList();
                membersInHeist.forEach(member -> {
                    emailService.sendSimpleMessage(member.getEmail(), "HEIST HAS STARTED!",
                            "Heist you have been assigned to has started! Gear up!");
                });
                heistRepository.save(heistById.get());
            } else {
                throw new ActionNotFound("Heist status is not ready!");
            }
        } else {
            throw new EntityNotFound("Heist does not exist!");
        }
    }

    @Override
    public void manualEndHeist(Long heistId) {
        Optional<Heist> heistById = heistRepository.findById(heistId);
        if (heistById.isPresent()) {
            if (heistById.get().getStatus().equals(HeistStatus.IN_PROGRESS)) {
                heistById.get().setStatus(HeistStatus.FINISHED);
                List<Member> membersInHeist = heistById.get().getMemberHeists().stream().map(MemberHeist::getMember).toList();
                membersInHeist.forEach(member -> {
                    emailService.sendSimpleMessage(member.getEmail(), "HEIST HAS ENDED!",
                            "Heist you have been assigned to has ended!");
                });
                heistRepository.save(heistById.get());
            } else {
                throw new ActionNotFound("Heist status is not IN_PROGRESS!");
            }
        } else {
            throw new EntityNotFound("Heist does not exist!");
        }
    }

    @Override
    public HeistDTO getHeistOutcome(Long heistId) {
        Optional<Heist> heistById = heistRepository.findById(heistId);
        if (heistById.isPresent()) {
            if (heistById.get().getStatus().equals(HeistStatus.FINISHED)) {
                Float requiredMembers = Float.valueOf(heistById.get().getHeistSkills().stream().map(HeistSkill::getMembers).reduce(0L, Long::sum));
                Float numberOfMembers = (float) heistById.get().getMemberHeists().size();
                if (numberOfMembers >= requiredMembers) {
                    heistById.get().setHeistOutcome(HeistOutcome.SUCCEEDED);
                } else if ((numberOfMembers / requiredMembers < 1) && (numberOfMembers / requiredMembers >= 0.75)) {
                    heistById.get().setHeistOutcome(HeistOutcome.SUCCEEDED);
                } else if ((numberOfMembers / requiredMembers < 0.75) && (numberOfMembers / requiredMembers >= 0.50)) {
                    if (new Random().nextDouble() <= 0.50) {
                        heistById.get().setHeistOutcome(HeistOutcome.SUCCEEDED);
                    } else {
                        heistById.get().setHeistOutcome(HeistOutcome.FAILED);
                    }
                } else if ((numberOfMembers / requiredMembers < 0.50)) {
                    heistById.get().setHeistOutcome(HeistOutcome.FAILED);
                }
                memberService.getPossibleOutcome(heistById, requiredMembers, numberOfMembers);
                heistRepository.save(heistById.get());
            } else {
                throw new ActionNotFound("Heist status is not FINISHED.");
            }
        } else {
            throw new EntityNotFound("Heist does not exist!");
        }
        return heistToHeistDTO.convert(heistById.get());
    }

    @Override
    public List<Heist> findHeistsForMemberId(Long memberId) {
        return heistRepository.findAllByMemberId(memberId);
    }

    @Override
    public HeistDTO getHeistById(Long heistId) {
        Optional<Heist> heistById = heistRepository.findById(heistId);
        if (heistById.isPresent()) {
            return heistToHeistDTO.convert(heistById.get());
        } else {
            throw new EntityNotFound("Heist does not exist!");
        }
    }

    @Override
    public List<MemberDTO> getHeistMembers(Long heistId) {
        Optional<Heist> heistById = heistRepository.findById(heistId);
        if (heistById.isPresent()) {
            if (!heistById.get().getStatus().equals(HeistStatus.PLANNING)) {
                List<Member> members = memberService.getAllMembersFromHeist(heistId);
                return members.stream().map(memberToMemberDTO::convert).toList();
            } else {
                throw new ActionNotFound("Heist status is Planning.");
            }
        } else {
            throw new EntityNotFound("Heist does not exist!");
        }
    }

    @Override
    public List<HeistSkillDTO> getHeistSkills(Long heistId) {
        Optional<Heist> heistById = heistRepository.findById(heistId);
        if (heistById.isPresent()) {
            List<HeistSkill> skills = heistSkillService.getAllSkillsFromHeist(heistId);
            return skills.stream().map(heistSkillToHeistSkillDTO::convert).toList();
        } else {
            throw new EntityNotFound("Heist does not exist!");
        }
    }

    @Override
    public HeistDTO getHeistStatus(Long heistId) {
        Optional<Heist> heistById = heistRepository.findById(heistId);
        if (heistById.isPresent()) {
            return heistToHeistDTO.convert(heistById.get());
        } else {
            throw new EntityNotFound("Heist does not exist!");
        }
    }

    @Scheduled(fixedDelay = 1000)
    @Transactional
    @Override
    public void automaticHeistStart() {
        List<Heist> heists = heistRepository.findAllHeists();
        heists.forEach(heist -> {
            if (heist.getStartTime().isBefore(LocalDateTime.now())
                    && heist.getStatus().equals(HeistStatus.READY)
                    && heist.getEndTime().isAfter(LocalDateTime.now())) {
                manualStartHeist(heist.getId());
            } else if (heist.getEndTime().isBefore(LocalDateTime.now())
                    && heist.getStatus().equals(HeistStatus.IN_PROGRESS)) {
                levelUpMembersSkillsInHeist(heist);
                manualEndHeist(heist.getId());
            }
        });
    }

    private void levelUpMembersSkillsInHeist(Heist heist) {
        LocalDateTime heistTime = heist.getEndTime().minusSeconds(heist.getStartTime().getSecond());
        int levelUps = heistTime.getSecond() / levelUpTime;
        List<String> heistSkills = heist.getHeistSkills().stream().map(heistSkill -> heistSkill.getSkill().getName()).toList();
        List<MemberSkill> memberSkillsToLevelUp = heist.getMemberHeists().stream().map(MemberHeist::getMember)
                .map(Member::getMemberSkill).flatMap(Collection::stream)
                .filter(memberSkill -> heistSkills.contains(memberSkill.getSkill().getName())).toList();
        memberSkillsToLevelUp.forEach(memberSkill -> {
            if (memberSkill.getSkillLevel().length() + levelUps < 10) {
                memberSkill.setSkillLevel("*".repeat(memberSkill.getSkillLevel().length() + levelUps));
            } else {
                memberSkill.setSkillLevel("**********");
            }
        });
        memberSkillService.saveAll(memberSkillsToLevelUp);
    }

    private List<Member> filterMembersBySkillAndStatus(Optional<Heist> heistById, List<Member> membersFromCommand) {
        membersFromCommand = membersFromCommand.stream().filter(member -> member.getMemberSkill().stream()
                .anyMatch(memberSkill -> {
                    for (HeistSkill heistSkill : heistById.get().getHeistSkills()) {
                        if (heistSkill.getLevel().length() <= memberSkill.getSkillLevel().length()
                                && heistSkill.getSkill().getName().equals(memberSkill.getSkill().getName())) {
                            return true;
                        }
                    }
                    return false;
                })).filter(member -> member.getMemberStatus().equals(MemberStatus.AVAILABLE)
                || member.getMemberStatus().equals(MemberStatus.RETIRED)).toList();
        return membersFromCommand;
    }

    @Override
    public boolean isDuplicateName(String name) {
        return heistRepository.existsByName(name);
    }
}
