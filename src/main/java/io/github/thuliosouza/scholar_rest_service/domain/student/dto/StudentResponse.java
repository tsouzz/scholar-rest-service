package io.github.thuliosouza.scholar_rest_service.domain.student.dto;

import io.github.thuliosouza.scholar_rest_service.domain.classgroup.ClassGroup;
import io.github.thuliosouza.scholar_rest_service.domain.student.Student;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public record StudentResponse(
        UUID id,
        String name,
        UUID classGroupId,
        String classGroupName,
        BigDecimal grade
) {
    public static StudentResponse from(Student student, BigDecimal grade) {
        return new StudentResponse(
                student.getId(),
                student.getName(),
                Optional.ofNullable(student.getClassGroup())
                        .map(ClassGroup::getId)
                        .orElse(null),
                Optional.ofNullable(student.getClassGroup())
                        .map(ClassGroup::getName)
                        .orElse(null),
                grade
        );
    }
}