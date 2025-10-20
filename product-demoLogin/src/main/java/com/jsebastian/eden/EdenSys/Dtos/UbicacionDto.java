package com.jsebastian.eden.EdenSys.Dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UbicacionDto(
        @NotNull(message = "La latitud es obligatoria") @Min(-90) @Max(90) Double latitud,
        @NotNull(message = "La longitud es obligatoria") @Min(-180) @Max(180) Double longitud
) {}
