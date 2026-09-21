package io.github.thuliosouza.scholar_rest_service.domain.activity.dto;

import io.github.thuliosouza.scholar_rest_service.domain.activity.Activity;
import io.github.thuliosouza.scholar_rest_service.domain.activity.ActivityType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ActivityResponse(
        UUID id,
        ActivityType activityType,
        String activityLabel,
        BigDecimal grade,
        LocalDate registrationDate,
        UUID studentId,
        String studentName
) {
    public static ActivityResponse from(Activity activity) {
        return new ActivityResponse(
                activity.getId(),
                activity.getActivityType(),
                activity.getActivityType().getLabel(),
                activity.getGrade(),
                activity.getRegistrationDate(),
                activity.getStudent().getId(),
                activity.getStudent().getName()
        );
    }
}
