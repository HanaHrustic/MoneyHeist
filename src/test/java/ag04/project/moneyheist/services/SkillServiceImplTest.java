package ag04.project.moneyheist.services;

import ag04.project.moneyheist.domain.Skill;
import ag04.project.moneyheist.repositories.SkillRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class SkillServiceImplTest {

    SkillService skillService;

    @Mock
    SkillRepository skillRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        skillService = new SkillServiceImpl(skillRepository);
    }

    @Test
    void findSkillByName() {
        skillService.findSkillByName("name");

        verify(skillRepository, times(1)).findByName("name");
    }

    @Test
    void saveAllSkills() {
        Skill skill = new Skill();

        skillService.saveAllSkills(Collections.singletonList(skill));

        verify(skillRepository, times(1)).saveAll(any());
        verify(skillRepository, times(1)).findAllByNameIn(any());
    }
}