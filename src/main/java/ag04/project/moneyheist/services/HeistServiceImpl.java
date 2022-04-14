package ag04.project.moneyheist.services;

import ag04.project.moneyheist.api.DTO.HeistDTO;
import ag04.project.moneyheist.api.command.HeistCommand;
import ag04.project.moneyheist.api.converter.HeistCommandToHeist;
import ag04.project.moneyheist.api.converter.HeistToHeistDTO;
import ag04.project.moneyheist.domain.Heist;
import ag04.project.moneyheist.domain.HeistSkill;
import ag04.project.moneyheist.domain.Skill;
import ag04.project.moneyheist.exceptions.ActionNotFound;
import ag04.project.moneyheist.exceptions.EntityNotFound;
import ag04.project.moneyheist.repositories.HeistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class HeistServiceImpl implements HeistService {
    private final HeistRepository heistRepository;
    private final HeistCommandToHeist heistCommandToHeist;
    private final HeistToHeistDTO heistToHeistDTO;
    private final SkillService skillService;
    private final HeistSkillService heistSkillService;

    @Autowired
    public HeistServiceImpl(HeistRepository heistRepository, HeistCommandToHeist heistCommandToHeist, HeistToHeistDTO heistToHeistDTO, SkillService skillService, HeistSkillService heistSkillService) {
        this.heistRepository = heistRepository;
        this.heistCommandToHeist = heistCommandToHeist;
        this.heistToHeistDTO = heistToHeistDTO;
        this.skillService = skillService;
        this.heistSkillService = heistSkillService;
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

        Heist savedHeist = heistRepository.save(heistToSave);

        heistSkillService.save(savedHeist);

        return heistToHeistDTO.convert(savedHeist);
    }

    @Override
    public void updateHeistSkills(HeistCommand heistCommand, Long memberId) {
        Heist heistToUpdate = heistCommandToHeist.convert(heistCommand);

        Optional<Heist> existingHeist = heistRepository.findById(memberId);

        if (existingHeist.isPresent()) {
            if (existingHeist.get().getStartTime().isAfter(LocalDateTime.now())) {
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

                heistSkillService.save(existingHeist.get());
            } else {
                throw new ActionNotFound("Heist has already begun!");
            }
        } else {
            throw new EntityNotFound("Heist does not exist!");
        }
    }

    @Override
    public boolean isDuplicateName(String name) {
        return heistRepository.existsByName(name);
    }
}
