package com.jsebastian.eden.EdenSys.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "inmueble")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inmueble {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String departamento;

    @ManyToOne
    @JoinColumn(name = "ubicacion_id")
    private Ubicacion ubicacion;

    @Enumerated(EnumType.STRING)
    private TipoNegocio tipoNegocio;

    @ManyToOne
    @JoinColumn(name = "agente_asociado_id")
    private AgenteInmobiliario agenteAsociado;

    @OneToMany(mappedBy = "inmueble", cascade = CascadeType.ALL)
    private List<DocumentoImportante> documentosImportantes;

    @Enumerated(EnumType.STRING)
    private TipoInmueble tipo;

    @Positive
    private double medidas;

    @Positive
    private int habitaciones;

    @Positive
    private int banos;

    private String descripcion;

    @Enumerated(EnumType.STRING)
    private EstadoInmueble estado;

    @Enumerated(EnumType.STRING)
    private EstadoPosteoInmueble estadoPosteoInmueble;

    @Positive
    private double precio;

    @Enumerated(EnumType.STRING)
    private EstadoTransaccion estadoTransa;

    private String ciudad;

    private int codigoInmueble;

    @OneToMany(mappedBy = "inmueble", cascade = CascadeType.ALL)
    private List<HistorialInmueble> historial;

    @ManyToOne
    @JoinColumn(name = "asesor_legal_id")
    private AsesorLegal asesorLegal;

    @Positive
    private int cantidadParqueaderos;

    private String telfonoContacto;

    private String nombreContacto;

    private String correoContacto;

    @OneToMany(mappedBy = "inmueble", cascade = CascadeType.ALL)
    private List<Imagen> imagenes;

    @ManyToOne
    @JoinColumn(name = "propietario_id")
    private Cliente propietario;
}