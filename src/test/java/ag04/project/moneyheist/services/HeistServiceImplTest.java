package ag04.project.moneyheist.services;

import ag04.project.moneyheist.Mail.EmailService;
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
}