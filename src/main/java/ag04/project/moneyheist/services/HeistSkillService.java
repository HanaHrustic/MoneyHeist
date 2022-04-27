package ag04.project.moneyheist.services;

import ag04.project.moneyheist.domain.Heist;
import ag04.project.moneyheist.domain.HeistSkill;

import java.util.List;

public interface HeistSkillService {
    void saveAllHeistSkills(Heist heist);

    List<HeistSkill> getAllSkillsFromHeist(Long heistId);
}
