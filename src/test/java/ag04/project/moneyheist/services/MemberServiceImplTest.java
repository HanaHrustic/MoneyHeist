package ag04.project.moneyheist.services;

import ag04.project.moneyheist.Mail.EmailService;
import ag04.project.moneyheist.api.command.MemberCommand;
import ag04.project.moneyheist.api.command.SkillCommand;
import ag04.project.moneyheist.api.converter.MemberCommandToMember;
import ag04.project.moneyheist.api.converter.MemberToMemberDTO;
import ag04.project.moneyheist.domain.*;
import ag04.project.moneyheist.repositories.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

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

    @Mock
    EmailService emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        memberService = new MemberServiceImpl(memberRepository, memberCommandToMember, memberToMemberDTO, skillService, memberSkillService, emailService);
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
        when(memberCommandToMember.convert(any())).thenReturn(new Member(1L, "name", "F", "gmail@gmail.com", Collections.singleton(memberSkill), skill, MemberStatus.valueOf("AVAILABLE"), null));

        memberService.addMember(memberCommand);

        verify(memberRepository, times(1)).save(any());
        verify(memberSkillService, times(1)).save(any());
        verify(skillService, times(1)).saveAllSkills(any());
        verify(skillService, times(1)).findSkillByName(any());
    }

    @Test
    void isDuplicateEmail() {
        memberService.isDuplicateEmail("gmail@gmail.com");

        verify(memberRepository, times(1)).existsByEmail("gmail@gmail.com");
    }

    @Test
    void getPossibleOutcome() {
        memberService.getPossibleOutcome(Optional.of(new Heist(null, null, null, null, null, List.of(new HeistSkill(), new HeistSkill()), null, new HashSet<MemberHeist>(List.of(new MemberHeist(new Member(1L, null, null, null, null, null, null, null), new Heist()))), null)), 1f, 1f);

        verify(memberRepository, times(1)).saveAll(any());
    }
}