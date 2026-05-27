package academy.devdojo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EmailAlreadyExistException extends ResponseStatusException {

    public EmailAlreadyExistException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
