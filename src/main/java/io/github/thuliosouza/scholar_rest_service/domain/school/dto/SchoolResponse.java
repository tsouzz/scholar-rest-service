package io.github.thuliosouza.scholar_rest_service.domain.school.dto;

import java.util.UUID;

public record SchoolResponse(
        UUID id,
        String name
) {}
