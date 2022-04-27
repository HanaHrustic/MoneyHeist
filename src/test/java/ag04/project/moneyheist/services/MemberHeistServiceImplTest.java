package ag04.project.moneyheist.services;

import ag04.project.moneyheist.Mail.EmailService;
import ag04.project.moneyheist.domain.Heist;
import ag04.project.moneyheist.domain.Member;
import ag04.project.moneyheist.domain.MemberHeist;
import ag04.project.moneyheist.exceptions.BadRequest;
import ag04.project.moneyheist.repositories.MemberHeistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class MemberHeistServiceImplTest {
    MemberHeistService memberHeistService;

    @Mock
    MemberHeistRepository memberHeistRepository;

    @Mock
    EmailService emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        memberHeistService = new MemberHeistServiceImpl(memberHeistRepository, emailService);
    }

    @Test
    void assignMemberToHeist() {
        Heist heist = new Heist();
        List<Member> members = new ArrayList<>();

        memberHeistService.assignMemberToHeist(heist, members);

        verify(memberHeistRepository, times(1)).saveAll(any());
    }

    @Test
    void memberAssignmentSendsEmail() {
        Heist heist = new Heist();
        Member member = new Member();
        List<Member> members = new ArrayList<>();
        members.add(member);

        memberHeistService.assignMemberToHeist(heist, members);

        verify(emailService, times(1)).sendSimpleMessage(any(), any(), any());
    }

    @Test
    void assignMemberToHeistThrowsBadRequest() {
        assertThrows(BadRequest.class, () -> {
            Heist heist = new Heist();
            Member member = new Member();
            member.setMemberHeists(new HashSet<>(List.of(new MemberHeist())));
            List<Member> members = new ArrayList<>();
            members.add(member);

            memberHeistService.assignMemberToHeist(heist, members);
        });
    }
}