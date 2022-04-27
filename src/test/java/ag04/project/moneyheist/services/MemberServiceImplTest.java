package ag04.project.moneyheist.services;

import ag04.project.moneyheist.Mail.EmailService;
import ag04.project.moneyheist.api.command.MemberCommand;
import ag04.project.moneyheist.api.command.SkillCommand;
import ag04.project.moneyheist.api.converter.MemberCommandToMember;
import ag04.project.moneyheist.api.converter.MemberToMemberDTO;
import ag04.project.moneyheist.domain.*;
import ag04.project.moneyheist.exceptions.BadRequest;
import ag04.project.moneyheist.exceptions.EntityNotFound;
import ag04.project.moneyheist.repositories.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
        when(memberRepository.save(any(Member.class))).thenReturn(new Member(1L, "name", "F", "gmail@gmail.com", Collections.singleton(memberSkill), skill, MemberStatus.valueOf("AVAILABLE"), null));

        memberService.addMember(memberCommand);

        verify(memberRepository, times(1)).save(any());
        verify(memberSkillService, times(1)).saveAllMemberSkill(any());
        verify(skillService, times(1)).saveAllSkills(any());
        verify(skillService, times(1)).findSkillByName(any());
    }

    @Test
    void isDuplicateEmail() {
        memberService.isDuplicateEmail("gmail@gmail.com");

        verify(memberRepository, times(1)).existsByEmail("gmail@gmail.com");
    }

    @Test
    void getPossibleOutcomeCaseFour() {
        MemberHeist memberHeist = new MemberHeist();
        memberHeist.setMember(new Member());
        Heist heist = new Heist(1L, "heist", "location", LocalDateTime.now().minusMinutes(2), LocalDateTime.now().minusMinutes(1), List.of(new HeistSkill()), HeistStatus.FINISHED, new HashSet<>(List.of(memberHeist)), null);

        memberService.getPossibleOutcome(Optional.of(heist), 4f, 3f);

        verify(memberRepository, times(1)).saveAll(any());
    }

    @Test
    void getPossibleOutcomeCaseOne() {
        MemberHeist memberHeist = new MemberHeist();
        memberHeist.setMember(new Member());
        Heist heist = new Heist(1L, "heist", "location", LocalDateTime.now().minusMinutes(2), LocalDateTime.now().minusMinutes(1), List.of(new HeistSkill()), HeistStatus.FINISHED, new HashSet<>(List.of(memberHeist)), null);

        memberService.getPossibleOutcome(Optional.of(heist), 4f, 1f);

        verify(memberRepository, times(1)).saveAll(any());
    }

    @Test
    void findAllByNames() {
        String name = "name";
        memberService.findAllByNames(List.of(name));

        verify(memberRepository, times(1)).findByNameIn(List.of(name));
    }

    @Test
    void getAllMembersFromHeistSkill() {
        HeistSkill heistSkill = new HeistSkill();
        Skill skill = new Skill(1L, "skill", null, null);
        heistSkill.setSkill(skill);
        memberService.getAllMembersFromHeistSkill(List.of(heistSkill));

        verify(memberRepository, times(1)).findByMemberBySkillNameIn(anyList());
    }

    @Test
    void getAllMembersFromHeist() {
        memberService.getAllMembersFromHeist(1L);

        verify(memberRepository, times(1)).findMembersByHeistId(anyLong());
    }

    @Test
    void getMemberSkills() {
        Member member = new Member(1L, "name", "F", "name@gmail.com", null, null, MemberStatus.AVAILABLE, null);

        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));

        memberService.getMemberSkills(1L);

        verify(memberRepository, times(1)).findById(anyLong());
        verify(memberToMemberDTO, times(1)).convert(any());
    }

    @Test
    void getMemberSkillsThrowsEntityNotFound() {
        assertThrows(EntityNotFound.class, () -> {
            when(memberRepository.findById(anyLong())).thenReturn(Optional.empty());

            memberService.getMemberSkills(1L);
        });
    }

    @Test
    void getMemberById() {
        Member member = new Member(1L, "name", "F", "name@gmail.com", null, null, MemberStatus.AVAILABLE, null);

        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));

        memberService.getMemberById(1L);

        verify(memberRepository, times(1)).findById(anyLong());
        verify(memberToMemberDTO, times(1)).convert(any());
    }

    @Test
    void getMemberByIdThrowsEntityNotFound() {
        assertThrows(EntityNotFound.class, () -> {
            when(memberRepository.findById(anyLong())).thenReturn(Optional.empty());

            memberService.getMemberById(1L);
        });
    }

    @Test
    void deleteMemberSkill() {
        Member member = new Member(1L, "name", "F", "name@gmail.com", null, null, MemberStatus.AVAILABLE, null);
        Skill skill = new Skill(1L, "name", null, null);
        MemberSkill memberSkill = new MemberSkill();
        memberSkill.setId(1L);

        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
        when(skillService.findSkillByName(anyString())).thenReturn(Optional.of(skill));
        when(memberSkillService.findBySkillId(anyLong())).thenReturn(memberSkill);

        memberService.deleteMemberSkill(1L, "skillName");

        verify(memberRepository, times(1)).findById(anyLong());
        verify(skillService, times(1)).findSkillByName(anyString());
        verify(memberSkillService, times(1)).findBySkillId(anyLong());
        verify(memberSkillService, times(1)).deleteById(anyLong());
    }

    @Test
    void deleteMemberSkillThrowsEntityNotFound() {
        assertThrows(EntityNotFound.class, () -> {
            when(memberRepository.findById(anyLong())).thenReturn(Optional.empty());

            memberService.deleteMemberSkill(1L, "skillName");
        });
    }

    @Test
    void updateMember() {
        MemberCommand memberCommand = new MemberCommand();
        Skill driving = new Skill(1L, "driving", null, null);
        Skill combat = new Skill(2L, "combat", null, null);
        MemberSkill drivingSkill = new MemberSkill();
        drivingSkill.setSkill(driving);
        drivingSkill.setSkillLevel("*");
        MemberSkill combatSkill = new MemberSkill();
        combatSkill.setSkill(combat);
        combatSkill.setSkillLevel("*");
        MemberSkill drivingSkill2 = new MemberSkill();
        drivingSkill2.setSkill(driving);
        drivingSkill2.setSkillLevel("**");
        Member member = new Member(1L, "name", "F", "name@gmail.com", new HashSet<>(List.of(drivingSkill)), null, MemberStatus.AVAILABLE, null);
        Member memberToUpdate = new Member(2L, "name", "F", "name@gmail.com", new HashSet<>(List.of(drivingSkill2, combatSkill)), combat, MemberStatus.AVAILABLE, null);


        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
        when(skillService.saveAllSkills(any())).thenReturn(List.of(driving, combat));
        when(memberCommandToMember.convert(any())).thenReturn(memberToUpdate);

        memberService.updateMember(memberCommand, 1L);

        assertTrue(member.getMemberSkill().contains(drivingSkill2));
        assertTrue(member.getMemberSkill().contains(combatSkill));
    }

    @Test
    void updateMemberThrowsEntityNotFound() {
        assertThrows(EntityNotFound.class, () -> {
            MemberCommand memberCommand = new MemberCommand();
            Member member = new Member(1L, "name", "F", "name@gmail.com", null, null, MemberStatus.AVAILABLE, null);

            when(memberCommandToMember.convert(memberCommand)).thenReturn(member);
            when(memberRepository.findById(anyLong())).thenReturn(Optional.empty());

            memberService.updateMember(memberCommand, 1L);
        });
    }

    @Test
    void updateMemberThrowsBadRequest() {
        assertThrows(BadRequest.class, () -> {
            MemberCommand memberCommand = new MemberCommand();
            Skill driving = new Skill(1L, "driving", null, null);
            Skill combat = new Skill(2L, "combat", null, null);
            MemberSkill drivingSkill = new MemberSkill();
            drivingSkill.setSkill(driving);
            drivingSkill.setSkillLevel("*");
            MemberSkill combatSkill = new MemberSkill();
            combatSkill.setSkill(combat);
            combatSkill.setSkillLevel("*");
            MemberSkill drivingSkill2 = new MemberSkill();
            drivingSkill2.setSkill(driving);
            drivingSkill2.setSkillLevel("**");
            Member member = new Member(1L, "name", "F", "name@gmail.com", new HashSet<>(List.of(drivingSkill)), new Skill(), MemberStatus.AVAILABLE, null);
            Member memberToUpdate = new Member(2L, "name", "F", "name@gmail.com", new HashSet<>(List.of(drivingSkill2, combatSkill)), new Skill(), MemberStatus.AVAILABLE, null);

            when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
            when(skillService.saveAllSkills(any())).thenReturn(List.of(driving, combat));
            when(memberCommandToMember.convert(any())).thenReturn(memberToUpdate);

            memberService.updateMember(memberCommand, 1L);
        });
    }
}