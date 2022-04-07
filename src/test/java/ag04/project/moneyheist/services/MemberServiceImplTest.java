package ag04.project.moneyheist.services;

import ag04.project.moneyheist.api.command.MemberCommand;
import ag04.project.moneyheist.api.command.SkillCommand;
import ag04.project.moneyheist.api.converter.MemberCommandToMember;
import ag04.project.moneyheist.api.converter.MemberToMemberDTO;
import ag04.project.moneyheist.domain.Member;
import ag04.project.moneyheist.domain.MemberSkill;
import ag04.project.moneyheist.domain.MemberStatus;
import ag04.project.moneyheist.domain.Skill;
import ag04.project.moneyheist.repositories.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class MemberServiceImplTest {

    MemberServiceImpl memberService;

    @Mock
    MemberRepository memberRepository;

    @Mock
    MemberCommandToMember memberCommandToMember;

    @Mock
    MemberToMemberDTO memberToMemberDTO;

    @Mock
    SkillService skillService;

    @Mock
    MemberSkillService memberSkillService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        memberService = new MemberServiceImpl(memberRepository, memberCommandToMember, memberToMemberDTO, skillService, memberSkillService);
    }

    @Test
    void addMember() {
        SkillCommand skillCommand = new SkillCommand();
        skillCommand.setName("combat");
        skillCommand.setLevel("****");
        MemberCommand memberCommand = new MemberCommand();
        memberCommand.setMainSkill("combat");
        memberCommand.setSkills(Collections.singletonList(skillCommand));
        MemberSkill memberSkill = new MemberSkill();
        Skill skill = new Skill();
        skill.setName("shooting");
        memberSkill.setId(1L);
        memberSkill.setSkill(skill);
        memberSkill.setSkillLevel("*******");
        when(memberCommandToMember.convert(any())).thenReturn(new Member(1L, "name", "F", "gmail@gmail.com", Collections.singleton(memberSkill), skill, MemberStatus.valueOf("AVAILABLE")));

        memberService.addMember(memberCommand);

        verify(memberRepository, times(1)).save(any());
        verify(memberSkillService, times(1)).save(any(), any());
        verify(skillService, times(1)).saveAllSkills(any());
        verify(skillService, times(1)).findSkillByName(any());
    }

    @Test
    void isDuplicateEmail() {
        memberService.isDuplicateEmail("gmail@gmail.com");

        verify(memberRepository, times(1)).existsByEmail("gmail@gmail.com");
    }
}