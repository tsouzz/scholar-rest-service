package io.github.thuliosouza.scholar_rest_service.domain.teacher.exception;

public class TeacherNotFoundException extends RuntimeException {
    public TeacherNotFoundException(String message) {
        super(message);
    }
}
