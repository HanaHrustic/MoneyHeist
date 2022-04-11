package ag04.project.moneyheist.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SkillDoesNotExist extends RuntimeException{

    public SkillDoesNotExist() {
        super();
    }

    public SkillDoesNotExist(String message) {
        super(message);
    }

    public SkillDoesNotExist(String message, Throwable cause) {
        super(message, cause);
    }

    public SkillDoesNotExist(Throwable cause) {
        super(cause);
    }

    protected SkillDoesNotExist(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
