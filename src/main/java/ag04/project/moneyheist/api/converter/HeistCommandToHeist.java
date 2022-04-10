package ag04.project.moneyheist.api.converter;

import ag04.project.moneyheist.api.command.HeistCommand;
import ag04.project.moneyheist.domain.Heist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class HeistCommandToHeist implements Converter<HeistCommand, Heist> {

    private final SkillCommandToHeistSkill skillCommandToHeistSkill;

    @Autowired
    public HeistCommandToHeist(SkillCommandToHeistSkill skillCommandToHeistSkill) {
        this.skillCommandToHeistSkill = skillCommandToHeistSkill;
    }

    public Heist convert(HeistCommand source) {
        if (source == null) {
            return null;
        }
        final Heist heist = new Heist();
        heist.setName(source.getName());
        heist.setLocation(source.getLocation());
        heist.setStartTime(source.getStartTime());
        heist.setEndTime(source.getEndTime());
        heist.setHeistSkills(source.getSkills().stream().map(skillCommandToHeistSkill::convert)
                .peek(heistSkill -> heistSkill.setHeist(heist)).collect(Collectors.toList()));

        return heist;
    }
}
