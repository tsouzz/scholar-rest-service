package io.github.thuliosouza.scholar_rest_service.domain.teacher.dto;

import io.github.thuliosouza.scholar_rest_service.domain.school.School;
import io.github.thuliosouza.scholar_rest_service.domain.teacher.Teacher;

import java.util.Optional;
import java.util.UUID;

public record TeacherResponse(UUID id, String name, String email, String schoolName) {

    public static TeacherResponse from(Teacher teacher) {
        return new TeacherResponse(
                teacher.getId(),
                teacher.getName(),
                teacher.getEmail(),
                Optional.ofNullable(teacher.getSchool())
                        .map(School::getName)
                        .orElse(null)
        );
    }
}
