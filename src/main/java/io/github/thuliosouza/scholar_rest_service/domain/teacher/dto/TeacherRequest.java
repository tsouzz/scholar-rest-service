package io.github.thuliosouza.scholar_rest_service.domain.teacher.dto;


public record TeacherRequest(
        String name,
        String email,
        String password,
        String confirmPassword,
        String schoolName
) {}
