package com.jsebastian.eden.EdenSys.Dtos;

public record ClienteAsociadoResponse(
        Long clienteId,
        String nombreCompleto,
        String correo,
        String telefono
) {}

