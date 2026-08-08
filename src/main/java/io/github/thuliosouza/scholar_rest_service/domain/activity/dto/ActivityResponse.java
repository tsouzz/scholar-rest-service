package io.github.thuliosouza.scholar_rest_service.domain.activity.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ActivityResponse(
        UUID id,
        String activityLabel,
        BigDecimal grade,
        LocalDate registrationDate,
        UUID studentId,
        String studentName
) {}
