package com.jsebastian.eden.EdenSys.domain;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@NotEmpty
@NotBlank
@Entity
@Getter
@Setter
public class AgenteInmobiliario extends Empleado {
}
