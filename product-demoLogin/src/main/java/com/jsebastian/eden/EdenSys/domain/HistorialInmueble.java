package com.jsebastian.eden.EdenSys.domain;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDateTime;

@NotBlank
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistorialInmueble {

    @PastOrPresent
    private LocalDateTime fechaInicio;

    @PastOrPresent
    private LocalDateTime fechaFin;

    private TipoNegocio tipoNegocio;

    @Positive
    private double precio;

    private String descripcion;

    private int idHistorial;

    private Cliente propietario;
}
