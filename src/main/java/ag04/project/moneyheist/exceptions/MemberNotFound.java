package ag04.project.moneyheist.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MemberNotFound extends RuntimeException {
    public MemberNotFound() {
        super();
    }

    public MemberNotFound(String message) {
        super(message);
    }

    public MemberNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    public MemberNotFound(Throwable cause) {
        super(cause);
    }

    protected MemberNotFound(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
