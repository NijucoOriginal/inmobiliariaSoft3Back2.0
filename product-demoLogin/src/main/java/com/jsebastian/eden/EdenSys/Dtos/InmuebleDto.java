package com.jsebastian.eden.EdenSys.Dtos;

import com.jsebastian.eden.EdenSys.domain.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;

public record InmuebleDto(
    //@NotBlank(message = "El departamento es obligatorio") String departamento,
    @Valid @NotNull(message = "La ubicación es obligatoria") int longitud,
    @Valid @NotNull(message = "La ubicación es obligatoria") int latitud,
    @NotNull(message = "El tipo de negocio es obligatorio") TipoNegocio tipoNegocio,
    //@Valid @NotNull(message = "El agente asociado es obligatorio") User agenteAsociado,
    //List<DocumentoImportante> documentosImportantes,
    @NotNull(message = "El tipo de inmueble es obligatorio") TipoInmueble tipo,
    @Positive(message = "Las medidas deben ser mayores a 0") double medidas,
    @Positive(message = "El número de habitaciones debe ser mayor a 0") int habitaciones,
    @Positive(message = "El número de baños debe ser mayor a 0") int banos,
    @NotBlank(message = "La descripción es obligatoria") String descripcion,
    @NotNull(message = "El estado del inmueble es obligatorio") EstadoInmueble estado,
    @Positive(message = "El precio debe ser mayor a 0") double precio,
   // @NotNull(message = "El estado de la transacción es obligatorio") EstadoTransaccion estadoTransa, //se define en el back
    //@NotBlank(message = "La ciudad es obligatoria") String ciudad,
    //@Positive(message = "El código del inmueble debe ser mayor a 0") int codigoInmueble,
    //@Valid @NotNull(message = "El historial es obligatorio") List<HistorialInmueble> historial, //se define en el back
    //@Valid @NotNull(message = "El asesor legal es obligatorio") User asesorLegal,
    @Min(value = 1, message = "Debe haber al menos un parqueadero") int cantidadParqueaderos,
    @NotBlank(message = "El teléfono de contacto es obligatorio") String telefonoContacto,
    @NotBlank(message = "El nombre de contacto es obligatorio") String nombreContacto,
    @Email(message = "El correo debe tener un formato válido") @NotBlank(message = "El correo de contacto es obligatorio") String correoContacto
    //List<Imagen> imagenes
) {}
