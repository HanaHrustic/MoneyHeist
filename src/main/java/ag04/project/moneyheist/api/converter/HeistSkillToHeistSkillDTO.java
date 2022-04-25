package ag04.project.moneyheist.api.converter;

import ag04.project.moneyheist.api.DTO.HeistSkillDTO;
import ag04.project.moneyheist.domain.HeistSkill;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class HeistSkillToHeistSkillDTO implements Converter<HeistSkill, HeistSkillDTO> {
    @Override
    public HeistSkillDTO convert(HeistSkill source) {
        if (source == null) {
            return null;
        }
        final HeistSkillDTO heistSkillDTO = new HeistSkillDTO();
        heistSkillDTO.setName(source.getSkill().getName());
        heistSkillDTO.setLevel(source.getLevel());
        heistSkillDTO.setMembers(source.getMembers());

        return heistSkillDTO;
    }
}
