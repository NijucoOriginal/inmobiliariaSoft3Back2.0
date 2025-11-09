package com.jsebastian.eden.EdenSys.Dtos;

import com.jsebastian.eden.EdenSys.domain.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;

public record InmuebleDto(
        @PositiveOrZero double longitud,
        @PositiveOrZero double latitud,
        @NotNull TipoNegocio tipoNegocio,
        @NotNull TipoInmueble tipo,
        @Positive double medidas,
        @Positive int habitaciones,
        @Positive int banos,
        @NotBlank String descripcion,
        @NotNull EstadoInmueble estado,
        @Positive double precio,
        @Min(1) int cantidadParqueaderos,
        @NotBlank String telefonoContacto,
        @NotBlank String nombreContacto,
        @Email @NotBlank String correoContacto
) {}

