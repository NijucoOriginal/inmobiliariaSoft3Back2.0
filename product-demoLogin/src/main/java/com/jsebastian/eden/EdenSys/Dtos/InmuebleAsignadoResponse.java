package com.jsebastian.eden.EdenSys.Dtos;

public record InmuebleAsignadoResponse(
        Long id,
        String descripcion,
        String tipo,
        String estadoActual,
        Double precio
) {}

