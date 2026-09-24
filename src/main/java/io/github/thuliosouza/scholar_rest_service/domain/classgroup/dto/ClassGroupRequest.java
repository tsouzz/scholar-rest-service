package io.github.thuliosouza.scholar_rest_service.domain.classgroup.dto;

import io.github.thuliosouza.scholar_rest_service.domain.classgroup.Module;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ClassGroupRequest(

        @NotNull(message = "O módulo é obrigatório")
        Module module

) {}
