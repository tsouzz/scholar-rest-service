package io.github.thuliosouza.scholar_rest_service.domain.activity.exception;

public class ActivityNotFoundException extends RuntimeException {
    public ActivityNotFoundException(String message) {
        super(message);
    }
}
