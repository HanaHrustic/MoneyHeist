package ag04.project.moneyheist.api.converter;

import ag04.project.moneyheist.api.DTO.HeistDTO;
import ag04.project.moneyheist.domain.Heist;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class HeistToHeistDTO implements Converter<Heist, HeistDTO> {

    private final HeistSkillToHeistSkillDTO heistSkillToHeistSkillDTO;

    public HeistToHeistDTO(HeistSkillToHeistSkillDTO heistSkillToHeistSkillDTO) {
        this.heistSkillToHeistSkillDTO = heistSkillToHeistSkillDTO;
    }

    @Override
    public HeistDTO convert(Heist source) {
        if (source == null) {
            return null;
        }
        final HeistDTO heistDTO = new HeistDTO();
        heistDTO.setId(source.getId());
        heistDTO.setName(source.getName());
        heistDTO.setLocation(source.getLocation());
        heistDTO.setStartTime(source.getStartTime());
        heistDTO.setEndTime(source.getEndTime());
        heistDTO.setSkills(source.getHeistSkills().stream().map(heistSkillToHeistSkillDTO::convert).collect(Collectors.toList()));
        heistDTO.setStatus(source.getStatus());
        heistDTO.setOutcome(source.getHeistOutcome());

        return heistDTO;
    }
}
