package ag04.project.moneyheist.annotations;

import ag04.project.moneyheist.api.command.SkillCommand;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collections;
import java.util.List;

@Component
public class SkillValidator implements ConstraintValidator<SkillNameDuplicate, List<SkillCommand>> {

    private boolean considerSkillLevel;

    @Override
    public void initialize(SkillNameDuplicate constraintAnnotation) {
        this.considerSkillLevel = constraintAnnotation.considerSkillLevel();
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(List<SkillCommand> value, ConstraintValidatorContext context) {
        if (considerSkillLevel) {
            return value.stream().peek(skillCommand -> skillCommand.setName(skillCommand.getName().toLowerCase()))
                    .filter(i -> Collections.frequency(value, i) > 1).toList().size() <= 0;
        } else {
            List<String> listSkillName = value.stream().map(SkillCommand::getName).map(String::toLowerCase).toList();
            return listSkillName.stream().filter(i -> Collections.frequency(listSkillName, i) > 1).toList().size() <= 0;
        }
    }
}