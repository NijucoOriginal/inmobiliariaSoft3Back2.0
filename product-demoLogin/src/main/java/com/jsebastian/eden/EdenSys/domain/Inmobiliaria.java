package com.jsebastian.eden.EdenSys.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.ArrayList;


@NotEmpty
@NotBlank
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inmobiliaria {

    private ArrayList<Cliente> clientes;
    private ArrayList<Inmueble> inmuebles;
    private ArrayList<AgenteInmobiliario> agentes;
    private ArrayList<AsesorLegal> asesoresLegales;
    private Ubicacion ubicacion;
}
