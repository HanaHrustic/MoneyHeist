package ag04.project.moneyheist.services;

import ag04.project.moneyheist.Mail.EmailService;
import ag04.project.moneyheist.domain.Heist;
import ag04.project.moneyheist.domain.Member;
import ag04.project.moneyheist.domain.MemberHeist;
import ag04.project.moneyheist.exceptions.BadRequest;
import ag04.project.moneyheist.repositories.MemberHeistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MemberHeistServiceImpl implements MemberHeistService {
    private final MemberHeistRepository memberHeistRepository;
    private final EmailService emailService;

    @Autowired
    public MemberHeistServiceImpl(MemberHeistRepository memberHeistRepository, EmailService emailService) {
        this.memberHeistRepository = memberHeistRepository;
        this.emailService = emailService;
    }

    @Override
    public List<MemberHeist> assignMemberToHeist(Heist heist, List<Member> members) {
        List<MemberHeist> memberHeist = new ArrayList<>();
        members.forEach(member -> {
            if (member.getMemberHeists().size() == 0) {
                memberHeist.add(new MemberHeist(member, heist));
                emailService.sendSimpleMessage(member.getEmail(), "ADDED TO HEIST",
                        "You have been added to a heist.");
            } else {
                throw new BadRequest("Member has already been confirmed on another heist!");
            }
        });

        List<MemberHeist> savedMemberHeists = new ArrayList<>();
        Iterable<MemberHeist> memberHeistsFromRepo = memberHeistRepository.saveAll(memberHeist);
        memberHeistsFromRepo.forEach(savedMemberHeists::add);

        return savedMemberHeists;
    }
}
