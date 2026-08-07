package io.github.thuliosouza.scholar_rest_service.domain.student.exception;

public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(String message) {
        super(message);
    }
}
