package ag04.project.moneyheist.services;

import ag04.project.moneyheist.Mail.EmailService;
import ag04.project.moneyheist.api.DTO.EligibleMembersDTO;
import ag04.project.moneyheist.api.DTO.HeistDTO;
import ag04.project.moneyheist.api.command.HeistCommand;
import ag04.project.moneyheist.api.converter.HeistCommandToHeist;
import ag04.project.moneyheist.api.converter.HeistSkillToHeistSkillDTO;
import ag04.project.moneyheist.api.converter.HeistToHeistDTO;
import ag04.project.moneyheist.api.converter.MemberToMemberDTO;
import ag04.project.moneyheist.domain.*;
import ag04.project.moneyheist.exceptions.ActionNotFound;
import ag04.project.moneyheist.exceptions.EntityNotFound;
import ag04.project.moneyheist.repositories.HeistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class HeistServiceImplTest {

    HeistServiceImpl heistService;

    @Mock
    HeistRepository heistRepository;
    @Mock
    HeistCommandToHeist heistCommandToHeist;
    @Mock
    HeistToHeistDTO heistToHeistDTO;
    @Mock
    SkillService skillService;
    @Mock
    HeistSkillService heistSkillService;
    @Mock
    MemberService memberService;
    @Mock
    HeistSkillToHeistSkillDTO heistSkillToHeistSkillDTO;
    @Mock
    MemberToMemberDTO memberToMemberDTO;
    @Mock
    MemberHeistService memberHeistService;
    @Mock
    EmailService emailService;
    @Mock
    MemberSkillService memberSkillService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        heistService = new HeistServiceImpl(heistRepository, heistCommandToHeist, heistToHeistDTO, skillService, heistSkillService, memberService, heistSkillToHeistSkillDTO, memberToMemberDTO, memberHeistService, emailService, memberSkillService, 86400);
    }

    @Test
    void getHeistOutcomeThrowsEntityNotFound() {
        assertThrows(EntityNotFound.class, () -> {
            when(heistRepository.findById(anyLong())).thenReturn(Optional.empty());

            heistService.getHeistOutcome(1L);

            verify(heistRepository, times(1)).findById(anyLong());
            verify(heistRepository, never()).save(any(Heist.class));
            verify(memberService, never()).getPossibleOutcome(any(), anyFloat(), anyFloat());
        });
    }

    @Test
    void getHeistOutcomeThrowsActionNotFound() {
        assertThrows(ActionNotFound.class, () -> {
            Heist unfinishedHeist = new Heist();
            unfinishedHeist.setStatus(HeistStatus.IN_PROGRESS);

            when(heistRepository.findById(anyLong())).thenReturn(Optional.of(unfinishedHeist));

            heistService.getHeistOutcome(1L);

            verify(heistRepository, times(1)).findById(anyLong());
            verify(heistRepository, never()).save(any(Heist.class));
            verifyNoInteractions(memberService);
        });
    }

    @Test
    void getHeistOutcomeIsValidOutcome() {
        Heist mockHeist = new Heist();
        mockHeist.setStatus(HeistStatus.FINISHED);

        //100%
        mockHeist.setHeistSkills(List.of(new HeistSkill(null, null, 4L, mockHeist, null)));
        mockHeist.setMemberHeists(Set.of(new MemberHeist(), new MemberHeist(), new MemberHeist(), new MemberHeist()));

        when(heistRepository.findById(anyLong())).thenReturn(Optional.of(mockHeist));
        heistService.getHeistOutcome(1L);

        assertEquals(HeistOutcome.SUCCEEDED, mockHeist.getHeistOutcome());

        //75%
        mockHeist.setHeistSkills(List.of(new HeistSkill(null, null, 4L, mockHeist, null)));
        mockHeist.setMemberHeists(Set.of(new MemberHeist(), new MemberHeist(), new MemberHeist()));

        when(heistRepository.findById(anyLong())).thenReturn(Optional.of(mockHeist));
        heistService.getHeistOutcome(1L);

        assertEquals(HeistOutcome.SUCCEEDED, mockHeist.getHeistOutcome());

        //50%
        mockHeist.setHeistSkills(List.of(new HeistSkill(null, null, 4L, mockHeist, null)));
        mockHeist.setMemberHeists(Set.of(new MemberHeist(), new MemberHeist()));

        when(heistRepository.findById(anyLong())).thenReturn(Optional.of(mockHeist));
        heistService.getHeistOutcome(1L);

        assertNotNull(mockHeist.getHeistOutcome());

        //<50%
        mockHeist.setHeistSkills(List.of(new HeistSkill(null, null, 4L, mockHeist, null)));
        mockHeist.setMemberHeists(Set.of(new MemberHeist()));

        when(heistRepository.findById(anyLong())).thenReturn(Optional.of(mockHeist));
        heistService.getHeistOutcome(1L);

        assertEquals(HeistOutcome.FAILED, mockHeist.getHeistOutcome());
    }

    @Test
    void getHeistOutcomeIsSuccessOrFail() {
        Heist mockHeist = new Heist();
        mockHeist.setStatus(HeistStatus.FINISHED);
        mockHeist.setHeistSkills(List.of(new HeistSkill(1L, "string", 1L, mockHeist, null)));
        mockHeist.setMemberHeists(new HashSet<>());

        when(heistRepository.findById(anyLong())).thenReturn(Optional.of(mockHeist));

        heistService.getHeistOutcome(1L);

        assertTrue(mockHeist.getHeistOutcome().equals(HeistOutcome.FAILED) || mockHeist.getHeistOutcome().equals(HeistOutcome.SUCCEEDED));

        verify(heistRepository, times(1)).findById(anyLong());
        verify(heistRepository, times(1)).save(any(Heist.class));
        verify(memberService, times(1)).getPossibleOutcome(any(), anyFloat(), anyFloat());
        verify(heistToHeistDTO, times(1)).convert(any(Heist.class));
    }

    @Test
    @Disabled
    void automaticHeistStart() {
        List<Heist> heists = new ArrayList<>();
        Heist heist = new Heist();
        heist.setId(1L);
        heist.setStartTime(LocalDateTime.now().minusSeconds(1));
        heist.setEndTime(LocalDateTime.now().plusMinutes(1));
        heist.setStatus(HeistStatus.READY);
        heist.setHeistSkills(new ArrayList<>());
        heist.setMemberHeists(new HashSet<>());
        heists.add(heist);

        when(heistRepository.findAllHeists()).thenReturn(heists);
        when(heistRepository.findById(1L)).thenReturn(Optional.of(heist));

        heistService.automaticHeistStart();

        verify(heistService, times(1)).manualStartHeist(anyLong());
    }

    @Test
    @Disabled
    void automaticHeistEnd() {
        List<Heist> heists = new ArrayList<>();
        Heist heist = new Heist();
        heist.setId(1L);
        heist.setStartTime(LocalDateTime.now().minusSeconds(2));
        heist.setEndTime(LocalDateTime.now().minusSeconds(1));
        heist.setStatus(HeistStatus.IN_PROGRESS);
        heist.setHeistSkills(new ArrayList<>());
        heist.setMemberHeists(new HashSet<>());
        heists.add(heist);

        when(heistRepository.findAllHeists()).thenReturn(heists);
        when(heistRepository.findById(1L)).thenReturn(Optional.of(heist));

        heistService.automaticHeistStart();

        verify(heistService, times(1)).manualEndHeist(anyLong());
    }

    @Test
    void addHeist() {
        Skill combat = new Skill();
        combat.setName("combat");
        HeistSkill combatHeistSkill = new HeistSkill();
        combatHeistSkill.setSkill(combat);
        combatHeistSkill.setMembers(1L);
        combatHeistSkill.setLevel("**");
        Heist heist = new Heist();
        heist.setHeistSkills(List.of(combatHeistSkill));

        when(heistCommandToHeist.convert(any(HeistCommand.class))).thenReturn(heist);
        when(skillService.saveAllSkills(anyList())).thenReturn(List.of(combat));
        when(heistRepository.save(any(Heist.class))).thenReturn(heist);

        HeistCommand heistCommand = new HeistCommand();
        heistCommand.setStartTime(LocalDateTime.now().minusSeconds(1));

        heistService.addHeist(heistCommand);

        assertEquals(HeistStatus.IN_PROGRESS, heist.getStatus());

        verify(heistRepository, times(1)).save(any(Heist.class));
        verify(heistSkillService, times(1)).saveAllHeistSkills(any(Heist.class));
        verify(heistToHeistDTO, times(1)).convert(any(Heist.class));
    }

    @Test
    void updateHeistSkillsThrowsEntityNotFound() {
        assertThrows(EntityNotFound.class, () -> {
            when(heistRepository.findById(anyLong())).thenReturn(Optional.empty());

            heistService.updateHeistSkills(new HeistCommand(), 1L);
        });
    }

    @Test
    void updateHeistSkillsThrowsActionNotFound() {
        assertThrows(ActionNotFound.class, () -> {
            Heist heist = new Heist();
            heist.setStatus(HeistStatus.IN_PROGRESS);

            when(heistRepository.findById(anyLong())).thenReturn(Optional.of(heist));

            heistService.updateHeistSkills(new HeistCommand(), 1L);
        });
    }

    @Test
    void updateHeistSkills() {
        Skill combat = new Skill();
        combat.setName("combat");
        HeistSkill combatHeistSkill = new HeistSkill();
        combatHeistSkill.setSkill(combat);
        combatHeistSkill.setMembers(1L);
        combatHeistSkill.setLevel("*");
        Heist heist = new Heist();
        heist.setHeistSkills(List.of(combatHeistSkill));

        when(heistCommandToHeist.convert(any(HeistCommand.class))).thenReturn(heist);
        when(heistRepository.findById(anyLong())).thenReturn(Optional.of(heist));
        when(skillService.saveAllSkills(anyList())).thenReturn(List.of(combat));

        heistService.updateHeistSkills(new HeistCommand(), 1L);

        assertTrue(heist.getHeistSkills().contains(combatHeistSkill));

        verify(heistSkillService, times(1)).saveAllHeistSkills(any(Heist.class));
    }

    @Test
    void getEligibleMembers() {
        Skill combat = new Skill();
        combat.setName("combat");
        HeistSkill combatHeistSkill = new HeistSkill();
        combatHeistSkill.setSkill(combat);
        combatHeistSkill.setMembers(1L);
        combatHeistSkill.setLevel("*");
        MemberSkill combatMemberSKill = new MemberSkill();
        combatMemberSKill.setSkillLevel("**");
        combatMemberSKill.setSkill(combat);
        Heist assignedheist = new Heist();
        assignedheist.setStartTime(LocalDateTime.now().minusMinutes(1));
        assignedheist.setEndTime(LocalDateTime.now().plusMinutes(1));
        MemberHeist memberHeist = new MemberHeist();
        memberHeist.setHeist(assignedheist);
        Member member = new Member();
        member.setMemberHeists(new HashSet<>(List.of(memberHeist)));
        member.setMemberSkill(new HashSet<>(List.of(combatMemberSKill)));
        member.setMemberStatus(MemberStatus.AVAILABLE);

        Heist heist = new Heist();
        heist.setStartTime(LocalDateTime.now().plusMinutes(2));
        heist.setEndTime(LocalDateTime.now().plusMinutes(5));
        heist.setHeistSkills(List.of(combatHeistSkill));

        when(heistRepository.findById(anyLong())).thenReturn(Optional.of(heist));
        when(memberService.getAllMembersFromHeistSkill(anyList())).thenReturn(List.of(member));

        EligibleMembersDTO dto = heistService.getEligibleMembers(1L);

        assertEquals(1, dto.getMembers().size());
    }

    @Test
    void confirmMembersInHeist() {
        Skill combat = new Skill();
        combat.setName("combat");
        HeistSkill combatHeistSkill = new HeistSkill();
        combatHeistSkill.setSkill(combat);
        combatHeistSkill.setMembers(1L);
        combatHeistSkill.setLevel("*");
        MemberSkill combatMemberSKill = new MemberSkill();
        combatMemberSKill.setSkillLevel("**");
        combatMemberSKill.setSkill(combat);
        Member member = new Member();
        member.setMemberSkill(new HashSet<>(List.of(combatMemberSKill)));
        member.setMemberStatus(MemberStatus.AVAILABLE);

        Heist heist = new Heist();
        heist.setStartTime(LocalDateTime.now().plusMinutes(2));
        heist.setEndTime(LocalDateTime.now().plusMinutes(5));
        heist.setHeistSkills(List.of(combatHeistSkill));
        heist.setStatus(HeistStatus.PLANNING);
        MemberHeist memberHeist = new MemberHeist();
        memberHeist.setMember(member);
        memberHeist.setHeist(heist);

        when(heistRepository.findById(anyLong())).thenReturn(Optional.of(heist));
        when(memberService.findAllByNames(anyList())).thenReturn(List.of(member));
        when(memberHeistService.assignMemberToHeist(any(Heist.class), anyList())).thenReturn(List.of(memberHeist));

        HeistCommand heistCommand = new HeistCommand();
        heistCommand.setMembers(List.of("member"));

        heistService.confirmMembersInHeist(heistCommand, 1L);

        assertEquals(HeistStatus.READY, heist.getStatus());
        assertEquals(1, heist.getMemberHeists().size());

        verify(memberHeistService, times(1)).assignMemberToHeist(any(Heist.class), anyList());
        verify(heistRepository, times(1)).save(any(Heist.class));
    }

    @Test
    void confirmMembersInHeistThrowsEntityNotFound() {
        assertThrows(EntityNotFound.class, () -> {
            when(heistRepository.findById(anyLong())).thenReturn(Optional.empty());

            heistService.confirmMembersInHeist(new HeistCommand(), 1L);

            verify(heistRepository, times(1)).findById(anyLong());
        });
    }

    @Test
    void confirmMembersInHeistThrowsActionNotFound() {
        assertThrows(ActionNotFound.class, () -> {
            Heist heist = new Heist();
            heist.setStatus(HeistStatus.IN_PROGRESS);
            when(heistRepository.findById(anyLong())).thenReturn(Optional.of(heist));

            heistService.confirmMembersInHeist(new HeistCommand(), 1L);

            verify(heistRepository, times(1)).findById(anyLong());
        });
    }

    @Test
    void getHeistById() {
        when(heistRepository.findById(anyLong())).thenReturn(Optional.of(new Heist()));
        when(heistToHeistDTO.convert(new Heist())).thenReturn(new HeistDTO());

        heistService.getHeistById(1L);

        verify(heistRepository, times(1)).findById(anyLong());
        verify(heistToHeistDTO, times(1)).convert(any(Heist.class));
    }

    @Test
    void getHeistByIdThrowsEntityNotFound() {
        assertThrows(EntityNotFound.class, () -> {
            when(heistRepository.findById(anyLong())).thenReturn(Optional.empty());

            heistService.getHeistById(1L);

            verify(heistRepository, times(1)).findById(anyLong());
        });
    }

    @Test
    void getHeistMembers() {
        Heist heist = new Heist();
        heist.setStatus(HeistStatus.READY);

        when(heistRepository.findById(anyLong())).thenReturn(Optional.of(heist));
        when(memberService.getAllMembersFromHeist(1L)).thenReturn(List.of(new Member()));

        heistService.getHeistMembers(1L);

        verify(heistRepository, times(1)).findById(anyLong());
        verify(memberService, times(1)).getAllMembersFromHeist(anyLong());
        verify(memberToMemberDTO, times(1)).convert(any(Member.class));
    }

    @Test
    void getHeistMembersThrowsEntityNotFound() {
        assertThrows(EntityNotFound.class, () -> {
            when(heistRepository.findById(anyLong())).thenReturn(Optional.empty());

            heistService.getHeistMembers(1L);

            verify(heistRepository, times(1)).findById(anyLong());
        });
    }

    @Test
    void getHeistMembersThrowsActionNotFound() {
        assertThrows(ActionNotFound.class, () -> {
            Heist heist = new Heist();
            heist.setStatus(HeistStatus.PLANNING);
            when(heistRepository.findById(anyLong())).thenReturn(Optional.of(heist));

            heistService.getHeistMembers(1L);

            verify(heistRepository, times(1)).findById(anyLong());
        });
    }

    @Test
    void getHeistSkills() {
        Heist heist = new Heist();

        when(heistRepository.findById(anyLong())).thenReturn(Optional.of(heist));
        when(heistSkillService.getAllSkillsFromHeist(anyLong())).thenReturn(List.of(new HeistSkill()));

        heistService.getHeistSkills(1L);

        verify(heistRepository, times(1)).findById(anyLong());
        verify(heistSkillService, times(1)).getAllSkillsFromHeist(anyLong());
    }

    @Test
    void getHeistSkillsThrowsEntityNotFound() {
        assertThrows(EntityNotFound.class, () -> {
            when(heistRepository.findById(anyLong())).thenReturn(Optional.empty());

            heistService.getHeistSkills(1L);

            verify(heistRepository, times(1)).findById(anyLong());
        });
    }

    @Test
    void getHeistStatus() {
        Heist heist = new Heist();

        when(heistRepository.findById(anyLong())).thenReturn(Optional.of(heist));

        heistService.getHeistStatus(1L);

        verify(heistRepository, times(1)).findById(anyLong());
    }

    @Test
    void getHeistStatusThrowsEntityNotFound() {
        assertThrows(EntityNotFound.class, () -> {
            when(heistRepository.findById(anyLong())).thenReturn(Optional.empty());

            heistService.getHeistStatus(1L);

            verify(heistRepository, times(1)).findById(anyLong());
        });
    }
}