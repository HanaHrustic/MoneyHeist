package ag04.project.moneyheist.annotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE})
@Constraint(validatedBy = DateTimeValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface DateTimeCheck {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
