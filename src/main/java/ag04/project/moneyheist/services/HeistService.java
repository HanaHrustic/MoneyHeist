package ag04.project.moneyheist.services;

import ag04.project.moneyheist.api.DTO.EligibleMembersDTO;
import ag04.project.moneyheist.api.DTO.HeistDTO;
import ag04.project.moneyheist.api.DTO.HeistSkillDTO;
import ag04.project.moneyheist.api.DTO.MemberDTO;
import ag04.project.moneyheist.api.command.HeistCommand;
import ag04.project.moneyheist.domain.Heist;

import java.util.List;

public interface HeistService {
    boolean isDuplicateName(String name);

    HeistDTO addHeist(HeistCommand heistCommand);

    void updateHeistSkills(HeistCommand heistCommand, Long memberId);

    EligibleMembersDTO getEligibleMembers(Long heistId);

    void confirmMembersInHeist(HeistCommand heistCommand, Long heistId);

    void manualStartHeist(Long heistId);

    List<Heist> findHeistsForMemberId(Long memberId);

    HeistDTO getHeistById(Long heistId);

    List<MemberDTO> getHeistMembers(Long heistId);

    List<HeistSkillDTO> getHeistSkills(Long heistId);

    HeistDTO getHeistStatus(Long heistId);

    void automaticHeistStart();

    void manualEndHeist(Long heistId);

    HeistDTO getHeistOutcome(Long heistId);
}
