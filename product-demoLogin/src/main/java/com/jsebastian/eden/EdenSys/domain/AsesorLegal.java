package com.jsebastian.eden.EdenSys.domain;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Entity
@NotEmpty
@NotBlank
@Getter
@Setter
public class AsesorLegal extends Empleado {
    // Hereda id de Empleado
}
