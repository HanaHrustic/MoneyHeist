package ag04.project.moneyheist.annotations;

import javax.validation.Constraint;
import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD})
@Constraint(validatedBy = NameValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface NameDuplicate {

}
