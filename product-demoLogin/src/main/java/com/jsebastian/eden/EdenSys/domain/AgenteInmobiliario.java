package com.jsebastian.eden.EdenSys.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@NotEmpty
@NotBlank
@Getter
@Setter
public class AgenteInmobiliario extends Empleado {
}
