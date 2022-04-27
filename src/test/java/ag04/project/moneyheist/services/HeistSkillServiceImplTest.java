package ag04.project.moneyheist.services;

import ag04.project.moneyheist.domain.Heist;
import ag04.project.moneyheist.repositories.HeistSkillRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class HeistSkillServiceImplTest {
    HeistSkillService heistSkillService;

    @Mock
    HeistSkillRepository heistSkillRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        heistSkillService = new HeistSkillServiceImpl(heistSkillRepository);
    }

    @Test
    void saveAllHeistSKills() {
        Heist heist = new Heist();
        heistSkillService.saveAllHeistSkills(heist);

        verify(heistSkillRepository, times(1)).saveAll(any());
    }

    @Test
    void getAllSkillsFromHeist() {
        heistSkillService.getAllSkillsFromHeist(1L);

        verify(heistSkillRepository, times(1)).findSkillsByHeistId(any());
    }
}