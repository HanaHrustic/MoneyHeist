package ag04.project.moneyheist.controllers;

import ag04.project.moneyheist.api.DTO.EligibleMembersDTO;
import ag04.project.moneyheist.api.DTO.HeistDTO;
import ag04.project.moneyheist.api.DTO.HeistSkillDTO;
import ag04.project.moneyheist.api.DTO.MemberDTO;
import ag04.project.moneyheist.api.command.HeistCommand;
import ag04.project.moneyheist.api.group.ConfirmMembersInHeist;
import ag04.project.moneyheist.api.group.CreateHeist;
import ag04.project.moneyheist.api.group.UpdateHeistSkill;
import ag04.project.moneyheist.api.view.GetHeist;
import ag04.project.moneyheist.api.view.GetHeistStatus;
import ag04.project.moneyheist.api.view.ReadEligibleMembers;
import ag04.project.moneyheist.services.HeistService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/heist")
public class HeistController {
    private final HeistService heistService;

    @Autowired
    public HeistController(HeistService heistService) {
        this.heistService = heistService;
    }

    @PostMapping()
    public ResponseEntity<Void> saveHeist(@Validated({CreateHeist.class}) @RequestBody HeistCommand heistCommand) {
        HeistDTO savedHeist = heistService.addHeist(heistCommand);

        return ResponseEntity.created(URI.create("/heist/" + savedHeist.getId().toString())).build();
    }

    @PatchMapping("{memberId}/skills")
    public ResponseEntity<Void> updateHeistSkills(@Validated({UpdateHeistSkill.class}) @RequestBody HeistCommand heistCommand, @PathVariable Long memberId) {
        heistService.updateHeistSkills(heistCommand, memberId);

        return ResponseEntity.noContent().header("Content-Location", "/heist/" + memberId + "/skills").build();
    }

    @JsonView(ReadEligibleMembers.class)
    @GetMapping("{heistId}/eligible_members")
    public ResponseEntity<EligibleMembersDTO> getEligibleMembers(@PathVariable Long heistId) {
        EligibleMembersDTO eligibleMembersDTO = heistService.getEligibleMembers(heistId);

        return ResponseEntity.status(HttpStatus.OK).body(eligibleMembersDTO);
    }

    @PutMapping("{heistId}/members")
    public ResponseEntity<Void> confirmMembersInHeist(@Validated({ConfirmMembersInHeist.class}) @RequestBody HeistCommand heistCommand, @PathVariable Long heistId) {
        heistService.confirmMembersInHeist(heistCommand, heistId);

        return ResponseEntity.noContent().header("Content-Location", "/heist/" + heistId + "/members").build();
    }

    @PutMapping("{heistId}/start")
    public ResponseEntity<Void> manualStartHeist(@PathVariable Long heistId) {
        heistService.manualStartHeist(heistId);

        return ResponseEntity.status(HttpStatus.OK).header("Location", "/heist/" + heistId + "/status").build();
    }

    @JsonView(GetHeist.class)
    @GetMapping("{heistId}")
    public ResponseEntity<HeistDTO> getHeistById(@PathVariable Long heistId) {
        HeistDTO heistDTO = heistService.getHeistById(heistId);

        return ResponseEntity.status(HttpStatus.OK).body(heistDTO);
    }

    @JsonView(ReadEligibleMembers.class)
    @GetMapping("{heistId}/members")
    public ResponseEntity<List<MemberDTO>> getHeistMembers(@PathVariable Long heistId) {
        List<MemberDTO> memberDTO = heistService.getHeistMembers(heistId);

        return ResponseEntity.status(HttpStatus.OK).body(memberDTO);
    }

    @JsonView(ReadEligibleMembers.class)
    @GetMapping("{heistId}/skills")
    public ResponseEntity<List<HeistSkillDTO>> getHeistSkills(@PathVariable Long heistId) {
        List<HeistSkillDTO> skillDTO = heistService.getHeistSkills(heistId);

        return ResponseEntity.status(HttpStatus.OK).body(skillDTO);
    }

    @JsonView(GetHeistStatus.class)
    @GetMapping("{heistId}/status")
    public ResponseEntity<HeistDTO> getHeistStatus(@PathVariable Long heistId) {
        HeistDTO heistDTO = heistService.getHeistStatus(heistId);

        return ResponseEntity.status(HttpStatus.OK).body(heistDTO);
    }
}
