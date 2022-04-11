package ag04.project.moneyheist.api.converter;

import ag04.project.moneyheist.api.DTO.HeistDTO;
import ag04.project.moneyheist.domain.Heist;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class HeistToHeistDTO implements Converter<Heist, HeistDTO> {
    @Override
    public HeistDTO convert(Heist source) {
        if (source == null) {
            return null;
        }
        final HeistDTO heistDTO = new HeistDTO();
        heistDTO.setId(source.getId());

        return heistDTO;
    }
}
