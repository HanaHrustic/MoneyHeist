package ag04.project.moneyheist.services;

import ag04.project.moneyheist.domain.Member;
import ag04.project.moneyheist.domain.MemberSkill;
import ag04.project.moneyheist.repositories.MemberSkillRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


class MemberSkillServiceImplTest {

    MemberSkillService memberSkillService;

    @Mock
    MemberSkillRepository memberSkillRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        memberSkillService = new MemberSkillServiceImpl(memberSkillRepository);
    }

    @Test
    void saveAllMemberSkill() {
        Member member = new Member();
        memberSkillService.saveAllMemberSkill(member);

        verify(memberSkillRepository, times(1)).saveAll(any());
    }

    @Test
    void deleteById() {
        memberSkillService.deleteById(1L);

        verify(memberSkillRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void findBySkillId() {
        memberSkillService.findBySkillId(1L);

        verify(memberSkillRepository, times(1)).findAll();
    }

    @Test
    void saveAll() {
        List<MemberSkill> memberSkills = new ArrayList<>();
        memberSkillService.saveAll(memberSkills);

        verify(memberSkillRepository, times(1)).saveAll(any());
    }
}