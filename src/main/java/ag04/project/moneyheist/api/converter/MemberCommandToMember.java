package ag04.project.moneyheist.api.converter;

import ag04.project.moneyheist.api.command.MemberCommand;
import ag04.project.moneyheist.domain.Member;
import ag04.project.moneyheist.domain.MemberStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class MemberCommandToMember implements Converter<MemberCommand, Member>{
    private final SkillCommandToMemberSkill skillCommandToMemberSkill;
    private final SkillCommandToSkill skillCommandToSkill;

    @Autowired
    public MemberCommandToMember(SkillCommandToMemberSkill skillCommandToMemberSkill, SkillCommandToSkill skillCommandToSkill) {
        this.skillCommandToMemberSkill = skillCommandToMemberSkill;
        this.skillCommandToSkill = skillCommandToSkill;
    }

    public Member convert(MemberCommand source){
        if(source == null){
            return null;
        }
        final Member member = new Member();
        member.setName(source.getName());
        member.setSex(source.getSex());
        member.setEmail(source.getEmail());
        member.setMemberSkill(source.getSkills().stream().map(skillCommandToMemberSkill::convert).map(memberSkill -> {memberSkill.setMember(member); return memberSkill;}).collect(Collectors.toSet()));
        member.setMainSkill(source.getSkills().stream().filter(skill -> skill.getName().equals(source.getMainSkill())).map(skillCommandToSkill::convert).findFirst().orElse(null));
        member.setMemberStatus(MemberStatus.valueOf(source.getStatus()));


        return member;
    }
}
