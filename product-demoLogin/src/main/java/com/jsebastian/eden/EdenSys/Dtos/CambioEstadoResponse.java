package com.jsebastian.eden.EdenSys.Dtos;

import java.time.LocalDateTime;

public record CambioEstadoResponse(
        Long inmuebleId,
        String estadoAnterior,
        String estadoNuevo,
        String mensaje,
        LocalDateTime fechaActualizacion
) {}

