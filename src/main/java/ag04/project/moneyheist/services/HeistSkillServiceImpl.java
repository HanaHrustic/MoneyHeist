package ag04.project.moneyheist.services;

import ag04.project.moneyheist.repositories.HeistSkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HeistSkillServiceImpl implements HeistSkillService {
    private final HeistSkillRepository heistSkillRepository;

    @Autowired
    public HeistSkillServiceImpl(HeistSkillRepository heistSkillRepository) {
        this.heistSkillRepository = heistSkillRepository;
    }
}
