package ag04.project.moneyheist.services;

import ag04.project.moneyheist.api.DTO.HeistDTO;
import ag04.project.moneyheist.api.command.HeistCommand;
import ag04.project.moneyheist.repositories.HeistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HeistServiceImpl implements HeistService {
    private final HeistRepository heistRepository;

    @Autowired
    public HeistServiceImpl(HeistRepository heistRepository) {
        this.heistRepository = heistRepository;
    }

    @Override
    public HeistDTO addHeist(HeistCommand heistCommand) {
        return null;
    }

    @Override
    public boolean isDuplicateName(String name) {
        return heistRepository.existsByName(name);
    }
}
