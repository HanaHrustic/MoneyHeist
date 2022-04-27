package ag04.project.moneyheist.services;

import ag04.project.moneyheist.domain.Heist;
import ag04.project.moneyheist.domain.HeistSkill;
import ag04.project.moneyheist.repositories.HeistSkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HeistSkillServiceImpl implements HeistSkillService {
    private final HeistSkillRepository heistSkillRepository;

    @Autowired
    public HeistSkillServiceImpl(HeistSkillRepository heistSkillRepository) {
        this.heistSkillRepository = heistSkillRepository;
    }

    @Override
    public void saveAllHeistSkills(Heist heist) {
        heistSkillRepository.saveAll(heist.getHeistSkills());
    }

    @Override
    public List<HeistSkill> getAllSkillsFromHeist(Long heistId) {
        return heistSkillRepository.findSkillsByHeistId(heistId);
    }
}
