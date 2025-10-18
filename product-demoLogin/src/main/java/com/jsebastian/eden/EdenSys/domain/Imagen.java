package com.jsebastian.eden.EdenSys.domain;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

@NotBlank
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Imagen {

    private String nombre;
    private String url;
    private String descripcion;
    private String codigoImagen;
}
