package io.github.thuliosouza.scholar_rest_service.domain.teacher.dto;

import java.util.UUID;

public record TeacherResponse(
        UUID id,
        String name,
        String email,
        String schoolName
) {}
