package ag04.project.moneyheist.annotations;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = UniqueValidator.class)
public @interface UniqueValidation {
    String message() default "Must be unique!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}