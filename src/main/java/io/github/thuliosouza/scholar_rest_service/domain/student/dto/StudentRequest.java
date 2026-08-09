package io.github.thuliosouza.scholar_rest_service.domain.student.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record StudentRequest(

        @NotNull(message = "O nome é obrigatório!")
        String name,

        @NotNull(message = "O id do grupo é obrigatório")
        UUID classGroupId
) {}
