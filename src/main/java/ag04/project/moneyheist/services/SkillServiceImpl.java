package ag04.project.moneyheist.services;

import ag04.project.moneyheist.domain.Skill;
import ag04.project.moneyheist.repositories.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class SkillServiceImpl implements SkillService {

    private final SkillRepository skillRepository;

    @Autowired
    public SkillServiceImpl(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    @Override
    public Optional<Skill> findSkillByName(String name) {
        return skillRepository.findByName(name);
    }

    @Override
    public List<Skill> saveAllSkills(List<Skill> skills) {
        List<Skill> existingSkills = skillRepository.findAllByNameIn(skills.stream().map(Skill::getName).collect(Collectors.toList()));

        List<Skill> list = new ArrayList<>();
        Iterable<Skill> skill = skillRepository.saveAll(skills.stream().filter(skill1 -> !existingSkills.contains(skill1)).collect(Collectors.toList()));
        skill.forEach(list::add);

        return Stream.concat(list.stream(), existingSkills.stream()).toList();
    }
}
