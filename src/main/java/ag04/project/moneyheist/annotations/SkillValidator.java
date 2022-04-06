package ag04.project.moneyheist.annotations;

import ag04.project.moneyheist.api.command.SkillCommand;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collections;
import java.util.List;

@Component
public class SkillValidator implements ConstraintValidator<SkillNameDuplicate, List<SkillCommand>> {

    @Override
    public void initialize(SkillNameDuplicate constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(List<SkillCommand> value, ConstraintValidatorContext context) {
        List<String> listSkillName = value.stream().map(SkillCommand::getName).map(String::toLowerCase).toList();
        return listSkillName.stream().filter(i -> Collections.frequency(listSkillName, i) > 1).toList().size() <= 0;
    }
}