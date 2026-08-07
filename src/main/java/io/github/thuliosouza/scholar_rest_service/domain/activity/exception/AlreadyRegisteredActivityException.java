package io.github.thuliosouza.scholar_rest_service.domain.activity.exception;

public class AlreadyRegisteredActivityException extends RuntimeException {
    public AlreadyRegisteredActivityException(String message) {
        super(message);
    }
}
