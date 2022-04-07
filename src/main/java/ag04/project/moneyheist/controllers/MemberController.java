package ag04.project.moneyheist.controllers;

import ag04.project.moneyheist.api.DTO.MemberDTO;
import ag04.project.moneyheist.api.command.MemberCommand;
import ag04.project.moneyheist.api.group.CreateMember;
import ag04.project.moneyheist.api.group.UpdateMemberSkill;
import ag04.project.moneyheist.services.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping()
    public ResponseEntity<Void> saveMember(@Validated({CreateMember.class}) @RequestBody MemberCommand memberCommand) {
        MemberDTO savedMember = memberService.addMember(memberCommand);

        return ResponseEntity.created(URI.create("/member/" + savedMember.getId().toString())).build();
    }

    @PutMapping("{memberId}/skills")
    public ResponseEntity<Void> updateMemberSkills(@Validated({UpdateMemberSkill.class}) @RequestBody MemberCommand memberCommand, @PathVariable Long memberId) {
        memberService.updateMember(memberCommand, memberId);

        return ResponseEntity.noContent().header("Content-Location", "/member/" + memberId + "/skills").build();
    }

    @DeleteMapping("{memberId}/skills/{skillName}")
    public ResponseEntity<Void> deleteMemberSkills(@PathVariable Long memberId, @PathVariable String skillName) {
        memberService.deleteMemberSkill(memberId, skillName);
        return ResponseEntity.noContent().build();
    }
}
