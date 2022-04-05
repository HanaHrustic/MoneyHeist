package ag04.project.moneyheist.api.converter;

import ag04.project.moneyheist.api.DTO.MemberDTO;
import ag04.project.moneyheist.api.command.SkillCommand;
import ag04.project.moneyheist.domain.Member;
import ag04.project.moneyheist.domain.Skill;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class MemberToMemberDTO implements Converter<Member, MemberDTO> {
    public MemberDTO convert(Member source) {
        if (source == null){
            return null;
        }
        final MemberDTO memberDto = new MemberDTO();
        memberDto.setId(source.getId());

        return memberDto;
    }
}
