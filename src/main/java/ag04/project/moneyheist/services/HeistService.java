package ag04.project.moneyheist.services;

import ag04.project.moneyheist.api.DTO.EligibleMembersDTO;
import ag04.project.moneyheist.api.DTO.HeistDTO;
import ag04.project.moneyheist.api.command.HeistCommand;

public interface HeistService {
    boolean isDuplicateName(String name);

    HeistDTO addHeist(HeistCommand heistCommand);

    void updateHeistSkills(HeistCommand heistCommand, Long memberId);

    EligibleMembersDTO getEligibleMembers(Long heistId);

    void confirmMembersInHeist(HeistCommand heistCommand, Long heistId);

    void manualStartHeist(Long heistId);
}
