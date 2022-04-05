package ag04.project.moneyheist.services;

import ag04.project.moneyheist.api.DTO.MemberDTO;
import ag04.project.moneyheist.api.command.MemberCommand;
import ag04.project.moneyheist.api.converter.MemberCommandToMember;
import ag04.project.moneyheist.api.converter.MemberToMemberDTO;
import ag04.project.moneyheist.domain.Member;
import ag04.project.moneyheist.repositories.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberCommandToMember memberCommandToMember;
    private final MemberToMemberDTO memberToMemberDTO;

    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository, MemberCommandToMember memberCommandToMember, MemberToMemberDTO memberToMemberDTO) {
        this.memberRepository = memberRepository;
        this.memberCommandToMember = memberCommandToMember;
        this.memberToMemberDTO = memberToMemberDTO;
    }

    @Override
    @Transactional
    public MemberDTO addMember(MemberCommand memberCommand) {
        Member memberToAdd = memberCommandToMember.convert(memberCommand);
        Member savedMember = memberRepository.save(memberToAdd);
        return memberToMemberDTO.convert(savedMember);
    }
}
