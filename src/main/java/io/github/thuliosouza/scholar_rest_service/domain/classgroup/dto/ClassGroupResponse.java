package io.github.thuliosouza.scholar_rest_service.domain.classgroup.dto;

import java.util.UUID;

public record ClassGroupResponse(
        UUID id,
        String name,
        Module module,
        String teacherName
) {}
