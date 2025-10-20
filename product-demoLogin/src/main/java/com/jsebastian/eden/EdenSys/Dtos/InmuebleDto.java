package com.jsebastian.eden.EdenSys.Dtos;

import jakarta.validation.constraints.*;
import java.util.List;

public record InmuebleDto(
    @NotBlank(message = "El departamento es obligatorio") String departamento,
    @NotBlank(message = "La ciudad es obligatoria") String ciudad,
    @NotNull(message = "La ubicación es obligatoria") UbicacionDto ubicacion,
    @NotNull(message = "El tipo de negocio es obligatorio") String tipoNegocio,
    @NotNull(message = "El tipo de inmueble es obligatorio") String tipo,
    @Positive(message = "Las medidas deben ser mayores a 0") double medidas,
    @Positive(message = "El número de habitaciones debe ser mayor a 0") int habitaciones,
    @Positive(message = "El número de baños debe ser mayor a 0") int banos,
    @NotBlank(message = "La descripción es obligatoria") String descripcion,
    @Positive(message = "El precio debe ser mayor a 0") double precio,
    @Min(value = 1, message = "Debe haber al menos un parqueadero") int cantidadParqueaderos,
    @NotBlank(message = "El teléfono de contacto es obligatorio") String telfonoContacto,
    @NotBlank(message = "El nombre de contacto es obligatorio") String nombreContacto,
    @Email(message = "El correo debe tener un formato válido") @NotBlank(message = "El correo de contacto es obligatorio") String correoContacto,
    @NotNull(message = "La lista de imágenes es obligatoria") List<@NotBlank(message = "La URL de la imagen no puede estar vacía") String> imagenes
) {}
