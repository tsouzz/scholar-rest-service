package io.github.thuliosouza.scholar_rest_service.domain.classgroup.exception;

public class ClassGroupNotFoundException extends RuntimeException {
    public ClassGroupNotFoundException(String message) {
        super(message);
    }
}
