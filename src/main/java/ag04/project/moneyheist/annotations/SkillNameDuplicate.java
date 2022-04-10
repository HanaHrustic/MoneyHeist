package ag04.project.moneyheist.annotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD})
@Constraint(validatedBy = SkillValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface SkillNameDuplicate {
    String message() default "Must be unique!";

    boolean considerSkillLevel() default false;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
