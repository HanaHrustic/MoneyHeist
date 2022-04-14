package ag04.project.moneyheist.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
public class ActionNotFound extends RuntimeException {
    public ActionNotFound() {
    }

    public ActionNotFound(String message) {
        super(message);
    }

    public ActionNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    public ActionNotFound(Throwable cause) {
        super(cause);
    }

    public ActionNotFound(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
