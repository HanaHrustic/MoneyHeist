package ag04.project.moneyheist.services;

import ag04.project.moneyheist.api.converter.HeistCommandToHeist;
import ag04.project.moneyheist.api.converter.HeistSkillToHeistSkillDTO;
import ag04.project.moneyheist.api.converter.HeistToHeistDTO;
import ag04.project.moneyheist.api.converter.MemberToMemberDTO;
import ag04.project.moneyheist.domain.*;
import ag04.project.moneyheist.exceptions.ActionNotFound;
import ag04.project.moneyheist.exceptions.EntityNotFound;
import ag04.project.moneyheist.repositories.HeistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        heistService = new HeistServiceImpl(heistRepository, heistCommandToHeist, heistToHeistDTO, skillService, heistSkillService, memberService, heistSkillToHeistSkillDTO, memberToMemberDTO, memberHeistService);
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
            verify(memberService, never()).getPossibleOutcome(any(), anyFloat(), anyFloat());
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
}