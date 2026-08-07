package io.github.thuliosouza.scholar_rest_service.domain.student.exception;

public class InvalidOperationException extends RuntimeException {
    public InvalidOperationException(String message) {
        super(message);
    }
}
