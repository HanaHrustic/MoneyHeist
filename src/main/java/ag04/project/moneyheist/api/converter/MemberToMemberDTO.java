package ag04.project.moneyheist.api.converter;

import ag04.project.moneyheist.api.DTO.MemberDTO;
import ag04.project.moneyheist.domain.Member;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class MemberToMemberDTO implements Converter<Member, MemberDTO> {

    private final MemberSkillToMemberSkillDTO memberSkillToMemberSkillDTO;

    public MemberToMemberDTO(MemberSkillToMemberSkillDTO memberSkillToMemberSkillDTO) {
        this.memberSkillToMemberSkillDTO = memberSkillToMemberSkillDTO;
    }

    public MemberDTO convert(Member source) {
        if (source == null) {
            return null;
        }
        final MemberDTO memberDto = new MemberDTO();
        memberDto.setId(source.getId());
        memberDto.setName(source.getName());
        memberDto.setSex(source.getSex());
        memberDto.setEmail(source.getEmail());
        memberDto.setSkills(source.getMemberSkill().stream().map(memberSkillToMemberSkillDTO::convert).collect(Collectors.toList()));
        memberDto.setMainSkill(source.getMainSkill().getName());
        memberDto.setStatus(source.getMemberStatus());

        return memberDto;
    }
}
