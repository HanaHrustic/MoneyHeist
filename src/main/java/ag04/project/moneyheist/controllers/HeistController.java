package ag04.project.moneyheist.controllers;

import ag04.project.moneyheist.api.DTO.EligibleMembersDTO;
import ag04.project.moneyheist.api.DTO.HeistDTO;
import ag04.project.moneyheist.api.command.HeistCommand;
import ag04.project.moneyheist.api.group.CreateHeist;
import ag04.project.moneyheist.api.group.UpdateHeistSkill;
import ag04.project.moneyheist.api.view.ReadEligibleMembers;
import ag04.project.moneyheist.services.HeistService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

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
}
