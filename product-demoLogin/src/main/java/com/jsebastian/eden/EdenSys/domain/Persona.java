package com.jsebastian.eden.EdenSys.domain;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Superclase mapeada para credenciales básicas.
 * Provee correo, contraseña y rol por defecto USER.
 */
@MappedSuperclass
@Getter
@Setter
public abstract class Persona {

    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El formato del correo electrónico no es válido")
    @Size(max = 150, message = "El correo electrónico no puede exceder 150 caracteres")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "La contraseña debe tener al menos una mayúscula, un número, un carácter especial y mínimo 8 caracteres"
    )
    private String contrasena;

    @Enumerated(EnumType.STRING)
    private Rol rol = Rol.USER;
}
