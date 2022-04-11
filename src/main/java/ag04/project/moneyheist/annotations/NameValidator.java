package ag04.project.moneyheist.annotations;

import ag04.project.moneyheist.services.HeistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class NameValidator implements ConstraintValidator<NameDuplicate, String> {

    private final HeistService heistService;

    @Autowired
    public NameValidator(HeistService heistService) {
        this.heistService = heistService;
    }

    @Override
    public void initialize(NameDuplicate constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return !heistService.isDuplicateName(value);
    }
}
