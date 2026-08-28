package io.github.thuliosouza.scholar_rest_service.infra.exception;

import java.time.LocalDateTime;

public record ApiError(
        int status,
        String error,
        String message,
        LocalDateTime timestamp
) {}
