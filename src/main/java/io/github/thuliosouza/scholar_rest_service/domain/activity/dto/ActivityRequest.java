package io.github.thuliosouza.scholar_rest_service.domain.activity.dto;


import io.github.thuliosouza.scholar_rest_service.domain.activity.ActivityType;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record ActivityRequest(

        @NotNull(message = "O Id do aluno é obrigatório!")
        UUID studentId,

        @NotNull(message = "O tipo da atividade é obrigatório!")
        ActivityType activityType,

        @NotNull(message = "A nota é obrigatória!")
        @DecimalMin(value = "0.0", message = "A nota não pode ser menor que 0.")
        @DecimalMax(value = "10.0", message = "A nota não pode ser maior que 10.")
        BigDecimal grade
) {}
