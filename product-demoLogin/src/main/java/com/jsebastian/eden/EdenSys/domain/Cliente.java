package com.jsebastian.eden.EdenSys.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.ArrayList;


@NotEmpty
@NotBlank
@Getter
@Setter
public class Cliente extends Persona {

    private ArrayList<Inmueble> inmublesPropios;

    private ArrayList<DocumentoImportante> documentsAdjuntos;
}
