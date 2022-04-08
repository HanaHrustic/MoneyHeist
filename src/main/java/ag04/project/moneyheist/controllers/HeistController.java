package ag04.project.moneyheist.controllers;

import ag04.project.moneyheist.api.DTO.HeistDTO;
import ag04.project.moneyheist.api.command.HeistCommand;
import ag04.project.moneyheist.services.HeistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<Void> saveHeist(@RequestBody HeistCommand heistCommand) {
        HeistDTO savedHeist = heistService.addHeist(heistCommand);

        return ResponseEntity.created(URI.create("/heist/" + savedHeist.getId().toString())).build();
    }
}
