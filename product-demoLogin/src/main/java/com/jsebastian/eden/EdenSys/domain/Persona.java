package com.jsebastian.eden.EdenSys.domain;

import jakarta.validation.constraints.*;
import lombok.*;

@NotBlank
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Persona {

    private String nombre;

    private String apellido;

    @Pattern(
            regexp = "^.{10}$",
            message = "Acuerdese que la cedula solo incluye 10 caracteres"
    )
    private int documentoIdentidad;

    @Email
    private String correoElectronico;

    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "La contraseña debe tener al " +
                    "menos una mayúscula, un número, un carácter especial y mínimo 8 caracteres"
    )
    private String contrasenia;

}
