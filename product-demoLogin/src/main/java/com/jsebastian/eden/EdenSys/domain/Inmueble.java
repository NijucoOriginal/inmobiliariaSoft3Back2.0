package com.jsebastian.eden.EdenSys.domain;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.ArrayList;

@NotBlank
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inmueble {

    private String departamento;

    private Ubicacion ubicacion;

    private TipoNegocio tipoNegocio;

    private AgenteInmobiliario agenteAsociado;

    private ArrayList<DocumentoImportante> documentosImportantes;

    private TipoInmueble tipo;

    @Positive
    private double medidas;

    @Positive
    private int habitaciones;

    @Positive
    private int banos;

    private String descripcion;

    private EstadoInmueble estado;

    @Positive
    private double precio;

    private EstadoTransaccion estadoTransa;

    private String ciudad;

    private int codigoInmueble;

    private ArrayList<HistorialInmueble> historial;

    private AsesorLegal asesorLegal;

    @Min(1)
    private int cantidadParqueaderos;

    private String telfonoContacto;

    private String nombreContacto;

    private String correoContacto;

    private ArrayList<Imagen> imagenes;

}
