package com.jsebastian.eden.EdenSys.Dtos;

import com.jsebastian.eden.EdenSys.domain.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

public record InmuebleResponse(
        @NotNull Long id,

        @NotNull TipoNegocio tipoNegocio,

        @Valid @NotNull User agenteAsociado,

        @Valid @NotNull User asesorLegal,

        @Valid @NotNull User propietario,

        @NotNull TipoInmueble tipo,

        @Positive double medidas,

        @Positive int habitaciones,

        @Positive int banos,

        @NotBlank String descripcion,

        @NotNull EstadoInmueble estado,

        @NotNull EstadoTransaccion estadoTransa,

        @Positive double precio,

        @Min(1) int cantidadParqueaderos,

        @NotBlank String telefonoContacto,

        @NotBlank String nombreContacto,

        @Email @NotBlank String correoContacto,

        @Min(-90) @Max(90) double latitud,

        @Min(-180) @Max(180) double longitud,

        @Valid @NotNull List<Imagen> imagenes,

        @Valid @NotNull List<DocumentoImportante> documentosImportantes,

        @Valid @NotNull List<HistorialInmueble> historial
) {}
