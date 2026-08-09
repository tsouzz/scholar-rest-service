package io.github.thuliosouza.scholar_rest_service.domain.student.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record StudentResponse(
        UUID id,
        String name,
        UUID ClassGroupId,
        String ClassGroupName,
        BigDecimal grade
) {}
