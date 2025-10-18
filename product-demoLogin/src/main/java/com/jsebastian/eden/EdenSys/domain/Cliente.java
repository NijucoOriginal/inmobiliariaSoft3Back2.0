package com.jsebastian.eden.EdenSys.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;


@NotEmpty
@NotBlank
@Entity
public class Cliente extends Persona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "propietario")
    private List<Inmueble> inmueblesPropios;

    @OneToMany(mappedBy = "cliente")
    private List<DocumentoImportante> documentsAdjuntos;
}