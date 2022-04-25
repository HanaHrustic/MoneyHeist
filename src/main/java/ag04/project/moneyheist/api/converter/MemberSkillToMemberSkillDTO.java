package ag04.project.moneyheist.api.converter;

import ag04.project.moneyheist.api.DTO.MemberSkillDTO;
import ag04.project.moneyheist.domain.MemberSkill;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class MemberSkillToMemberSkillDTO implements Converter<MemberSkill, MemberSkillDTO> {
    @Override
    public MemberSkillDTO convert(MemberSkill source) {
        if (source == null) {
            return null;
        }
        final MemberSkillDTO memberSkillDTO = new MemberSkillDTO();
        memberSkillDTO.setName(source.getSkill().getName());
        memberSkillDTO.setLevel(source.getSkillLevel());

        return memberSkillDTO;
    }
}
