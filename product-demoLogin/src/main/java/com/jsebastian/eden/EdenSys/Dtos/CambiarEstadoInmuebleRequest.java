package com.jsebastian.eden.EdenSys.Dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

public record CambiarEstadoInmuebleRequest(
        @NotNull Long inmuebleId,
        @NotBlank String nuevoEstado,
        String comentario
) {}

