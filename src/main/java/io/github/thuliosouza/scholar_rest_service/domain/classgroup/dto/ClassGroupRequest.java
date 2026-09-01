package io.github.thuliosouza.scholar_rest_service.domain.classgroup.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ClassGroupRequest(

        @NotNull(message = "O número/nome da turma é obrigatório")
        int number,

        @NotNull(message = "O módulo é obrigatório")
        Module module,

        @NotNull(message = "O ID do professor é obrigatório")
        UUID teacherId

) {}
