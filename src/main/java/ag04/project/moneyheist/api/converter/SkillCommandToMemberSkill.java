package ag04.project.moneyheist.api.converter;

import ag04.project.moneyheist.api.DTO.MemberDTO;
import ag04.project.moneyheist.api.command.MemberCommand;
import ag04.project.moneyheist.api.command.SkillCommand;
import ag04.project.moneyheist.domain.Member;
import ag04.project.moneyheist.domain.MemberSkill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class SkillCommandToMemberSkill implements Converter<SkillCommand, MemberSkill> {

    private final SkillCommandToSkill skillCommandToSkill;

    @Autowired
    public SkillCommandToMemberSkill(SkillCommandToSkill skillCommandToSkill) {
        this.skillCommandToSkill = skillCommandToSkill;
    }

    public MemberSkill convert(SkillCommand source) {
        if (source == null){
            return null;
        }
        final MemberSkill memberSkill = new MemberSkill();
        memberSkill.setSkillLevel(source.getLevel());
        memberSkill.setSkill(skillCommandToSkill.convert(source));


        return memberSkill;
    }
}
