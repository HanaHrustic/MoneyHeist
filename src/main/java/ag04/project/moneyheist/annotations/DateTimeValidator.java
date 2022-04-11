package ag04.project.moneyheist.annotations;

import ag04.project.moneyheist.api.command.HeistCommand;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

@Component
public class DateTimeValidator implements ConstraintValidator<DateTimeCheck, HeistCommand> {

    @Override
    public void initialize(DateTimeCheck constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(HeistCommand heistCommand, ConstraintValidatorContext context) {
        LocalDateTime currentTime = LocalDateTime.now();
        if (heistCommand.getEndTime().isBefore(currentTime) || heistCommand.getStartTime().isAfter(heistCommand.getEndTime())) {
            return false;
        }
        return true;
    }
}
