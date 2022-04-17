package ag04.project.moneyheist.controllers;

import ag04.project.moneyheist.api.DTO.MemberDTO;
import ag04.project.moneyheist.api.command.MemberCommand;
import ag04.project.moneyheist.api.group.CreateMember;
import ag04.project.moneyheist.api.group.UpdateMemberSkill;
import ag04.project.moneyheist.api.view.GetMember;
import ag04.project.moneyheist.api.view.GetMemberSkills;
import ag04.project.moneyheist.services.MemberService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @Autowired
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

    @JsonView(GetMember.class)
    @GetMapping("{memberId}")
    public ResponseEntity<MemberDTO> getMemberById(@PathVariable Long memberId) {
        MemberDTO memberDTO = memberService.getMemberById(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(memberDTO);
    }

    @JsonView(GetMemberSkills.class)
    @GetMapping("{memberId}/skills")
    public ResponseEntity<MemberDTO> getMemberSkills(@PathVariable Long memberId) {
        MemberDTO memberDTO = memberService.getMemberSkills(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(memberDTO);
    }
}
