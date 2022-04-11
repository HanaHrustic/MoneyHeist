package ag04.project.moneyheist.annotations;

import ag04.project.moneyheist.services.MemberService;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class EmailValidator implements ConstraintValidator<EmailDuplicate, String> {

    private final MemberService memberService;

    public EmailValidator(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public void initialize(EmailDuplicate constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return !memberService.isDuplicateEmail(value);
    }
}
