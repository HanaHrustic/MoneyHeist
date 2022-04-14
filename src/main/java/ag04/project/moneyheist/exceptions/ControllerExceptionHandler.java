package ag04.project.moneyheist.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(SkillDoesNotExist.class)
    public ResponseEntity<Object> handleSkillDoesNotExist(RuntimeException ex, WebRequest request) {

        return handleExceptionInternal(ex, null, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(EntityNotFound.class)
    public ResponseEntity<Object> handleMemberNotFound(RuntimeException ex, WebRequest request) {

        return handleExceptionInternal(ex, null, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(ActionNotFound.class)
    public ResponseEntity<Object> handleActionNotFound(RuntimeException ex, WebRequest request) {

        return handleExceptionInternal(ex, null, new HttpHeaders(), HttpStatus.METHOD_NOT_ALLOWED, request);
    }
}
