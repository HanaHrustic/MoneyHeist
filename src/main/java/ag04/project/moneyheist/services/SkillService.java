package ag04.project.moneyheist.services;

import ag04.project.moneyheist.domain.Skill;

import java.util.List;
import java.util.Optional;

public interface SkillService {
    Optional<Skill> findSkillByName(String name);

    List<Skill> saveAllSkills(List<Skill> skills);
}
