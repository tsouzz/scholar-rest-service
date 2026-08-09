package io.github.thuliosouza.scholar_rest_service.domain.classgroup.dto;

import jakarta.validation.constraints.NotNull;

public record ClassGroupRequest(

        @NotNull(message = "O módulo é obrigatório")
        Module module

) {}
