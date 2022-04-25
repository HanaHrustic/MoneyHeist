package ag04.project.moneyheist.services;


import ag04.project.moneyheist.api.DTO.MemberDTO;
import ag04.project.moneyheist.api.command.MemberCommand;
import ag04.project.moneyheist.domain.Heist;
import ag04.project.moneyheist.domain.HeistSkill;
import ag04.project.moneyheist.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberService {
    MemberDTO addMember(MemberCommand memberCommand);

    boolean isDuplicateEmail(String email);

    void updateMember(MemberCommand memberCommand, Long memberId);

    void deleteMemberSkill(Long memberId, String skillName);

    List<Member> getAllMembersFromHeistSkill(List<HeistSkill> heistSkills);

    List<Member> findAllByNames(List<String> names);

    MemberDTO getMemberById(Long memberId);

    MemberDTO getMemberSkills(Long memberId);

    List<Member> getAllMembersFromHeist(Long heistId);

    void getPossibleOutcome(Optional<Heist> heistById, Float requiredMembers, Float numberOfMembers);
}
