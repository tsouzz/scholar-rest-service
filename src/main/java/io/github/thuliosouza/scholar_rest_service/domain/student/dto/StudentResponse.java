package io.github.thuliosouza.scholar_rest_service.domain.student.dto;

import io.github.thuliosouza.scholar_rest_service.domain.student.Student;

import java.util.UUID;

public record StudentResponse(
        UUID id,
        String name,
        UUID ClassGroupId,
        String ClassGroupName
) {
    public static StudentResponse from(Student student) {
        return new StudentResponse(
                student.getId(),
                student.getName(),
                student.getClassGroup().getId(),
                student.getClassGroup().getName()
        );
    }
}
