package ag04.project.moneyheist.api.converter;

import ag04.project.moneyheist.api.command.SkillCommand;
import ag04.project.moneyheist.domain.HeistSkill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class SkillCommandToHeistSkill implements Converter<SkillCommand, HeistSkill> {
    private final SkillCommandToSkill skillCommandToSkill;

    @Autowired
    public SkillCommandToHeistSkill(SkillCommandToSkill skillCommandToSkill) {
        this.skillCommandToSkill = skillCommandToSkill;
    }

    public HeistSkill convert(SkillCommand source) {
        if (source == null) {
            return null;
        }
        final HeistSkill heistSkill = new HeistSkill();
        heistSkill.setLevel(source.getLevel());
        heistSkill.setMembers(source.getMembers());
        heistSkill.setSkill(skillCommandToSkill.convert(source));

        return heistSkill;
    }
}
