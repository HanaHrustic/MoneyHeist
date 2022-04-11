package ag04.project.moneyheist.annotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD})
@Constraint(validatedBy = NameValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface NameDuplicate {
    String message() default "Must be unique!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
