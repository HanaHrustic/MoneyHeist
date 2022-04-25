package ag04.project.moneyheist.services;

import ag04.project.moneyheist.domain.Heist;
import ag04.project.moneyheist.domain.Member;
import ag04.project.moneyheist.domain.MemberHeist;

import java.util.List;

public interface MemberHeistService {
    List<MemberHeist> assignMemberToHeist(Heist heist, List<Member> members);
}
