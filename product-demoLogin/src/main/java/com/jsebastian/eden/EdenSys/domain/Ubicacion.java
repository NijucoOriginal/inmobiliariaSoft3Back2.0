package com.jsebastian.eden.EdenSys.domain;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@NotEmpty
@NotBlank
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ubicacion {

    @Min(-90)
    @Max(90)
    private int latitud;
    @Min(-180)
    @Max(180)
    private int longitud;
    private String descripcion;
}
