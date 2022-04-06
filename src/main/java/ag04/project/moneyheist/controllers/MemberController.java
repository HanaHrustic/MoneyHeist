package ag04.project.moneyheist.controllers;

import ag04.project.moneyheist.api.DTO.MemberDTO;
import ag04.project.moneyheist.api.command.MemberCommand;
import ag04.project.moneyheist.services.MemberService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping()
    public ResponseEntity<Void> saveMember(@Valid @RequestBody MemberCommand memberCommand){
        MemberDTO savedMember = memberService.addMember(memberCommand);

        return ResponseEntity.created(URI.create("/member/" + savedMember.getId().toString())).build();
    }
}
