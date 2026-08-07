package io.github.thuliosouza.scholar_rest_service.domain.activity.exception;

public class InvalidGradeException extends RuntimeException {
    public InvalidGradeException(String message) {
        super(message);
    }
}
