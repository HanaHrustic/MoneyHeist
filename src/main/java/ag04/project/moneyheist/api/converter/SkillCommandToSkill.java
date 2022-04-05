package ag04.project.moneyheist.api.converter;

import ag04.project.moneyheist.api.command.SkillCommand;
import ag04.project.moneyheist.domain.Skill;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class SkillCommandToSkill implements Converter<SkillCommand, Skill> {

    public Skill convert(SkillCommand source) {
        if (source == null){
            return null;
        }
        final Skill skill = new Skill();
        skill.setName(source.getName());
        skill.setLevel(source.getLevel());

        return skill;
    }
}
