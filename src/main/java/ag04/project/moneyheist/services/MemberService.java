package ag04.project.moneyheist.services;


import ag04.project.moneyheist.api.DTO.MemberDTO;
import ag04.project.moneyheist.api.command.MemberCommand;

public interface MemberService {
    MemberDTO addMember(MemberCommand memberCommand);
}
